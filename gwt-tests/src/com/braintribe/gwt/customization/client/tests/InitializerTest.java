// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.gwt.customization.client.tests;

import java.math.BigDecimal;
import java.util.Date;

import com.braintribe.gwt.customization.client.tests.model.initializer.Color;
import com.braintribe.gwt.customization.client.tests.model.initializer.InitializedEntity;
import com.braintribe.gwt.customization.client.tests.model.initializer.InitializedSubEntity;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;

/**
 * @author peter.gazdik
 */
public class InitializerTest extends AbstractGmGwtTest {

	private static final long SECOND_IN_MILLIS = 1000;

	@Override
	protected void tryRun() throws GmfException {
		GmMetaModel metaModel = generateModel("test.gwt27.InitializerModel", InitializedEntity.T, InitializedSubEntity.T);

		makeSignaturesDynamic(metaModel, false);
		ensureModelTypes(metaModel);

		testInitialized(InitializedEntity.class.getName());
		testInitialized(makeSignatureDynamic(InitializedEntity.class.getName()));

		EntityType<?> et = typeReflection.getEntityType(makeSignatureDynamic(InitializedEntity.class.getName()));
		GenericEntity entity = et.create();

		log("OldBoolean: " + et.getProperty("uninitializedBooleanValue").setDirect(entity, true));
		log("OldLong: " + et.getProperty("uninitializedLongValue").setDirect(entity, 0l));

		testInitializedSub(InitializedSubEntity.class.getName());
		testInitializedSub(makeSignatureDynamic(InitializedSubEntity.class.getName()));
	}

	private void testInitialized(String typeSignature) {
		EntityType<?> et = typeReflection.getEntityType(typeSignature);

		testInitialized(et, "enhanced", et.create());
	}

	private void testInitialized(EntityType<?> et, String type, GenericEntity entity) {
		logTestingEntity(et);

		assertProperty(et, entity, "intValue", 99);
		assertProperty(et, entity, "longValue", 11L);
		assertProperty(et, entity, "floatValue", 123f);
		assertProperty(et, entity, "doubleValue", -123D);
		assertProperty(et, entity, "bigFloatValue", 1.0e30f);
		assertProperty(et, entity, "bigDoubleValue", -1.0e30d);
		assertProperty(et, entity, "decimalValue", new BigDecimal("99889988.00"));
		assertProperty(et, entity, "booleanValue", true);
		assertProperty(et, entity, "enumValue", Color.green);
		assertProperty(et, entity, "enumShort", Color.green);
		assertProperty(et, entity, "uninitializedDateValue", null);
		assertProperty(et, entity, "uninitializedBooleanValue", false);
		assertProperty(et, entity, "uninitializedLongValue", 0L);
		assertDateNow(et, type, entity, "dateValue");
	}

	private void testInitializedSub(String typeSignature) {
		EntityType<GenericEntity> et = typeReflection.getEntityType(typeSignature);

		GenericEntity entity = et.create();

		logTestingEntity(et);

		assertProperty(et, entity, "intValue", 88); // overridden
		assertProperty(et, entity, "longValue", 0L); // overridden with 0 as nothing was stated, but this is
														// default
		assertProperty(et, entity, "newLongValue", 0L); // new property which is primitive
		assertProperty(et, entity, "floatValue", 123f); // overridden with null as nothing was stated, but this is
														// default
		assertProperty(et, entity, "booleanValue", true);
		assertProperty(et, entity, "dateValue", null); // inherited
	}

	private void assertProperty(EntityType<?> et, GenericEntity entity, String propertyName, Object expected) {
		Object actual = et.getProperty(propertyName).get(entity);

		if (equalsOrBothNull(expected, actual))
			log("    property '" + propertyName + "' [OK]");
		else
			logError("Property: " + et.getTypeSignature() + "." + propertyName + " has wrong value. Expected: " + expected + ", actual: " + actual);
	}

	protected boolean equalsOrBothNull(Object expected, Object actual) {
		if (expected == null)
			return isReallyNull(actual);
		else
			return expected.equals(actual);
	}

	private void assertDateNow(EntityType<?> et, String type, GenericEntity entity, String propertyName) {
		Date actual = (Date) et.getProperty(propertyName).get(entity);
		if (actual == null) {
			logError("Property: [" + type + "] " + et.getTypeSignature() + "." + propertyName + " should not be null!");
			return;
		}
		Date now = new Date();
		long diffInMillis = now.getTime() - actual.getTime();
		if (diffInMillis > 10 * SECOND_IN_MILLIS) {
			logError("Property: [" + type + "] " + et.getTypeSignature() + "." + propertyName
					+ " has wrong value. The difference in millis is too big. Now: " + now + ", actual: " + actual + ", Difference: " + diffInMillis);
		} else {
			log("    property '" + propertyName + "' [OK]");
		}
	}

	private void logTestingEntity(EntityType<?> et) {
		log("Testing '" + et.getTypeSignature());
	}

}

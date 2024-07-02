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
import com.braintribe.utils.lcd.CommonTools;

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

		testInitialized(et, "plain", et.createPlain());
		testInitialized(et, "enhanced", et.create());
	}

	private void testInitialized(EntityType<?> et, String type, GenericEntity entity) {
		logTestingEntity(et, type);

		assertProperty(et, type, entity, "intValue", 99);
		assertProperty(et, type, entity, "longValue", 11L);
		assertProperty(et, type, entity, "floatValue", 123f);
		assertProperty(et, type, entity, "doubleValue", -123D);
		assertProperty(et, type, entity, "bigFloatValue", 1.0e30f);
		assertProperty(et, type, entity, "bigDoubleValue", -1.0e30d);
		assertProperty(et, type, entity, "decimalValue", new BigDecimal("99889988.00"));
		assertProperty(et, type, entity, "booleanValue", true);
		assertProperty(et, type, entity, "enumValue", Color.green);
		assertProperty(et, type, entity, "enumShort", Color.green);
		assertProperty(et, type, entity, "uninitializedDateValue", null);
		assertProperty(et, type, entity, "uninitializedBooleanValue", false);
		assertProperty(et, type, entity, "uninitializedLongValue", 0L);
		assertDateNow(et, type, entity, "dateValue");
	}

	private void testInitializedSub(String typeSignature) {
		EntityType<GenericEntity> et = typeReflection.getEntityType(typeSignature);

		testInitializedSub(et, "plain", et.createPlain());
		testInitializedSub(et, "enhanced", et.create());
	}

	private void testInitializedSub(EntityType<?> et, String type, GenericEntity entity) {
		logTestingEntity(et, type);

		assertProperty(et, type, entity, "intValue", 88); // overridden
		assertProperty(et, type, entity, "longValue", 0L); // overridden with 0 as nothing was stated, but this is
															// default
		assertProperty(et, type, entity, "newLongValue", 0L); // new property which is primitive
		assertProperty(et, type, entity, "floatValue", 123f); // overridden with null as nothing was stated, but this is
																// default
		assertProperty(et, type, entity, "booleanValue", true);
		assertProperty(et, type, entity, "dateValue", null); // inherited
	}

	private void assertProperty(EntityType<?> et, String type, GenericEntity entity, String propertyName, Object expected) {
		Object actual = et.getProperty(propertyName).get(entity);

		if (CommonTools.equalsOrBothNull(expected, actual))
			log("    property '" + propertyName + "' [OK]");
		else
			logError("Property: [" + type + "] " + et.getTypeSignature() + "." + propertyName + " has wrong value. Expected: " + expected
					+ ", actual: " + actual);
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

	private void logTestingEntity(EntityType<?> et, String type) {
		log("Testing '" + et.getTypeSignature() + "'[" + type + "]");
	}

}

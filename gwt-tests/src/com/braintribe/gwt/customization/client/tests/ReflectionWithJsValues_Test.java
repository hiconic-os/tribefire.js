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
import com.braintribe.gwt.customization.client.tests.model.simple.PropsEntity;
import com.braintribe.gwt.customization.client.tests.model.simple.PropsEntitySub;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.reflection.EssentialTypes;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.utils.lcd.CommonTools;

/**
 * @author peter.gazdik
 */
public class ReflectionWithJsValues_Test extends AbstractGmGwtTest {

	// Special handling when getting/setting via JS properties (so not getter/setter)
	//
	// All other cases the type is the same.
	// For some reason String in Java is also String in JS without us having to do anything
	//
	// [Compile-time Java type] <-> [Runtime JS Type]
	// boolean <-> boolean (JS)
	// int <-> Number
	// float <-> Number {type: 'f'}
	// double <-> Number {type: 'd'}
	// long <-> BigInt
	// Date (Java) <-> Date (JS)

	private PropsEntity e;

	@Override
	protected void tryRun() throws GmfException {
		initPropsEntity();

		testIsInstanceInJs();
		testEmptyValueIsEmptyInJs();
		testGetActualTypeInJs();
	}

	private void initPropsEntity() {
		Boolean boolVal = Boolean.TRUE;
		int intVal = 23;
		long longVal = 1234567890L;
		float floatVal = 42f;
		double doubleVal = 99d;
		BigDecimal bigDecimalVal = new BigDecimal(420);
		String stringVal = "unnamed string";
		Date dateVal = new Date();

		e = PropsEntity.T.create();
		e.setId("id");
		e.setPrimitiveBoolean(true);
		e.setBooleanWrapper(boolVal);
		e.setPrimitiveInteger(intVal);
		e.setIntegerWrapper(intVal);
		e.setPrimitiveLong(longVal);
		e.setLongWrapper(longVal);
		e.setPrimitiveFloat(floatVal);
		e.setFloatWrapper(floatVal);
		e.setPrimitiveDouble(doubleVal);
		e.setDoubleWrapper(doubleVal);
		e.setBigDecimal(bigDecimalVal);
		e.setString(stringVal);
		e.setDate(dateVal);

		e.setColor(Color.green);
		e.setOtherEntity(PropsEntitySub.T.create());

		e.getListString().add(stringVal);
		e.getSetString().add(stringVal);
		e.getMapIntegerString().put(intVal, stringVal);
	}

	// #######################################################################################################################################
	
	private void testIsInstanceInJs() {
		logTesting("Testing GenericModelType.isInstance");
		assertValueIsInstanceOfPropertyTypeInJs(e);
	}

	private native void assertValueIsInstanceOfPropertyTypeInJs(PropsEntity e) /*-{
		var props = e.Properties();
		for(var i=0; i<props.length; i++) {
			var prop = props[i];
			var type = prop.getType();
			var value = e[prop.getName()];
			if (value != null)
				this.assertTrue(
					type.isInstance(value),
					"Value " + value + " of prop '" + prop.getName() + "' instance of " + type.getTypeName()
				);
		}
	}-*/;

	// #######################################################################################################################################
	
	private void testEmptyValueIsEmptyInJs() {
		logTesting("Testing Propety.isEmptyvalue");

		PropsEntity emptyE = PropsEntity.T.create();

		assertValueIsEmpty(emptyE, "primitiveBoolean", true);
		assertValueIsEmpty(emptyE, "primitiveInteger", true);
		assertValueIsEmpty(emptyE, "primitiveLong", true);
		assertValueIsEmpty(emptyE, "primitiveFloat", true);
		assertValueIsEmpty(emptyE, "primitiveDouble", true);

		assertValueIsEmpty(emptyE, "id", true);
		assertValueIsEmpty(emptyE, "color", true);
		assertValueIsEmpty(emptyE, "otherEntity", true);

		assertValueIsEmpty(emptyE, "listString", true);
		assertValueIsEmpty(emptyE, "setString", true);
		assertValueIsEmpty(emptyE, "mapIntegerString", true);

		assertValueIsEmpty(e, "primitiveBoolean", false);
		assertValueIsEmpty(e, "primitiveInteger", false);
		assertValueIsEmpty(e, "primitiveLong", false);
		assertValueIsEmpty(e, "primitiveFloat", false);
		assertValueIsEmpty(e, "primitiveDouble", false);

		assertValueIsEmpty(e, "id", false);
		assertValueIsEmpty(e, "color", false);
		assertValueIsEmpty(e, "otherEntity", false);

		assertValueIsEmpty(e, "listString", false);
		assertValueIsEmpty(e, "setString", false);
		assertValueIsEmpty(e, "mapIntegerString", false);
	}

	private void assertValueIsEmpty(PropsEntity e, String propName, boolean isEmtpy) {
		Property p = e.entityType().getProperty(propName);
		assertValueIsEmptyJs(e, propName, p, isEmtpy);
	}

	private native void assertValueIsEmptyJs(PropsEntity e, String propName, Property p, boolean isEmtpy) /*-{
		var val = e[propName];
		this.assertEqual(p.isEmptyValue(val), isEmtpy, "Empty value for prop '" + propName + "'");
	}-*/;

	// #######################################################################################################################################

	private void testGetActualTypeInJs() {
		logTesting("Testing GenericModelType.getActualtype");

		assertActualType(e, "primitiveBoolean", EssentialTypes.TYPE_BOOLEAN);
		assertActualType(e, "primitiveInteger", EssentialTypes.TYPE_INTEGER);
		assertActualType(e, "primitiveLong", EssentialTypes.TYPE_LONG);
		assertActualType(e, "primitiveFloat", EssentialTypes.TYPE_FLOAT);
		assertActualType(e, "primitiveDouble", EssentialTypes.TYPE_DOUBLE);

		assertActualType(e, "booleanWrapper", EssentialTypes.TYPE_BOOLEAN);
		assertActualType(e, "integerWrapper", EssentialTypes.TYPE_INTEGER);
		assertActualType(e, "longWrapper", EssentialTypes.TYPE_LONG);
		assertActualType(e, "floatWrapper", EssentialTypes.TYPE_FLOAT);
		assertActualType(e, "doubleWrapper", EssentialTypes.TYPE_DOUBLE);

		assertActualType(e, "string", EssentialTypes.TYPE_STRING);
		assertActualType(e, "date", EssentialTypes.TYPE_DATE);
		assertActualType(e, "bigDecimal", EssentialTypes.TYPE_DECIMAL);

		assertActualType(e, "color", Color.T);
		assertActualType(e, "otherEntity", PropsEntitySub.T);
	}

	private void assertActualType(PropsEntity e, String propName, GenericModelType valType) {
		Property p = e.entityType().getProperty(propName);
		assertActualTypeJs(e, propName, p, valType);
	}

	/** Uses {@link EssentialTypes} */
	private native void assertActualTypeJs(PropsEntity e, String propName, Property p, GenericModelType expectedType) /*-{
		var val = e[propName];

		this.assertEqual(p.getType().getActualType(val), expectedType,
			"prop.getType().getActualType for: " + propName);
		this.assertEqual(@EssentialTypes::TYPE_OBJECT.getActualType(val), expectedType,
			"BaseType.getActualType       for: " + propName);
	}-*/;

	// #######################################################################################################################################
	
	private void logTesting(String useCase) {
		log("UseCase: " + useCase);
	}

	private void assertEqual(Object actual, Object expected, String propertyName) {
		if (CommonTools.equalsOrBothNull(expected, actual))
			log("    " + propertyName + " == " + expected + "  [OK]");
		else
			logError("Property: [" + propertyName + "] " + " has wrong value. " + //
					"Expected: " + expected + "(" + expected.getClass() + ")" + //
					", actual: " + actual + "(" + actual.getClass() + ")");
	}

}

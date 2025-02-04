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

import static com.braintribe.utils.lcd.CollectionTools2.asMap;
import static com.braintribe.utils.lcd.CollectionTools2.asSet;
import static com.braintribe.utils.lcd.CollectionTools2.first;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.gwt.customization.client.tests.model.simple.PropsEntity;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.collection.PlainList;
import com.braintribe.model.generic.collection.PlainMap;
import com.braintribe.model.generic.collection.PlainSet;
import com.braintribe.model.generic.reflection.EssentialTypes;
import com.braintribe.model.generic.reflection.ListType;
import com.braintribe.model.generic.reflection.MapType;
import com.braintribe.model.generic.reflection.SetType;
import com.braintribe.utils.lcd.CommonTools;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
//import com.google.gwt.core.client.UnsafeNativeLong;

/**
 * @author peter.gazdik
 */
public class Props_CompileTimeEntity_Test extends AbstractGmGwtTest {

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

	@Override
	protected void tryRun() throws GmfException {
		testInJava_SimpleValues();
		testInJava_WrapperValues();

		testSettingInJsGettingInJava();
		testSettingInJavaGettingInJs();

		testConversionsForObjectProperty();

		testSettingNativeJavaCollections();
	}

	private void testInJava_SimpleValues() {
		logTesting("Setting primitive values in Java");

		PropsEntity entity = PropsEntity.T.create();
		entity.setPrimitiveBoolean(true);
		entity.setPrimitiveInteger(23);
		entity.setPrimitiveLong(4711L);
		entity.setPrimitiveFloat((float) Math.PI);
		entity.setBooleanWrapper(true);
		entity.setIntegerWrapper(23);
		entity.setLongWrapper(4711L);
		entity.setFloatWrapper((float) Math.PI);
		entity.setPrimitiveDouble(Math.E);
		entity.setDoubleWrapper(Math.E);

		assertTrue(entity.getPrimitiveBoolean() == true, "primitiveBoolean == true");
		assertTrue(entity.getPrimitiveInteger() == 23, "primitiveInteger == 23");
		assertTrue(entity.getPrimitiveLong() == 4711L, "primitiveLong == 4711L");
		assertTrue(entity.getPrimitiveFloat() == (float) Math.PI, "primitiveFloat == Math.PI");
		assertTrue(entity.getPrimitiveDouble() == Math.E, "primitiveDouble == Math.E");

		assertEqual(entity.getBooleanWrapper(), true, "booleanWrapper");
		assertEqual(entity.getIntegerWrapper(), 23, "integerWrapper");
		assertEqual(entity.getLongWrapper(), 4711L, "longWrapper");
		assertEqual(entity.getFloatWrapper(), (float) Math.PI, "floatWrapper");
		assertEqual(entity.getDoubleWrapper(), Math.E, "doubleWrapper");
	}

	private void testInJava_WrapperValues() {
		logTesting("Setting object values in Java");

		Boolean boolVal = Boolean.TRUE;
		Integer intVal = 23;
		Long longVal = 4711L;
		Double doubleVal = Math.E;
		Float floatVal = (float) Math.PI;
		BigDecimal bigDecimalVal = new BigDecimal(420);
		String stringVal = "unnamed string";
		Date dateVal = new Date();

		PropsEntity e = PropsEntity.T.create();
		e.setBooleanWrapper(boolVal);
		e.setIntegerWrapper(intVal);
		e.setLongWrapper(longVal);
		e.setFloatWrapper(floatVal);
		e.setDoubleWrapper(doubleVal);
		e.setBigDecimal(bigDecimalVal);
		e.setString(stringVal);
		e.setDate(dateVal);

		assertEqual(e.getBooleanWrapper(), true, "booleanWrapper");
		assertEqual(e.getIntegerWrapper(), 23, "integerWrapper");
		assertEqual(e.getLongWrapper(), 4711L, "longWrapper");
		assertEqual(e.getFloatWrapper(), (float) Math.PI, "floatWrapper");
		assertEqual(e.getDoubleWrapper(), Math.E, "doubleWrapper");
		assertEqual(e.getBigDecimal(), bigDecimalVal, "bigDecimal");
		assertEqual(e.getString(), stringVal, "string");
		assertEqual(e.getDate(), dateVal, "date");
	}

	private void testSettingInJsGettingInJava() {
		logTesting("Setting in JS, getting in Java");

		long primitiveLongVal = Long.MAX_VALUE;
		Long longWrapperVal = Long.MAX_VALUE;
		Date date = new Date();
		JsDate dateVal = JsDate.create(date.getTime());

		// Fill
		PropsEntity e = PropsEntity.T.create();
		fillNativeJs(e, "" + primitiveLongVal, dateVal);

		// Assert
		assertTrue(e.getPrimitiveBoolean() == true, "primitiveBoolean == true");
		assertTrue(e.getPrimitiveInteger() == 23, "primitiveInteger == 23");
		assertTrue(e.getPrimitiveLong() == primitiveLongVal, "primitiveLong == Long.MAX_VALUE");
		assertTrue(e.getPrimitiveFloat() == 123f, "primitiveFloat == 123");
		assertTrue(e.getPrimitiveDouble() == 420d, "primitiveDouble == 420");

		assertEqual(e.getBooleanWrapper(), true, "booleanWrapper");
		assertEqual(e.getIntegerWrapper(), 23, "integerWrapper");
		assertEqual(e.getLongWrapper(), longWrapperVal, "longWrapper");
		assertEqual(e.getFloatWrapper(), 123f, "floatWrapper");
		assertEqual(e.getDoubleWrapper(), 420d, "doubleWrapper");
		assertEqual(e.getString(), "unnamed string", "string");
		assertEqual(e.getDate(), date, "date");

		assertEqual(first(e.getListObject()), date, "listObject(0)");
		assertEqual(first(e.getListString()), "hello", "listString(0)");
		assertEqual(first(e.getListFloat()), 123f, "listFloat(0)");
		assertEqual(first(e.getListDouble()), 420d, "listDouble(0)");
		assertEqual(first(e.getListLong()), longWrapperVal, "listLong(0)");
		assertEqual(first(e.getListDate()), date, "listDate(0)");

		assertEqual(first(e.getSetString()), "hello", "first(setString)");
		assertEqual(first(e.getSetDate()), date, "first(setDate)");

		assertEqual(first(e.getMapIntegerString().keySet()), 23, "first(mapStringString.keySet())");
		assertEqual(first(e.getMapIntegerString().values()), "value", "first(mapStringString.values())");

		assertEqual(first(e.getMapFloatDate().keySet()), 123f, "first(mapDateDate.keySet())");
		assertEqual(first(e.getMapFloatDate().values()), date, "first(mapDateDate.values())");
	}

	// $hcjs is a local variable for the right window/globalThis object
	private native void fillNativeJs(PropsEntity entity, String longVal, JsDate dateVal) /*-{
		entity.primitiveBoolean = true;
		entity.booleanWrapper = true;
		entity.primitiveInteger = 23;
		entity.integerWrapper = 23;
		entity.primitiveLong = BigInt(longVal);
		entity.longWrapper = BigInt(longVal);
		entity.primitiveFloat = new $hcjs.T.Double(123);
		entity.floatWrapper = new $hcjs.T.Double(123);
		entity.primitiveDouble = new $hcjs.T.Double(420);
		entity.doubleWrapper = new $hcjs.T.Double(420);
		entity.string = "unnamed string";
		entity.date = dateVal;

		entity.listObject.push(dateVal);
		entity.listString.push("hello");
		entity.listFloat.push(123);
		entity.listDouble.push(420);
		entity.listLong.push(BigInt(longVal));
		entity.listDate.push(dateVal);
		
		entity.setString.add("hello");
		entity.setDate.add(dateVal);

		entity.mapIntegerString.set(23, "value");
		entity.mapFloatDate.set(123, dateVal);
	}-*/;

	private void testSettingInJavaGettingInJs() {
		logTesting("Setting in Java, getting in Js");

		Boolean boolVal = Boolean.TRUE;
		int intVal = 23;
		long longVal = 1234567890L;
		float floatVal = 42f;
		double doubleVal = 99d;
		BigDecimal bigDecimalVal = new BigDecimal(420);
		String stringVal = "unnamed string";
		Date dateVal = new Date();

		PropsEntity e = PropsEntity.T.create();
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

		e.getListObject().add(dateVal);
		e.getListString().add(stringVal);
		e.getListFloat().add(floatVal);
		e.getListDouble().add(doubleVal);
		e.getListLong().add(longVal);
		e.getListDate().add(dateVal);

		e.getSetString().add(stringVal);
		e.getSetDate().add(dateVal);

		e.getMapIntegerString().put(intVal, stringVal);
		e.getMapFloatDate().put(floatVal, dateVal);

		JsDate jsDateVal = JsDate.create(dateVal.getTime());

		assertNativeJs(e, jsDateVal);

		assertEqualWithJsGet(e, "bigDecimal", bigDecimalVal);
	}

	private native void assertNativeJs(PropsEntity e, JsDate dateVal) /*-{
		this.assertEqual(e.primitiveBoolean, true, "primitiveBoolean == true");
		this.assertEqual(e.booleanWrapper, true, "booleanWrapper == true");
		this.assertEqual(e.primitiveInteger, 23, "primitiveInteger == 23");
		this.assertEqual(e.integerWrapper, 23, "integerWrapper == 23");
		this.assertEqual(e.primitiveLong, BigInt(1234567890), "primitiveLong == 1234567890n");
		this.assertEqual(e.longWrapper, BigInt(1234567890), "longWrapper == 1234567890n");
		this.assertEqual(e.primitiveFloat, 42, "primitiveFloat == 42");
		this.assertEqual(e.floatWrapper, 42, "floatWrapper == 42");
		this.assertEqual(e.primitiveDouble, 99, "primitiveDouble == 99");
		this.assertEqual(e.doubleWrapper, 99, "doubleWrapper == 99");
		this.assertEqual(e.string, "unnamed string", "string == 'unnamed string'");
		this.assertEqual(e.date.valueOf(), dateVal.valueOf(), "date.valueOf() == " + dateVal + ".valueOf() == " + dateVal.valueOf());

		this.assertOperation(e, 'x.primitiveFloat.type()', 'f');
		this.assertOperation(e, 'x.floatWrapper.type()', 'f');
		this.assertOperation(e, 'x.primitiveDouble.type()', 'd');
		this.assertOperation(e, 'x.doubleWrapper.type()', 'd');

		this.assertOperation(e, '[...x.listObject][0].valueOf()', dateVal.valueOf());
		this.assertOperation(e, '[...x.listString][0]', "unnamed string");
		this.assertOperation(e, '[...x.listFloat][0]', 42);
		this.assertOperation(e, '[...x.listDouble][0]', 99);
		this.assertOperation(e, '[...x.listLong][0]', BigInt(1234567890));
		this.assertOperation(e, '[...x.listDate][0].valueOf()', dateVal.valueOf());
	
		this.assertOperation(e, '[...x.setString][0]', "unnamed string");
		this.assertOperation(e, '[...x.setDate][0].valueOf()', dateVal.valueOf());
	
		this.assertOperation(e, '[...x.mapIntegerString.keys()][0]', 23);
		this.assertOperation(e, '[...x.mapIntegerString.values()][0]', "unnamed string");
	
		this.assertOperation(e, '[...x.mapFloatDate.keys()][0]', 42);
		this.assertOperation(e, '[...x.mapFloatDate.values()][0].valueOf()', dateVal.valueOf());
	}-*/;

	private void assertEqualWithJsGet(PropsEntity entity, String propertyName, Object expected) {
		assertEqual(getProperty(entity, propertyName), expected, propertyName);
	}

	private native Object getProperty(PropsEntity entity, String propertyName) /*-{
		return entity[propertyName];
	}-*/;

	private void testConversionsForObjectProperty() {
		logTesting("Object property conversions");

		PropsEntity e = PropsEntity.T.create();

		// null
		e.setId(null);
		assertIdIsNullInJs(e);
		setIdAsNullInJs(e);
		assertEqual(e.getId(), null, "id is null");

		// Integer
		e.setId(1);
		assertIdIsJsNumber(e);
		setIdAsNumberInJs(e, "new Number(1)");
		assertEqual(e.getId(), 1, "id (Integer)");

		// Long
		e.setId(1L);
		assertIdIsBigInt(e);
		setIdAsBigInt(e);
		assertEqual(e.getId(), 1l, "id (Long)");

		// Float
		e.setId(1f);
		assertIdIsJsNumber(e, "f");
		setIdAsNumberInJs(e, "new $hcjs.T.Float(1)");
		assertEqual(e.getId(), 1f, "id (Float)");

		// Double
		e.setId(1d);
		assertIdIsJsNumber(e, "d");
		setIdAsNumberInJs(e, "new $hcjs.T.Double(1)");
		assertEqual(e.getId(), 1d, "id (Double)");

		// Date
		e.setId(new Date(1));
		assertIdIsJsDate(e);
		setIdAsJsDate(e);
		assertEqual(e.getId(), new Date(1), "id (Date)");

		// List
		ListType listStringType = typeReflection.getListType(EssentialTypes.TYPE_STRING);
		e.setId(new PlainList<>(listStringType, Arrays.asList("a", "b")));
		assertOperation(e, "[...x.id][0]", "a");

		// Set
		SetType setDateType = typeReflection.getSetType(EssentialTypes.TYPE_DATE);
		e.setId(new PlainSet<>(setDateType, Arrays.asList(new Date(2))));
		assertOperation(e, "[...x.id][0].valueOf()", 2);

		// Map
		MapType mapStringDateType = typeReflection.getMapType(EssentialTypes.TYPE_STRING, EssentialTypes.TYPE_DATE);
		e.setId(new PlainMap<>(mapStringDateType, asMap("hey", new Date(2))));
		assertOperation(e, "[...x.id.keys()][0]", "hey");
		assertOperation(e, "[...x.id.values()][0].valueOf()", 2);
	}

	private native void assertIdIsNullInJs(GenericEntity entity) /*-{
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(entity.id == null, "id is null");
	}-*/;

	private native void setIdAsNullInJs(GenericEntity entity) /*-{
		entity.id = null;
	}-*/;

	private native void assertIdIsJsNumber(GenericEntity entity) /*-{
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(typeof(entity.id) == 'number', "typeof id == 'number'");
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(entity.id == 1, "id == value");
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(entity.id.type == null, "id.type is null");
	}-*/;

	private native void assertIdIsJsNumber(GenericEntity entity, String type) /*-{
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(entity.id instanceof Number, "id instanceof Number");
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(entity.id == 1, "id == value");
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(entity.id.type() == type, "id.type() == '" + type + "'");
	}-*/;

	private native void setIdAsNumberInJs(GenericEntity entity, String constructor) /*-{
		entity.id = eval(constructor); 
	}-*/;

	private native void assertIdIsBigInt(GenericEntity entity) /*-{
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(typeof entity.id == 'bigint', "id is BigInt");
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(entity.id == BigInt(1), "id == value");
	}-*/;

	private native void setIdAsBigInt(GenericEntity entity) /*-{
		entity.id = BigInt(1); 
	}-*/;

	private native void assertIdIsJsDate(GenericEntity entity) /*-{
	 	this.@Props_CompileTimeEntity_Test::assertTrue(*)(@Props_CompileTimeEntity_Test::isJsDate(*)(entity.id), "id is Date");
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(entity.id.valueOf() == 1, "id.valueOf() == 1");
	}-*/;

	//
	// Interestingly here [o instanceof Date] would be false, but [o instanceof globalThis.parent.Date] would be true
	private static native boolean isJsDate(JavaScriptObject o) /*-{
		return Object.prototype.toString.call(o) === '[object Date]';
	}-*/;

	private native void setIdAsJsDate(GenericEntity entity) /*-{
		entity.id = new Date(1); 
	}-*/;

	private native void assertIdIsArrayish(PropsEntity e) /*-{
	 	this.@Props_CompileTimeEntity_Test::assertTrue(*)(@Props_CompileTimeEntity_Test::isJsDate(*)(entity.id), "id is Date");
		this.@Props_CompileTimeEntity_Test::assertTrue(*)(entity.id.valueOf() == 1, "id.valueOf() == 1");
	}-*/;

	private void testSettingNativeJavaCollections() {
		logTesting("Set native Java collection");

		PropsEntity e = PropsEntity.T.create();

		// String[]
		setStringArrayInJs(e, "listString");
		setStringArrayInJs(e, "id");
		assertEqual(e.getListString(), Arrays.asList("a"), "listString");
		assertEqual(e.getId(), Arrays.asList("a"), "listString");
		assertFirstValueInJs(e, "listString", "a");
		assertFirstValueInJs(e, "id", "a");
		assertType("listString", e.getListString(), PlainList.class);
		assertType("id", e.getId(), PlainList.class);

		// Date[]
		setDateArrayInJs(e, "listDate");
		setDateArrayInJs(e, "id");
		assertEqual(first(e.getListDate()), new Date(5), "listDate");
		assertEqual(first((List<?>) e.getId()), new Date(5), "id");
		assertType("listDate", e.getListDate(), PlainList.class);
		assertType("id", e.getId(), PlainList.class);

		// Set<String>
		setStringSetInJs(e, "setString");
		setStringSetInJs(e, "id");
		assertEqual(e.getSetString(), asSet("a"), "setString");
		assertEqual(e.getId(), asSet("a"), "id");
		assertFirstValueInJs(e, "listString", "a");
		assertFirstValueInJs(e, "id", "a");
		assertType("setString", e.getSetString(), PlainSet.class);
		assertType("id", e.getId(), PlainSet.class);

		// Set<Date>
		setDateSetInJs(e, "setDate");
		setDateSetInJs(e, "id");
		assertEqual(first(e.getSetDate()), new Date(5), "setDate");
		assertEqual(first((Set<?>) e.getId()), new Date(5), "id");
		assertType("setDate", e.getSetDate(), PlainSet.class);
		assertType("id", e.getId(), PlainSet.class);

		// Map<Integer, String>
		setMapIntegerStringInJs(e, "mapIntegerString");
		setMapIntegerStringInJs(e, "id");
		assertEqual(firstKey(e.getMapIntegerString()), 1, "mapIntegerString.key");
		assertEqual(firstValue(e.getMapIntegerString()), "one", "mapIntegerString.value");
		assertEqual(firstKey(e.getId()), 1, "id.key");
		assertEqual(firstValue(e.getId()), "one", "id.value");
		assertFirstKeyInJs(e, "mapIntegerString", 1);
		assertFirstValueInJs(e, "mapIntegerString", "one");
		assertFirstKeyInJs(e, "id", 1);
		assertFirstValueInJs(e, "id", "one");
		assertType("mapIntegerString", e.getMapIntegerString(), PlainMap.class);
		assertType("id", e.getId(), PlainMap.class);

		// Map<Float, Date>
		setMapFloatDateInJs(e, "mapFloatDate");
		setMapFloatDateInJs(e, "id");
		assertEqual(firstKey(e.getMapFloatDate()), 11f, "mapFloatDate.key");
		assertEqual(firstValue(e.getMapFloatDate()), new Date(12), "mapFloatDate.value");
		assertEqual(firstKey(e.getId()), 11f, "id.key");
		assertEqual(firstValue(e.getId()), new Date(12), "id.value");
		assertType("mapFloatDate", e.getMapFloatDate(), PlainMap.class);
		assertType("id", e.getId(), PlainMap.class);
	}

	private Object firstKey(Object map) {
		return first(((Map<?, ?>) map).keySet());
	}

	private Object firstValue(Object map) {
		return first(((Map<?, ?>) map).values());
	}

	private native void setStringArrayInJs(PropsEntity e, String prop) /*-{
		e[prop] = ["a"];
	}-*/;

	private native void setDateArrayInJs(PropsEntity e, String prop) /*-{
		e[prop] = [new Date(5)];
	}-*/;

	private native void setStringSetInJs(PropsEntity e, String prop) /*-{
		e[prop] = new Set(["a"]);
	}-*/;

	private native void setDateSetInJs(PropsEntity e, String prop) /*-{
		e[prop] = new Set([new Date(5)]);
	}-*/;

	private native void setMapIntegerStringInJs(PropsEntity e, String prop) /*-{
		e[prop] = new Map([[1, "one"]]);
	}-*/;

	private native void setMapFloatDateInJs(PropsEntity e, String prop) /*-{
		e[prop] = new Map([[new $hcjs.T.Float(11), new Date(12)]]);
	}-*/;

	private native void assertFirstKeyInJs(PropsEntity e, String prop, Object expected) /*-{
		this.assertEqual(e[prop].keys().next().value, expected, "firstKey(e." + prop + ") == " + expected);
	}-*/;
	
	private native void assertFirstValueInJs(PropsEntity e, String prop, Object expected) /*-{
		this.assertEqual(e[prop].values().next().value, expected, "firstValue(e." + prop + ") == " + expected);
	}-*/;

	private void assertType(String desc, Object o, Class<?> clazz) {
		if (o.getClass() != clazz)
			logError("Property: [" + desc + "] " + " has wrong type. " + //
					"Expected: " + clazz.getSimpleName() + ", actual: " + o.getClass().getSimpleName());
	}

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

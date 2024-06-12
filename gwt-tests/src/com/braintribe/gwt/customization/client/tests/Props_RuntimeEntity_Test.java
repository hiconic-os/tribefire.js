package com.braintribe.gwt.customization.client.tests;

import java.util.Date;

import com.braintribe.gwt.customization.client.tests.model.simple.PropsEntity;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.utils.lcd.CommonTools;

/**
 * @see Props_CompileTimeEntity_Test
 * 
 * @author peter.gazdik
 */
public class Props_RuntimeEntity_Test extends AbstractGmGwtTest {

	@Override
	protected void tryRun() throws GmfException {
		GmMetaModel model = generateModel("test.gwt:simple-props-model", PropsEntity.T);
		makeSignaturesDynamic(model);
		ensureModelTypes(model);
		EntityType<?> dynamicEt = getDynamicCounterpart(PropsEntity.T);

		GenericEntity e = dynamicEt.create();
		fillNativeJs(e);
		assertNativeJs(e);
		assertJava(e);

		testGetSetNativeJsViaReflection(e);
	}

	// @UnsafeNativeLong
	private native void fillNativeJs(GenericEntity e) /*-{
		e.primitiveBoolean = true;
		e.booleanWrapper = true;
		e.primitiveInteger = 23;
		e.integerWrapper = 23;
		e.primitiveLong = BigInt(123456789);
		e.longWrapper = BigInt(123456789);
		e.primitiveFloat = 42;
		e.floatWrapper = 42;
		e.primitiveDouble = 420;
		e.doubleWrapper = 420;
		e.string = "unnamed string";
		e.date = new Date(123456789);

		e.listObject.push("hello");
		e.listString.push("hello");
		e.listDate.push(new Date(987654321));
		
		e.setString.add("hello");
		e.setDate.add(new Date(987654321));
		
		e.mapIntegerString.set(123, "hello");
		e.mapFloatDate.set(42, new Date(987654321));
	}-*/;

	private native void assertNativeJs(GenericEntity e) /*-{
		this.assertEqual(e.primitiveBoolean, true, "primitiveBoolean");
		this.assertEqual(e.booleanWrapper, true, "booleanWrapper");
		this.assertEqual(e.primitiveInteger, 23, "primitiveInteger");
		this.assertEqual(e.integerWrapper, 23, "integerWrapper");
		this.assertEqual(e.primitiveLong, BigInt(123456789), "primitiveLong");
		this.assertEqual(e.longWrapper, BigInt(123456789), "longWrapper");
		this.assertEqual(e.primitiveFloat, 42, "primitiveFloat");
		this.assertEqual(e.floatWrapper, 42, "floatWrapper");
		this.assertEqual(e.primitiveDouble, 420, "primitiveDouble");
		this.assertEqual(e.doubleWrapper, 420, "doubleWrapper");
		this.assertEqual(e.string, "unnamed string", "string");
		this.assertEqual(e.date.valueOf(), 123456789, "date.valueOf()");

		this.assertEqual(e.primitiveInteger.type, null, "primitiveInteger.type");
		this.assertEqual(e.integerWrapper.type, null, "integerWrapper.type");
		this.assertEqual(e.primitiveFloat.type(), 'f', "primitiveInteger.type()");
		this.assertEqual(e.floatWrapper.type(), 'f', "floatWrapper.type()");
		this.assertEqual(e.primitiveDouble.type(), 'd', "primitiveDouble.type()");
		this.assertEqual(e.doubleWrapper.type(), 'd', "doubleWrapper.type");

		this.assertEqual(e.listObject.at(0), 'hello', "listObject.at(0)");
		this.assertEqual(e.listString.at(0), 'hello', "listString.at(0)");
		this.assertEqual(e.listDate.at(0).valueOf(), 987654321, "listDate.at(0).valueOf()");

		this.assertOperation(e, '[...x.setString][0]', "hello");
		this.assertOperation(e, '[...x.setDate][0].valueOf()', 987654321);

		this.assertOperation(e, '[...x.mapIntegerString.keys()][0]', 123);
		this.assertOperation(e, '[...x.mapIntegerString.values()][0]', "hello");

		this.assertOperation(e, '[...x.mapFloatDate.keys()][0]', 42);
		this.assertOperation(e, '[...x.mapFloatDate.values()][0].valueOf()', 987654321);	
	}-*/;

	private native void testGetSetNativeJsViaReflection(GenericEntity e) /*-{
		var et = e.EntityType();
		var p;

		p = et.getProperty("primitiveBoolean");
		this.assertEqual(p.get(e), true, "prop.get: primitiveBoolean");
		p.set(e, false);
		this.assertEqual(p.get(e), false, "prop.set: primitiveBoolean");

		p = et.getProperty("booleanWrapper");
		this.assertEqual(p.get(e), true, "prop.get: booleanWrapper");
		p.set(e, false);
		this.assertEqual(p.get(e), false, "prop.set: booleanWrapper");

		// integer

		p = et.getProperty("primitiveInteger");
		this.assertEqual(p.get(e), 23, "prop.get: primitiveInteger");
		p.set(e, 24);
		this.assertEqual(p.get(e), 24, "prop.set: primitiveInteger");

		p = et.getProperty("integerWrapper");
		this.assertEqual(p.get(e), 23, "prop.get: integerWrapper");
		p.set(e, 24);
		this.assertEqual(p.get(e), 24, "prop.set: integerWrapper");

		// long

		p = et.getProperty("primitiveLong");
		this.assertEqual(p.get(e), BigInt(123456789), "prop.get: primitiveLong");
		p.set(e, BigInt(987654321));
		this.assertEqual(p.get(e), BigInt(987654321), "prop.set: primitiveLong");

		p = et.getProperty("longWrapper");
		this.assertEqual(p.get(e), BigInt(123456789), "prop.get: longWrapper");
		p.set(e, BigInt(987654321));
		this.assertEqual(p.get(e), BigInt(987654321), "prop.set: longWrapper");

		// float

		p = et.getProperty("primitiveFloat");
		this.assertEqual(p.get(e), 42, "prop.get: primitiveFloat");
		p.set(e, 43);
		this.assertEqual(p.get(e), 43, "prop.set: primitiveFloat");

		p = et.getProperty("floatWrapper");
		this.assertEqual(p.get(e), 42, "prop.get: floatWrapper");
		p.set(e, 43);
		this.assertEqual(p.get(e), 43, "prop.set: floatWrapper");

		// double

		p = et.getProperty("primitiveDouble");
		this.assertEqual(p.get(e), 420, "prop.get: primitiveDouble");
		p.set(e, 421);
		this.assertEqual(p.get(e), 421, "prop.set: primitiveDouble");

		p = et.getProperty("doubleWrapper");
		this.assertEqual(p.get(e), 420, "prop.get: doubleWrapper");
		p.set(e, 421);
		this.assertEqual(p.get(e), 421, "prop.set: doubleWrapper");

		//date

		p = et.getProperty("date");
		this.assertEqual(p.get(e).valueOf(), 123456789, "prop.get: date.valueOf()");
		p.set(e, new Date(987654321));
		this.assertEqual(p.get(e).valueOf(), 987654321, "prop.set: date.valueOf()");
	}-*/;

	private void assertJava(GenericEntity e) {
		assertEqualWithReflectionGet(e, "primitiveBoolean", true);
		assertEqualWithReflectionGet(e, "booleanWrapper", true);
		assertEqualWithReflectionGet(e, "primitiveInteger", 23);
		assertEqualWithReflectionGet(e, "integerWrapper", 23);
		assertEqualWithReflectionGet(e, "primitiveLong", 123456789L);
		assertEqualWithReflectionGet(e, "longWrapper", 123456789L);
		assertEqualWithReflectionGet(e, "primitiveFloat", 42f);
		assertEqualWithReflectionGet(e, "floatWrapper", 42f);
		assertEqualWithReflectionGet(e, "primitiveDouble", 420d);
		assertEqualWithReflectionGet(e, "doubleWrapper", 420d);
		assertEqualWithReflectionGet(e, "string", "unnamed string");
		assertEqualWithReflectionGet(e, "date", new Date(123456789));
	}

	private void assertEqualWithReflectionGet(GenericEntity e, String propertyName, Object expected) {
		Property p = e.entityType().getProperty(propertyName);
		Object actual = p.get(e);
		assertEqual(actual, expected, propertyName);
	}

	private void assertEqual(Object actual, Object expected, String propertyName) {
		if (CommonTools.equalsOrBothNull(expected, actual))
			log("    property '" + propertyName + "' [OK]");
		else
			logError("Property: [" + propertyName + "] " + " has wrong value. Expected: " + expected + ", actual: " + actual);
	}

}

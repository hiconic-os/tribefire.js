package com.braintribe.gwt.customization.client.tests;

import com.braintribe.gwt.customization.client.tests.model.simple.PropsEntity;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.TypeCode;
import com.braintribe.model.meta.GmMetaModel;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author peter.gazdik
 */
public class VirtualPropertyTest extends AbstractGmGwtTest {

	private EntityType<?> DYNAMIC_ET;

	@Override
	protected void tryRun() throws GmfException {
		deployDynamicModel();

		log("COMPILE-TIME");
		test_PropertyAccess(PropsEntity.T);

		log("RUNTIME");
		test_PropertyAccess(DYNAMIC_ET);
	}

	private void test_PropertyAccess(EntityType<?> et) {
		log("testing virtual properties for: " + et.getTypeSignature());

		GenericEntity instance = et.create();

		for (Property p : et.getProperties()) {
			if (p.getDeclaringType() == GenericEntity.T)
				continue;

			if (p.getType().getTypeCode() == TypeCode.longType) {
				checkAssigningNativeJsNumberToLongThrowsException(instance, p);
				continue;
			}

			checkNonNullValue(instance, p);
			checkNullValueIfNullable(instance, p);
		}

	}

	// We cannot convert long/Long to Number (not enough precision), so we also do not allow writing a number to long/Long property (false friend).
	private void checkAssigningNativeJsNumberToLongThrowsException(GenericEntity instance, Property p) {
		JavaScriptObject jsNumber = getNativeJsValue(p);
		try {
			setPropertyValue(instance, p.getName(), jsNumber);
			logError("    " + p.getName() + " Exception should have been thrown for value: " + valueAndType(jsNumber));

		} catch (Exception e) {
			log("    " + p.getName() + " Exception was correctly thrown for value: " + valueAndType(jsNumber));
		}
	}

	private void checkNonNullValue(GenericEntity instance, Property p) {
		JavaScriptObject jsValue = getNativeJsValue(p);
		setPropertyValue(instance, p.getName(), jsValue);
		JavaScriptObject walue = getPropertyValue(instance, p.getName());

		checkEqual(p, jsValue, walue);
	}

	private void checkNullValueIfNullable(GenericEntity instance, Property p) {
		if (!p.isNullable())
			return;

		setPropertyValue(instance, p.getName(), null);
		try {
			Object directHopefullyNull = p.getDirectUnsafe(instance);
			checkNull(p, directHopefullyNull, "direct");

			Object hopefullyNull = getPropertyValue(instance, p.getName());
			checkNull(p, hopefullyNull, "virtual");

		} catch (Exception e) {
			logError("    " + p.getName() + " [null] could not be read. Exception: " + e);
		}

	}

	private JavaScriptObject getNativeJsValue(Property p) {
		switch (p.getType().getTypeCode()) {
			case booleanType:
				return jsBoolean();
			case doubleType:
				return jsDouble();
			case floatType:
				return jsFloat();
			case integerType:
				return jsInteger();
			case longType:
				return jsLong();
			case stringType:
				return jsString();

			case dateType:
			case decimalType:
			case objectType:
			case enumType:
			case listType:
			case mapType:
			case setType:
			case entityType:
			default:
				throw new IllegalStateException("Not expected property of type: " + p.getType() + ". Property: " + p);

		}
	}

	private static native JavaScriptObject jsBoolean() /*-{ return true; }-*/;
	private static native JavaScriptObject jsDouble() /*-{ return 666; }-*/;
	private static native JavaScriptObject jsFloat() /*-{ return 777.77; }-*/;
	private static native JavaScriptObject jsInteger() /*-{ return 111; }-*/;
	private static native JavaScriptObject jsLong() /*-{ return 123456789; }-*/;
	private static native JavaScriptObject jsString() /*-{ return "Hello"; }-*/;

	private native void setPropertyValue(GenericEntity instance, String name, JavaScriptObject value) /*-{ instance[name] = value; }-*/;
	private native JavaScriptObject getPropertyValue(GenericEntity instance, String name) /*-{ return instance[name]; }-*/;

	// #################################################
	// ## . . . . . . . . . Asserts . . . . . . . . . ##
	// #################################################

	private void checkEqual(Property p, JavaScriptObject origin, JavaScriptObject read) {
		if (areEqual(origin, read))
			log("    " + p.getName() + ": '" + valueAndType(origin) + "' [OK]");
		else
			logError("    " + p.getName() + " Wrong value read. Wrote: " + valueAndType(origin) + ", Read: " + valueAndType(read));
	}

	private native boolean areEqual(JavaScriptObject a, JavaScriptObject b) /*-{
		return a === b;
	}-*/;

	private void checkNull(Property p, Object hopefullyNull, String desc) {
		if (hopefullyNull == null)
			log("    " + p.getName() + ": null (" + desc + ") [OK]");
		else
			logError("    " + p.getName() + " Null was not read (" + desc + "): " + valueAndType(hopefullyNull));
	}

	private String valueAndType(Object v) {
		return v + typeof(v);
	}

	private native String typeof(Object v) /*-{
		return "[" + typeof(v) + "]";
	}-*/;

	// #################################################
	// ## . . . . . . . Dynamic Model . . . . . . . . ##
	// #################################################

	private void deployDynamicModel() throws GmfException {
		GmMetaModel metaModel = generateModel("est.gwt:transient-property-test", PropsEntity.T);

		makeSignaturesDynamic(metaModel);
		ensureModelTypes(metaModel);

		loadDynamicEt();
	}

	private void loadDynamicEt() {
		String dynamicSignature = makeSignatureDynamic(PropsEntity.T.getTypeSignature());
		DYNAMIC_ET = GMF.getTypeReflection().getEntityType(dynamicSignature);
	}

}

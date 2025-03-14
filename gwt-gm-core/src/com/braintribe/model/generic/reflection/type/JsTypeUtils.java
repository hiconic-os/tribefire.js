package com.braintribe.model.generic.reflection.type;

import java.util.Date;

/**
 * @author peter.gazdik
 */
public class JsTypeUtils {

	/**
	 * This is more robust than e.g. "instanceof Date", as instanceof checks don't work across frames<br>
	 * <a href="https://stackoverflow.com/a/643827">See StackOverflow</a>.
	 */
	// @formatter:off
	public static boolean isJsDate(Object o) { return o != null && isJsType(o, "Date"); }
	public static boolean isJsNumber(Object o) { return o != null &&  isJsType(o, "Number"); }
	public static boolean isJsArray(Object o) { return o != null && isJsType(o, "Array"); }
	public static boolean isJsSet(Object o) { return o != null && isJsType(o, "Set"); }
	public static boolean isJsMap(Object o) { return o != null && isJsType(o, "Map"); }

	public static boolean isJsBigInt(Object o) { return o != null && isJsType(o, "BigInt"); }	
	// @formatter:on

	public static boolean isJsInteger(Object o) {
		return isJsNumber(o) && typeIsNull(o);
	}

	public static boolean isJsFloat(Object o) {
		return isJsNumber(o) && hasType(o, "f");
	}

	public static boolean isJsDouble(Object o) {
		return isJsNumber(o) && hasType(o, "d");
	}

	private static native boolean typeIsNull(Object o) /*-{
		return o.type == null;
	}-*/;

	private static native boolean hasType(Object o, String type) /*-{
		return o.type == null ? null : o.type() == type;
	}-*/;

	public static native boolean isJsType(Object o, String type) /*-{
		return Object.prototype.toString.call(o) === '[object '+type+']';
	}-*/;

	// #################################################
	// ## . . . . . . . . Conversions . . . . . . . . ##
	// #################################################

	public native static Object nonCollectionJsToObject(Object v) /*-{
		if (v.@Object::getClass())
			return v;

		var t = typeof v;
		if (t == 'string' || v instanceof String)
		 	return v;
		if (t == 'number')
		 	return @Integer::valueOf(I)(v);

		if (@JsTypeUtils::isJsNumber(*)(v)) {
			if (v.type == null)
		 		return @Integer::valueOf(I)(v);
		 	if (v.type() == 'f')
		 		return @Float::valueOf(F)(v.valueOf());
		 	if (v.type() == 'd')
		 		return v.valueOf();
		 	return @Integer::valueOf(I)(v);
		}

		if (t == 'bigint')
			return @Long::new(Ljava/lang/String;)(v.toString());
		if (@JsTypeUtils::isJsDate(*)(v))
			return @JsTypeUtils::jsToJDate(*)(v);

		return v;
	}-*/;

	public static native Date jsToJDate(Object jsdate) /*-{
		if (jsdate == null)
			return null;
		if (@JsTypeUtils::isJsDate(*)(jsdate))
			return @Date::fromJsDate(*)(jsdate);
		throw new Error("Value is not a JS Date: " + date);
	}-*/;

}

// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.gwt.genericmodel.client.itw;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.common.lcd.Pair;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Arrayish;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Mapish;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Setish;
import com.braintribe.gwt.genericmodel.client.reflect.TypePackage;
import com.braintribe.model.generic.collection.Collectionish;
import com.braintribe.model.generic.collection.JsWrappableCollection;
import com.braintribe.model.generic.reflection.GmtsEnhancedEntityStub;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.PropertyAccessInterceptor;
import com.braintribe.model.generic.reflection.TypeCode;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.core.client.UnsafeNativeLong;

import jsinterop.utils.Lambdas.JsUnaryFunction;

/**
 * Documented to force an import, thus using short name in JS code
 * 
 * @see GmtsEnhancedEntityStub
 * @see PropertyAccessInterceptor
 */
public class GenericAccessorMethods {

	// #################################################
	// ## . . . . . . . Getters/Setters . . . . . . . ##
	// #################################################

	/**
	 * The field that holds the value is of type object, so we need to handle primitive types.
	 * 
	 * <p>
	 * int/float - the primitive value is native JS number. Hence:
	 * <ul>
	 * <li>when calling the primitive getter and setter, we do the boxing/unboxing.
	 * <li>when using virtual property assignment, we also do boxing/unboxing, but we also allow <tt>null</tt>.
	 * </ul>
	 * 
	 * <p>
	 * double/Double - is compiled by GWT as number (in both cases), so there is nothing to do
	 *
	 * <p>
	 * long/Long - the primitive value is the same object as Long:
	 * <ul>
	 * <li>when calling the primitive getter and setter, we do the boxing/unboxing.
	 * <li>when use generic getter/setter, which throws an exception when we try to assign JS number.
	 * </ul>
	 */
	public static Pair<JavaScriptObject, JavaScriptObject> buildGetterSetterAccessors(GwtScriptProperty property) {
		if (!property.isNullable()) {
			switch (property.getType().getTypeCode()) {
				case integerType:
					return Pair.of(convGetter(property, C_UNBOX), buildIntBoxingSetter(property));
				case floatType:
					return Pair.of(convGetter(property, C_UNBOX), buildFloatBoxingSetter(property));
				case longType:
					return Pair.of(convGetter(property, C_UNBOX_LONG), buildLongBoxingSetter(property));
				default:
					break;
			}
		}

		return new Pair<>(noConvGetter(property), noConvSetter(property));
	}

	private native static JavaScriptObject noConvGetter(Property property) /*-{
		return function(){
			return this.@GmtsEnhancedEntityStub::pai.@PropertyAccessInterceptor::getProperty(*)(property, this, false);
		}
	}-*/;

	private native static JavaScriptObject noConvSetter(Property property) /*-{
		return function(v){
			this.@GmtsEnhancedEntityStub::pai.@PropertyAccessInterceptor::setProperty(*)(property, this, v, false);
		}
	}-*/;

	private native static JavaScriptObject buildIntBoxingSetter(Property property) /*-{
		return function(v){
			v = @Integer::valueOf(I)(v);
			this.@GmtsEnhancedEntityStub::pai.@PropertyAccessInterceptor::setProperty(*)(property, this, v, false);
		}
	}-*/;

	@SuppressWarnings("unusable-by-js")
	@UnsafeNativeLong
	private native static JavaScriptObject buildLongBoxingSetter(Property property) /*-{
		return function(v){
			v = @java.lang.Long::valueOf(J)(v);
			this.@GmtsEnhancedEntityStub::pai.@PropertyAccessInterceptor::setProperty(*)(property, this, v, false);
		}
	}-*/;

	private native static JavaScriptObject buildFloatBoxingSetter(Property property) /*-{
		return function(v){
			v = @Float::valueOf(F)(v);
			this.@GmtsEnhancedEntityStub::pai.@PropertyAccessInterceptor::setProperty(*)(property, this, v, false);
		}
	}-*/;

	// #################################################
	// ## . . . . . . Virtual Properties . . . . . . .##
	// #################################################

	/**
	 * Types can be null. It's only passed if the property might need a conversion, i.e. for collections/simple/object, but not entities/enums.
	 */
	public static Pair<JavaScriptObject, JavaScriptObject> buildJsConvertingAccessors( //
			Property property, JavaScriptObject getterFunction, JavaScriptObject setterFunction, //
			TypeCode collectionType, TypeCode keyType, TypeCode valueType) {

		Pair<JsUnaryFunction<?, ?>, JsUnaryFunction<?, ?>> converters = resolveConverters(property, true, collectionType, keyType, valueType);

		return Pair.of( //
				converters.first != null ? convGetter(property, converters.first) : getterFunction, //
				converters.second != null ? convSetter(property, converters.second) : setterFunction //
		);
	}

	private native static JavaScriptObject convGetter(Property property, JsUnaryFunction<?, ?> converter) /*-{
		return function(){
			var value = this.@GmtsEnhancedEntityStub::pai.@PropertyAccessInterceptor::getProperty(*)(property, this, false);
			return value == null ? null : converter(value);
		}
	}-*/;

	private native static JavaScriptObject convSetter(Property property, JsUnaryFunction<?, ?> converter) /*-{
		return function(v){
			v = v == null ? null : converter(v);
			this.@GmtsEnhancedEntityStub::pai.@PropertyAccessInterceptor::setProperty(*)(property, this, v, false);
		}
	}-*/;

	// #################################################
	// ## . . . . . . . Property get/set . . . . . . .##
	// #################################################

	/**
	 * Types can be null. It's only passed if the property might need a conversion, i.e. for collections/simple/object, but not entities/enums.
	 */
	public static Pair<JavaScriptObject, JavaScriptObject> buildJsConvertingPropertyAccessors( //
			Property property, TypeCode collectionType, TypeCode keyType, TypeCode valueType) {

		Pair<JsUnaryFunction<?, ?>, JsUnaryFunction<?, ?>> converters = resolveConverters(property, false, collectionType, keyType, valueType);

		return Pair.of( //
				convGet(property, converters.first), //
				convSet(property, converters.second) //
		);
	}

	private native static JavaScriptObject convGet(Property property, JsUnaryFunction<?, ?> converter) /*-{
		return function(e){
			var value = e.@GmtsEnhancedEntityStub::pai.@PropertyAccessInterceptor::getProperty(*)(property, e, false);
			return converter == null || value == null ? value : converter(value);
		}
	}-*/;

	private native static JavaScriptObject convSet(Property property, JsUnaryFunction<?, ?> converter) /*-{
		return function(e,v){
			v = converter == null || v == null ? v : converter(v);
			e.@GmtsEnhancedEntityStub::pai.@PropertyAccessInterceptor::setProperty(*)(property, e, v, false);
		}
	}-*/;

	// #################################################
	// ## . . . . . . . . . Helpers . . . . . . . . . ##
	// #################################################

	private static Pair<JsUnaryFunction<?, ?>, JsUnaryFunction<?, ?>> resolveConverters(//
			Property property, boolean hasBoxUnboxFallback, //
			TypeCode collectionType, TypeCode keyType, TypeCode valueType) {

		if (collectionType != null) {
			switch (collectionType) {
				case listType:
					return Pair.of(jToJsListConverter(valueType), C_UNWRAP_COLLECTION);
				case setType:
					return Pair.of(jToJsSetConverter(valueType), C_UNWRAP_COLLECTION);
				case mapType:
					return Pair.of(jToJsMapConverter(keyType, valueType), C_UNWRAP_COLLECTION);
				default:
					break;
			}
		}

		if (valueType != null) {
			if (property.isNullable()) {
				switch (valueType) {
					case integerType:
						return Pair.of(C_UNBOX, C_BOX_INT);
					case floatType:
						return Pair.of(C_J_TO_JS_FLOAT, C_BOX_FLOAT);
					default:
						break;
				}
			}

			switch (valueType) {
				case objectType:
					return Pair.of(C_OBJECT_TO_JS, C_JS_TO_OBJECT);
				case integerType:
					return hasBoxUnboxFallback ? Pair.of(null, null) : Pair.of(C_UNBOX, C_BOX_INT);
				case floatType:
					// primitive ->
					// getter uses PAI, we need to unbox and mark type;
					// setter accepts primitive float, which is number
					return Pair.of(C_J_TO_JS_FLOAT, hasBoxUnboxFallback ? null : C_BOX_FLOAT);
				case doubleType:
					// both primitive and wrapper return number, so we just need to mark it and setter works
					return Pair.of(C_J_TO_JS_DOUBLE, null);
				case longType:
					// both primitive and wrapper
					return Pair.of(C_LONG_TO_BIG_INT, C_BIG_INT_TO_LONG);
				case dateType:
					return Pair.of(C_J_TO_JS_DATE, C_JS_TO_J_DATE);
				default:
					break;
			}
		}

		return Pair.of(null, null);
	}

	private static JsUnaryFunction<?, Object> jToJsListConverter(TypeCode valueType) {
		JsUnaryFunction<Object, Object> javaToJs = nonCollection_JToJs_Converter(valueType);
		JsUnaryFunction<Object, Object> jsToJava = nonCollection_JsToJ_Converter(valueType);

		return (List<Object> list) -> {
			JsWrappableCollection wl = (JsWrappableCollection) list;
			Collectionish w = wl.getCollectionWrapper();
			if (w == null) {
				w = new Arrayish<Object>(list, javaToJs, jsToJava);
				wl.setCollectionWrapper(w);
			}

			return w;
		};
	}

	private static JsUnaryFunction<?, Object> jToJsSetConverter(TypeCode valueType) {
		JsUnaryFunction<Object, Object> javaToJs = nonCollection_JToJs_Converter(valueType);
		JsUnaryFunction<Object, Object> jsToJava = nonCollection_JsToJ_Converter(valueType);

		return (Set<Object> set) -> {
			JsWrappableCollection wl = (JsWrappableCollection) set;
			Collectionish w = wl.getCollectionWrapper();
			if (w == null) {
				w = new Setish<Object>(set, javaToJs, jsToJava);
				wl.setCollectionWrapper(w);
			}

			return w;
		};
	}

	private static JsUnaryFunction<?, Object> jToJsMapConverter(TypeCode keyType, TypeCode valueType) {
		JsUnaryFunction<Object, Object> keyJavaToJs = nonCollection_JToJs_Converter(keyType);
		JsUnaryFunction<Object, Object> keyJsToJava = nonCollection_JsToJ_Converter(keyType);

		JsUnaryFunction<Object, Object> javaToJs = nonCollection_JToJs_Converter(valueType);
		JsUnaryFunction<Object, Object> jsToJava = nonCollection_JsToJ_Converter(valueType);

		return (Map<Object, Object> map) -> {
			JsWrappableCollection wl = (JsWrappableCollection) map;
			Collectionish w = wl.getCollectionWrapper();
			if (w == null) {
				w = new Mapish<>(map, //
						javaToJs, jsToJava, //
						keyJavaToJs, keyJsToJava);
				wl.setCollectionWrapper(w);
			}

			return w;
		};
	}

	private static JsUnaryFunction<Object, Object> nonCollection_JToJs_Converter(TypeCode type) {
		if (type == null)
			return null;

		switch (type) {
			case objectType:
				return C_NON_COLLECTION_OBJECT_TO_JS;
			case integerType:
				return C_UNBOX;
			case longType:
				return C_LONG_TO_BIG_INT;
			case floatType:
				return C_J_TO_JS_FLOAT;
			case doubleType:
				return C_J_TO_JS_DOUBLE;
			case dateType:
				return C_J_TO_JS_DATE;
			default:
				return null;
		}
	}

	private static JsUnaryFunction<Object, Object> nonCollection_JsToJ_Converter(TypeCode type) {
		if (type == null)
			return null;

		switch (type) {
			case objectType:
				return C_NON_COLLECTION_JS_TO_OBJECT;
			case integerType:
				return C_BOX_INT;
			case longType:
				return C_BIG_INT_TO_LONG;
			case floatType:
				return C_BOX_FLOAT;
			case doubleType:
				return null; // should be OK
			case dateType:
				return C_JS_TO_J_DATE;
			default:
				return null;
		}
	}

	// JS TO J
	private static final JsUnaryFunction<Object, Object> C_JS_TO_OBJECT = C::jsToObject;
	private static final JsUnaryFunction<Object, Object> C_BOX_INT = C::boxInt;
	private static final JsUnaryFunction<Object, Object> C_BIG_INT_TO_LONG = C::bigIntToLong;
	private static final JsUnaryFunction<Object, Object> C_BOX_FLOAT = C::boxFloat;
	private static final JsUnaryFunction<Object, Object> C_JS_TO_J_DATE = C::jsToJDate;
	private static final JsUnaryFunction<Object, Object> C_UNWRAP_COLLECTION = C::unwrapCollection;
	private static final JsUnaryFunction<Object, Object> C_NON_COLLECTION_JS_TO_OBJECT = C::nonCollectionJsToObject;

	// J TO JS
	private static final JsUnaryFunction<Object, Object> C_OBJECT_TO_JS = C::objectToJs;
	private static final JsUnaryFunction<Object, Object> C_J_TO_JS_DATE = C::jToJsDate;
	private static final JsUnaryFunction<Object, Object> C_J_TO_JS_DOUBLE = C::jToJsDouble;
	private static final JsUnaryFunction<Object, Object> C_J_TO_JS_FLOAT = C::jToJsFloat;
	private static final JsUnaryFunction<Object, Object> C_LONG_TO_BIG_INT = C::longToBigInt;
	private static final JsUnaryFunction<Object, Object> C_UNBOX = C::unbox;
	private static final JsUnaryFunction<Object, Object> C_UNBOX_LONG = C::unboxLong;
	private static final JsUnaryFunction<Object, Object> C_NON_COLLECTION_OBJECT_TO_JS = C::nonCollectionObjectToJs;

	// converters
	private static class C {

		public native static Double unbox(Object boxer) /*-{
			return boxer.valueOf();
		}-*/;

		public static Object unwrapCollection(Object c) {
			return ((Collectionish) c).wrappedCollection();
		}

		@SuppressWarnings("unusable-by-js")
		@UnsafeNativeLong
		public native static JavaScriptObject unboxLong(Object boxer) /*-{
			return boxer.@Long::longValue()();
		}-*/;

		public native static Object boxInt(Object v) /*-{
			return @Integer::valueOf(I)(v);
		}-*/;

		public native static Object boxFloat(Object v) /*-{
			return @Float::valueOf(F)(v.valueOf());
		}-*/;

		public native static JavaScriptObject longToBigInt(Object v) /*-{
			return globalThis.BigInt(v.@Long::toString()());
		}-*/;

		public native static Object bigIntToLong(Object v) /*-{
			return @Long::new(Ljava/lang/String;)(v.toString());
		}-*/;

		/** Uses {@link TypePackage} */
		public native static JavaScriptObject jToJsFloat(Object v) /*-{
			return new (@TypePackage::getRoot()()).Float(v.valueOf());
		}-*/;

		/** Uses {@link TypePackage} */
		public native static JavaScriptObject jToJsDouble(Object v) /*-{
			return new (@TypePackage::getRoot()()).Double(v.valueOf());
		}-*/;

		public static Object objectToJs(Object v) {
			if (!(v instanceof JsWrappableCollection))
				return nonCollectionObjectToJs(v);

			JsWrappableCollection wc = (JsWrappableCollection) v;
			Collectionish w = wc.getCollectionWrapper();
			if (w != null)
				return w;

			JsUnaryFunction<Object, Object> javaToJs = C_NON_COLLECTION_OBJECT_TO_JS;
			JsUnaryFunction<Object, Object> jsToJava = C_NON_COLLECTION_JS_TO_OBJECT;

			if (wc instanceof List)
				w = new Arrayish<>((List<Object>) wc, javaToJs, jsToJava);
			else if (wc instanceof Set)
				w = new Setish<>((Set<Object>) wc, javaToJs, jsToJava);
			else
				w = new Mapish<>((Map<Object, Object>) wc, javaToJs, jsToJava, javaToJs, jsToJava);

			wc.setCollectionWrapper(w);
			return w;
		}

		private native static JavaScriptObject nonCollectionObjectToJs(Object v) /*-{
		 	var clazz = v.@Object::getClass()();
		 	if(clazz == @String::class)
		 		return v;
		 	if(clazz == @Integer::class)
		 		return v.valueOf(0);
		 	if (clazz == @Long::class)
		 		return globalThis.BigInt(v.@Long::toString()());
		 	if (clazz == @Float::class)
		 		return new (@TypePackage::getRoot()()).Float(v.valueOf());
		 	if (typeof v == 'number')
		 		return new (@TypePackage::getRoot()()).Double(v);
		 	if (clazz == @java.util.Date::class)
		 	 	return @C::jToJsDate(*)(v);

		 	return v;  
		}-*/;

		private static Object jsToObject(Object v) {
			if (v instanceof Collectionish)
				return ((Collectionish) v).wrappedCollection();
			else
				return nonCollectionJsToObject(v);
		}

		private native static Object nonCollectionJsToObject(Object v) /*-{
			if (v.@Object::getClass())
				return v;
		
			var t = typeof v;
			 if (t == 'string' || v instanceof String)
			 	return v;
			if (t == 'number')
			 	return @Integer::valueOf(I)(v);
	
			if (@C::isJsNumber(*)(v)) {
				if (v.type == null)
			 		return @Integer::valueOf(I)(v);
			 	if (v.type() == 'f')
			 		return @Float::valueOf(F)(v.valueOf());
			 	if (v.type() == 'd')
			 		return v.valueOf();
			 	return @Integer::valueOf(I)(v);
			}
	
			if (typeof v == 'bigint')
				return @Long::new(Ljava/lang/String;)(v.toString());
			if (@C::isJsDate(*)(v))
				return @C::jsToJDate(*)(v);
	
			return v;
		}-*/;

		private static native Date jsToJDate(Object jsdate) /*-{
			if (jsdate == null)
				return null;
			if (@C::isJsDate(*)(jsdate))
				return @Date::fromJsDate(*)(jsdate);
			throw new Error("Value is not a JS Date: " + date);
		}-*/;

		private static native JsDate jToJsDate(Object date) /*-{
			return date == null ? null : date.@Date::toJsDate()();
		}-*/;

		/**
		 * This is more robust than "instanceof Date", as instanceof checks don't work across frames<br>
		 * <a href="https://stackoverflow.com/a/643827">See StackOverflow</a>.
		 */
		private static native boolean isJsDate(JavaScriptObject o) /*-{
			return Object.prototype.toString.call(o) === '[object Date]';
		}-*/;

		private static native boolean isJsNumber(JavaScriptObject o) /*-{
			return Object.prototype.toString.call(o) === '[object Number]';
		}-*/;
	}

}

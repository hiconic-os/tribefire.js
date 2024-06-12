package com.braintribe.gwt.genericmodel.client.jsinterop.collectionish;

import com.braintribe.model.generic.collection.Collectionish;
import com.braintribe.model.generic.collection.JsWrappableCollection;

import jsinterop.utils.Lambdas.JsUnaryFunction;

/**
 * Base for JS collection wrappers, which are implementations of the native JS Array/Set/Map interfaces.
 * <p>
 * They are called "-ish", because arrays don't support the square bracket index access (a[123]), and because they might not support all the methods,
 * as these are being added to JS potentially every year.
 * 
 * @see JsWrappableCollection
 */
public abstract class AbstractCollectionish<T> implements Collectionish {

	protected final JsUnaryFunction<Object, T> javaToJs;
	protected final JsUnaryFunction<T, Object> jsToJava;

	public AbstractCollectionish(JsUnaryFunction<Object, T> javaToJs, JsUnaryFunction<T, Object> jsToJava) {
		this.javaToJs = javaToJs;
		this.jsToJava = jsToJava;
	}

	protected T[] toJsArrayIfNeeded(Object[] jArray) {
		return jsToJava == null || jArray.length == 0 ? (T[]) jArray : convertToJsArray(jArray);
	}

	protected native T[] convertToJsArray(Object[] jArray) /*-{
		return jArray.map(this.@AbstractCollectionish::javaToJs); 
	}-*/;

	protected Object[] toJArrayIfNeeded(T[] jsArray) {
		return jsToJava == null || jsArray.length == 0 ? (Object[]) jsArray : convertToJArray(jsArray);
	}

	protected native Object[] convertToJArray(T[] jsArray) /*-{
		return jsArray.map(this.@AbstractCollectionish::jsToJava); 
	}-*/;

	protected T toJsIfNeeded(Object o) {
		return javaToJs == null || o == null ? (T) o : javaToJs.call(o);
	}

	protected Object toJavaIfNeeded(T t) {
		return jsToJava == null || t == null ? t : jsToJava.call(t);
	}

	protected native <E> E undefined() /*-{
		return undefined;
	}-*/;

}

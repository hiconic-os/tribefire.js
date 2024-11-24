package com.braintribe.gwt.genericmodel.client.jsinterop.collectionish;

import com.braintribe.gwt.browserfeatures.client.JsCollections;
import com.braintribe.gwt.genericmodel.client.reflect.TypePackage;

import jsinterop.annotations.JsType;
import jsinterop.annotations.custom.TsIgnore;

/**
 * This only exists so that it's used as a return type of a static factory method that creates a {@link Arrayish} instance.
 * <p>
 * It's important that this method is reflected in its .d.ts as if it was returning T.Array, but Arrayish itself cannot be mapped there, cause GWT
 * would also project the class under that namespace.
 * <p>
 * This we don't want, as we want to use a trick (implemented in JS) to make sure {@code new T.Map()} is mapped to that static method.
 * <p>
 * So with this interface, we make TS writer write the correct method return type without any JS code being produced.
 */
@SuppressWarnings("unused")
@TsIgnore
@JsType(name = "Array", namespace = TypePackage.GM_TYPE_NAMESPACE)
public interface ArrayIface<E> {
	// marker interface for JsInterop
}

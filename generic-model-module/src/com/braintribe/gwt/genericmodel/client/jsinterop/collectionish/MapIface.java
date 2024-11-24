package com.braintribe.gwt.genericmodel.client.jsinterop.collectionish;

import com.braintribe.gwt.genericmodel.client.reflect.TypePackage;

import jsinterop.annotations.JsType;
import jsinterop.annotations.custom.TsIgnore;

/**
 * @see ArrayIface 
 */
@SuppressWarnings("unused")
@TsIgnore
@JsType(name = "Map", namespace = TypePackage.GM_TYPE_NAMESPACE)
public interface MapIface<K, V> {
	// marker interface for JsInterop
}

package com.braintribe.model.generic.collection;

import jsinterop.annotations.JsType;
import jsinterop.annotations.custom.TsIgnore;
import jsinterop.context.JsInteropNamespaces;

/**
 * @see ArrayIface 
 */
@SuppressWarnings("unused")
@TsIgnore
@JsType(name = "Map", namespace = JsInteropNamespaces.type)
public interface MapIface<K, V> {
	// marker interface for JsInterop
}

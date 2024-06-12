package com.braintribe.model.generic.reflection;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmCoreApiInteropNamespaces;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

@JsType(name = "Attribute", namespace = GmCoreApiInteropNamespaces.reflection)
public interface JsInteropAttribute {

	@JsMethod(name = "get")
	<T> T getJs(GenericEntity entity);

	@JsMethod(name = "set")
	void setJs(GenericEntity entity, Object value);

}

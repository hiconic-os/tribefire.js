package com.braintribe.model.generic.reflection;

import jsinterop.annotations.JsMethod;

/**
 * Similar to {@link GenericModelTypeJs}
 */
public interface PropertyJs extends Property {

	@JsMethod(name = "isEmptyValue")
	boolean isEmptyValueJs(Object value);

}

package com.braintribe.model.generic.reflection;

import com.braintribe.model.generic.GenericEntity;

import jsinterop.annotations.JsMethod;

/**
 * @see GenericModelTypeJs
 */
public interface EntityTypeJs<T extends GenericEntity> extends EntityType<T> {

	@JsMethod(name = "getProperty")
	default Property getPropertyJs(String name) {
		return getProperty(name);
	}

	@JsMethod(name = "findProperty")
	default Property findPropertyJs(String name) {
		return findProperty(name);
	}

	@JsMethod(name = "create")
	default T createJs() {
		return createRaw();
	}
	
	@JsMethod(name = "createRaw")
	default T createRawJs() {
		return createRaw();
	}
	
}

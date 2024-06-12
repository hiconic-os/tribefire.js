package com.braintribe.model.generic.collection;

/**
 * Interface implemented by Plain/Enhanced collections, so they can be 1 to 1 associated with a JS wrapper.
 * <p>
 * This JS collection wrapper is something that (mostly) fulfills the Array/Set/Map interfaces in JavaScript, to achieve the goal that our entities
 * interface with native JS objects, rather than gwt-compiled Java objects (e.g. a date property looks like it holds a JS Date).
 * <p>
 * See Arrayish, Setish and Mapish in generic-model-module.
 */
public interface JsWrappableCollection {

	Collectionish getCollectionWrapper();

	void setCollectionWrapper(Collectionish wrapper);

}

package com.braintribe.gwt.genericmodel.client.jsinterop;

import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Arrayish;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Mapish;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Setish;
import com.braintribe.gwt.genericmodel.client.reflect.TypePackage;
import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.google.gwt.core.client.JavaScriptObject;

import jsinterop.annotations.JsMethod;

public class HcJsInitializer {

	@JsMethod(namespace = GmCoreApiInteropNamespaces.gm)
	public static void initHcJs() {
		JavaScriptObject root = TypePackage.getRoot();
		makeCollectionishesIterable(root);
		createDoubleTypes(root);
	}

	/**
	 * @see Arrayish
	 * @see Setish
	 * @see Mapish
	 */
	private static native void makeCollectionishesIterable(JavaScriptObject typeNs) /*-{
		typeNs.Arrayish.prototype[Symbol.iterator] = typeNs.Arrayish.prototype.values;
		typeNs.Setish.prototype[Symbol.iterator] = typeNs.Setish.prototype.values;
		typeNs.Mapish.prototype[Symbol.iterator] = typeNs.Mapish.prototype.entries;
	}-*/;

	// The inner brackets around class definitions are mandatory!!!
	public static native void createDoubleTypes(JavaScriptObject typeNs) /*-{
		typeNs.Float =eval('(class Float  extends Number {type(){return "f"}})');
		typeNs.Double=eval('(class Double extends Number {type(){return "d"}})');
	}-*/;

	// Just in case, this is an alternative without eval:

	// $hcjs.T.Double = function Double(value) {
	// Object.defineProperty(this, 'value', {
	// value: value.valueOf(),
	// writable: false,
	// enumerable: true
	// });
	// };
	//
	// // Set up inheritance
	// $hcjs.T.Double.prototype = Object.create(Number.prototype);
	// $hcjs.T.Double.prototype.constructor = $hcjs.T.Double;
	// $hcjs.T.Double.prototype.valueOf = function() {
	// return this.value;
	// };
	// $hcjs.T.Double.prototype.toString = function() {
	// return this.value.toString();
	// };

}

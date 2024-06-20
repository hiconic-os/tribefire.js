package com.braintribe.gwt.genericmodel.client.jsinterop;

import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Arrayish;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Mapish;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Setish;
import com.braintribe.gwt.genericmodel.client.reflect.TypePackage;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

public class GmTypesInteropEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		makeCollectionishesIterable();
		createDoubleTypes(TypePackage.getRoot());
	}

	/**
	 * @see Arrayish
	 * @see Setish
	 * @see Mapish
	 */
	private native void makeCollectionishesIterable() /*-{
		$wnd.$T.Array.prototype[Symbol.iterator] = $wnd.$T.Array.prototype.values;
		$wnd.$T.Set.prototype[Symbol.iterator] = $wnd.$T.Set.prototype.values;
		$wnd.$T.Map.prototype[Symbol.iterator] = $wnd.$T.Map.prototype.entries;
	}-*/;

	// The inner brackets around class definitions are mandatory!!!
	public static native void createDoubleTypes(JavaScriptObject typeNs) /*-{
		typeNs.Float =eval('(class Float  extends Number {type(){return "f"}})');
		typeNs.Double=eval('(class Double extends Number {type(){return "d"}})');
	}-*/;

	// Just in case, this is an alternative without eval:

	// $wnd.Double = function Double(value) {
	// Object.defineProperty(this, 'value', {
	// value: value.valueOf(),
	// writable: false,
	// enumerable: true
	// });
	// };
	//
	// // Set up inheritance
	// $wnd.Double.prototype = Object.create(Number.prototype);
	// $wnd.Double.prototype.constructor = $wnd.Double;
	// $wnd.Double.prototype.valueOf = function() {
	// return this.value;
	// };
	// $wnd.Double.prototype.toString = function() {
	// return this.value.toString();
	// };

}

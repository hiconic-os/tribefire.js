// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
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
		JavaScriptObject root = TypePackage.getRoot();
		makeCollectionishesIterable(root);
		createDoubleTypes(root);
	}

	/**
	 * @see Arrayish
	 * @see Setish
	 * @see Mapish
	 */
	private native void makeCollectionishesIterable(JavaScriptObject typeNs) /*-{
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

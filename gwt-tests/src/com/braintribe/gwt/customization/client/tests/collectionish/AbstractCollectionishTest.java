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
package com.braintribe.gwt.customization.client.tests.collectionish;

import com.braintribe.gwt.customization.client.tests.AbstractGwtTest;
import com.google.gwt.core.client.JavaScriptObject;

import jsinterop.annotations.JsMethod;

public abstract class AbstractCollectionishTest extends AbstractGwtTest {

	@JsMethod
	protected native void assertIteration(JavaScriptObject adapter, JavaScriptObject a, String iterationBody, JavaScriptObject expected) /*-{
		this.assertEqOperation(adapter, a, '[...' + iterationBody + '].join()', expected);
	}-*/;

	@JsMethod
	protected native void assertEqOperation(JavaScriptObject adapter, JavaScriptObject a, String operationBody, JavaScriptObject expected) /*-{
		var f = eval("(function(a){return " + operationBody + ";})");
		var actualAdapted = f(adapter);
		var actual = f(a);

		// optionally check an explicit result that is expected
		if (expected)
			this.assertEqual(actualAdapted, expected, operationBody + " == " + expected);
		this.assertEqual(actualAdapted, actual, operationBody + " == " + actual + " (native JS object comparison)", expected != null);
	}-*/;

}

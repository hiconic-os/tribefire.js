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

import static com.braintribe.utils.lcd.CollectionTools2.asSet;

import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Setish;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Tests for {@link Setish}.
 */
public class SetishTest extends AbstractCollectionishTest{

	@Override
	protected void tryRun() throws Exception {
		Setish<String> set = new Setish<>(asSet("A", "B", "C"), null, null);
		JavaScriptObject jsSet = abcJsSet();

		testMethods(set, jsSet);
	}

	// $wnd is GWT's local variable for the right window/globalThis object
	private native JavaScriptObject abcJsSet() /*-{
		return new $wnd.Set(['A', 'B', 'C']);
	}-*/;

	private native void testMethods(Setish<String> sa, JavaScriptObject s) /*-{
		this.assertEqual(eval("[...Sa]"), ['A', 'B', 'C'], "spread to array");

		this.assertEqOperation(sa, s, "a.size", 3);

		this.assertEqOperation(sa, s, 'a.has("B")', true);
		this.assertEqOperation(sa, s, 'a.has("X")', false);

		this.assertEqOperation(sa, s, 'Array.from(a.add("D")).toString()', 'A,B,C,D');

		this.assertEqOperation(sa, s, 'a.delete("X")', false);
		this.assertEqOperation(sa, s, 'a.delete("D")', true);
		this.assertEqOperation(sa, s, 'Array.from(a).toString()', 'A,B,C');

		this.assertEqOperation(sa, s, '(i=0,a.forEach(function(x){i++}), i);', 3);

		this.assertEqOperation(sa, s, 'a.clear()');
		this.assertEqOperation(sa, s, "a.size", 0);


		// --- es2015.iterable ---
		this.assertEqOperation(sa, s, 'a.add("A").add("B").size', '2');

		this.assertIteration(sa, s, 'a.entries()', 'A,A,B,B');
		this.assertIteration(sa, s, 'a.keys()', 'A,B');
		this.assertIteration(sa, s, 'a.values()', 'A,B');
	}-*/;

}

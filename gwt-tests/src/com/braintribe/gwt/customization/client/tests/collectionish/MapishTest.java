package com.braintribe.gwt.customization.client.tests.collectionish;

import static com.braintribe.utils.lcd.CollectionTools2.asMap;

import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Mapish;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Tests for {@link Mapish}.
 */
public class MapishTest extends AbstractCollectionishTest {

	@Override
	protected void tryRun() throws Exception {
		Mapish<Double, String> map = new Mapish<>(asMap(1d, "A", 2d, "B", 3d, "C"), null, null, null, null);
		JavaScriptObject jsMap = abcJsMap();

		testMethods(map, jsMap);
	}

	private native JavaScriptObject abcJsMap() /*-{
		return new $wnd.Map([[1, "A"], [2, "B"], [3, "C"]]);
	}-*/;

	private native void testMethods(Mapish<Double, String> ma, JavaScriptObject m) /*-{
		//this.assertEqual(eval("[...Sa]"), ['A', 'B', 'C'], "spread to array");

		this.assertEqOperation(ma, m, "a.size", 3);

		this.assertEqOperation(ma, m, 'a.has(2)', true);
		this.assertEqOperation(ma, m, 'a.has(4)', false);

		this.assertEqOperation(ma, m, 'a.get(2)', "B");
		this.assertEqOperation(ma, m, 'a.get(4)', undefined);

		this.assertEqOperation(ma, m, 'a.set(4, "D").size');
		this.assertEqOperation(ma, m, 'a.get(4)', "D");

		this.assertEqOperation(ma, m, 'a.delete(5)', false);
		this.assertEqOperation(ma, m, 'a.delete(4)', true);
		this.assertEqOperation(ma, m, 'a.get(4)', undefined);

		this.assertEqOperation(ma, m, '(i=0,a.forEach(function(x){i++}), i);', 3);

		this.assertEqOperation(ma, m, 'a.clear()');
		this.assertEqOperation(ma, m, "a.size", 0);

		// --- es2015.iterable ---
		this.assertEqOperation(ma, m, 'a.set(1, "A").set(2, "B").size');

		this.assertIteration(ma, m, 'a.entries()', '1,A,2,B');
		this.assertIteration(ma, m, 'a.keys()', '1,2');
		this.assertIteration(ma, m, 'a.values()', 'A,B');
	}-*/;

}

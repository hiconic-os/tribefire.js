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

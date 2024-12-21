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
package com.braintribe.gwt.customization.client.tests;

import com.braintribe.gwt.logging.client.ExceptionUtil;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.dom.client.Style.Unit;

import jsinterop.annotations.JsMethod;

/**
 * @author peter.gazdik
 */
public abstract class AbstractGwtTest {

	public void run() {
		log("Running: " + this.getClass().getSimpleName());

		try {
			tryRun();

		} catch (Exception e) {
			logErrorDetail("error: " + e.getMessage(), e);
		}

		logSeparator();
	}

	protected abstract void tryRun() throws Exception;

	// ##########################################
	// ## . . . . . . Util methods . . . . . . ##
	// ##########################################

	protected static <T> T cast(Object o) {
		return (T) o;
	}

	// ##########################################
	// ## . . . . . Assertions . . . . . . . . ##
	// ##########################################

	protected void assertNotNull(Object o, String descriptor) {
		if (o == null)
			throw new RuntimeException("Object is null: " + descriptor);
	}

	@JsMethod(name = "assertEqual")
	public native void assertEqualJs(JavaScriptObject actual, JavaScriptObject expected, String message, JavaScriptObject omitLogIfOk) /*-{
		if (!this.areEqual(actual, expected)) 
			this.@AbstractGwtTest::logError(*)("Error: " + message + //
					 "   Expected: " + expected + " (" + this.typeOf(expected) + ")" + //
					", actual: " + actual + " (" +  this.typeOf(actual) + ")");

		else if (!omitLogIfOk)
			this.@AbstractGwtTest::log(*)("    " + message + " [OK]");
	}-*/;

	@JsMethod
	protected native void assertOperation(Object o, String operationBody, Object expected) /*-{
		var f = eval("(function(x){return " + operationBody + ";})");
		var actual = f(o);
		this.assertEqual(actual, expected, operationBody + " == " + expected);
	}-*/;

	@JsMethod
	private native boolean areEqual(Object a, Object b) /*-{
		if (a == b)
			return true;
		if (!Array.isArray(a))
			return false;
		if (!Array.isArray(b))
			return false;
		if (a.length != b.length)
			return false;
		for (var i = 0; i < a.length; i++)
			if (!this.areEqual(a[i], b[i]))
				return false;
		return true;
	}-*/;

	protected <T> void assertArraysEquals(T[] arr1, T[] arr2, String desc) {
		if (arr1.length != arr2.length) {
			logError("Arrays have different lengths: " + desc + " First: " + arr1 + ", Second: " + arr2);
			return;
		}

		boolean hasError = false;
		for (int i = 0; i < arr1.length; i++)
			if (!areEqual(arr1[i], arr2[i])) {
				logError("Difference at index " + i + " - " + desc);
				hasError = true;
			}

		if (!hasError)
			log("    " + desc + " [OK]");

	}

	@JsMethod
	private native boolean areJsEqual(Object a, Object b) /*-{
		return this.areEqual(a, b);
	}-*/;

	@JsMethod(name = "typeOf")
	private native String typeOf(JavaScriptObject o) /*-{
		if (o == undefined || o == null)
			return "N/A";

		if (o.@Object::getClass())
			return o.@Object::getClass()();

		return typeof o;
	}-*/;

	@JsMethod
	protected void assertTrue(boolean bool, String what) {
		if (bool)
			log("    " + what + " [OK]");
		else
			logError("    " + what + " [ERROR]");
	}

	// ##########################################
	// ## . . . . . . . tf.js . . . . . . . . .##
	// ##########################################

	protected final native JavaScriptObject getJsProperty(JavaScriptObject jso, String name) /*-{
		return jso[name];
	}-*/;

	protected final native <T> T getJsPropertyCasted(JavaScriptObject jso, String name) /*-{
		return jso[name];
	}-*/;

	// ##########################################
	// ## . . . . . . . Printing . . . . . . . ##
	// ##########################################

	protected void logSeparator() {
		Document document = Document.get();
		document.getBody().appendChild(document.createHRElement());
	}

	@JsMethod
	protected void log(String msg) {
		Document document = Document.get();
		PreElement preElement = document.createPreElement();
		preElement.appendChild(document.createTextNode(msg));
		preElement.getStyle().setMargin(0, Unit.PX);
		document.getBody().appendChild(preElement);
	}

	@JsMethod
	protected void logErrorDetail(String msg, Throwable t) {
		Document document = Document.get();
		PreElement preElement = document.createPreElement();
		preElement.getStyle().setColor("red");
		preElement.getStyle().setMargin(0, Unit.PX);
		preElement.appendChild(document.createTextNode(msg + "\n" + ExceptionUtil.format(t)));
		document.getBody().appendChild(preElement);
	}

	@JsMethod
	protected void logError(String msg) {
		logMsg(msg, "red");
	}

	@JsMethod
	protected void logWarn(String msg) {
		logMsg(msg, "orange");
	}

	private void logMsg(String msg, String color) {
		Document document = Document.get();
		PreElement preElement = document.createPreElement();
		preElement.getStyle().setColor(color);
		preElement.getStyle().setMargin(0, Unit.PX);
		preElement.appendChild(document.createTextNode(msg));
		document.getBody().appendChild(preElement);
	}

}

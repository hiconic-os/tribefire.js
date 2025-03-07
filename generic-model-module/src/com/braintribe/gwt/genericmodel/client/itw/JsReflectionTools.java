// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
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
package com.braintribe.gwt.genericmodel.client.itw;

import com.braintribe.common.lcd.Pair;
import com.google.gwt.core.client.GwtScriptOnly;
import com.google.gwt.core.client.JavaScriptObject;

import javaemul.internal.annotations.DoNotInline;
import jsinterop.context.JsKeywords;

/**
 * @author peter.gazdik
 */
@GwtScriptOnly
public class JsReflectionTools {

	private static final String NEW_ = "new ";
	private static final String THIS_DOT = "this.";

	/** Expects source to be like "function() { return new Xyz;}" of "function() {new Xyz;}", where Xyz is a constructor function (created as a constructor for a java class) */
	public static Object extractConstructor(JavaScriptObject constructorFunction) {
		String constructor = trimmedSubstring(constructorFunction, NEW_, "}");
		constructor = ensureJsExpressionWithoutSemicolon(constructor);

		return ScriptOnlyItwTools.eval(constructor);
	}

	/** Expects source to be like "function() {this.${jsProperty}}" */
	public static String extractJsPropertyName(JavaScriptObject propertyWrappedInFunction) {
		String jsPropertyName = trimmedSubstring(propertyWrappedInFunction, THIS_DOT, "}");
		return ensureJsExpressionWithoutSemicolon(jsPropertyName);
	}

	/** GWT likes to play around, sometimes the ';' is there, sometimes it is not (e.g. for code like: <tt>function() {return foo.bar;}</tt>) */
	private static String ensureJsExpressionWithoutSemicolon(String expressionWithPossibleSemicolon) {
		if (expressionWithPossibleSemicolon.endsWith(";"))
			return expressionWithPossibleSemicolon.substring(0, expressionWithPossibleSemicolon.length() - 1).trim();
		else
			return expressionWithPossibleSemicolon;
	}

	private static String trimmedSubstring(JavaScriptObject jso, String left, String right) {
		String source = jso.toString();

		int s = source.indexOf(left);
		int e = source.lastIndexOf(right);

		return source.substring(s + left.length(), e).trim();
	}

	/** This is here to avoid inline implementation, because this will be obfuscated and thus shorter. This one doesn't seem to work though. */
	@DoNotInline
	public static native JavaScriptObject prototypeOf(JavaScriptObject jso) /*-{
	 	return jso.prototype;
	}-*/;

	/** This is here to avoid inline implementation, because this will be obfuscated and thus shorter */
	public static void defineGmPropertyForAccessors(JavaScriptObject target, String propertyName, Pair<JavaScriptObject, JavaScriptObject> accessorPair) {
		defineActualProperty(target, JsKeywords.javaIdentifierToJs(propertyName), accessorPair.first, accessorPair.second);
	}

	public static final void defineGmProperty(JavaScriptObject target, String propertyName, JavaScriptObject getter, JavaScriptObject setter) {
		defineActualProperty(target, JsKeywords.javaIdentifierToJs(propertyName), getter, setter);
	}

	private static final native void defineActualProperty(JavaScriptObject target, String propertyName, JavaScriptObject getter, JavaScriptObject setter) /*-{
		Object.defineProperty(target, propertyName, {
			get: getter,
			set: setter
		})
	}-*/;

}

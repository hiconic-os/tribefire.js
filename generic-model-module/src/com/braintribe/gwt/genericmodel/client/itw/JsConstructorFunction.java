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

import com.google.gwt.core.client.GwtScriptOnly;
import com.google.gwt.core.client.JavaScriptObject;

@GwtScriptOnly
public class JsConstructorFunction extends GenericJavaScriptObject {

	protected JsConstructorFunction() {

	}

	public final static JsConstructorFunction create(Class<?> javaClass, JavaScriptObject superPrototype) {
		CastableTypeMap superTypeMap = ScriptOnlyItwTools.getCastableTypeMap(superPrototype);
		CastableTypeMap castableTypeMap = (CastableTypeMap) ScriptOnlyItwTools.portableObjectCreate(superTypeMap);

		JsConstructorFunction result = createRawConstructor(superConstructor);

		GenericJavaScriptObject prototype = ScriptOnlyItwTools.portableObjectCreate(superPrototype);

		ScriptOnlyItwTools.setClass(prototype, javaClass);
		ScriptOnlyItwTools.setCastableTypeMap(prototype, castableTypeMap); // TODO we might want to check if this is even needed

		result.setPrototype(prototype);

		return result;
	}

	private static JsConstructorFunction superConstructor = enhancedEntityStubConstructor();

	/** Uses {@link GwtEnhancedEntityStub} */
	private static native JsConstructorFunction enhancedEntityStubConstructor() /*-{
		return @JsReflectionTools::extractConstructor(*)(@GwtEnhancedEntityStub::new());
	}-*/;

	// @formatter:off
	private static native JsConstructorFunction createRawConstructor(JsConstructorFunction superConstructor) /*-{
		return function() {
			superConstructor.call(this);
		};
	}-*/;
	
	protected final native <T> T newInstance() /*-{
		return new this;
	}-*/;

	public final native GenericJavaScriptObject getPrototype() /*-{
		return this.prototype;
	}-*/;

	private native void setPrototype(GenericJavaScriptObject prototype) /*-{
		this.prototype = prototype;
	}-*/;
	// @formatter:on

	public final CastableTypeMap getCastableTypeMap() {
		return ScriptOnlyItwTools.getCastableTypeMap(getPrototype());
	}

}

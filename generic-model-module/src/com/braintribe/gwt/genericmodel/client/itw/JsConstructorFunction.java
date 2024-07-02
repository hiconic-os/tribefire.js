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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.gwt.genericmodel.client.itw;

import com.google.gwt.core.client.GwtScriptOnly;

@GwtScriptOnly
public class JsConstructorFunction extends GenericJavaScriptObject {

	protected JsConstructorFunction() {

	}

	public final static JsConstructorFunction create(Class<?> clazz, JsConstructorFunction superConstructor) {
		Object superPrototype = superConstructor.getPrototype();

		CastableTypeMap superTypeMap = ScriptOnlyItwTools.getCastableTypeMap(superPrototype);
		CastableTypeMap derivedTypeMap = (CastableTypeMap) ScriptOnlyItwTools.portableObjectCreate(superTypeMap);

		// TODO replace superConstructor with GwtEnhancedEntityStub constructor
		return createConstructor(superConstructor, clazz, derivedTypeMap);
	}

	private static JsConstructorFunction createConstructor(JsConstructorFunction superConstructor, Class<?> javaClass,
			CastableTypeMap castableTypeMap) {
		JsConstructorFunction result = createRawConstructor(superConstructor);

		GenericJavaScriptObject prototype = ScriptOnlyItwTools.portableObjectCreate(superConstructor.getPrototype());

		ScriptOnlyItwTools.setClass(prototype, javaClass);
		ScriptOnlyItwTools.setCastableTypeMap(prototype, castableTypeMap); // TODO we might want to check if this is even needed

		ScriptOnlyItwTools.setPrototype(result, prototype);

		return result;
	}

	// @formatter:off
	private static native JsConstructorFunction createRawConstructor(JsConstructorFunction superConstructor) /*-{
		return function() {
			superConstructor.call(this);
		};
	}-*/;
	
	protected final native <T> T newInstance() /*-{
		return new this;
	}-*/;
	// @formatter:on

	public final GenericJavaScriptObject getPrototype() {
		return ScriptOnlyItwTools.getPrototype(this);
	}

	public final CastableTypeMap getCastableTypeMap() {
		return ScriptOnlyItwTools.getCastableTypeMap(getPrototype());
	}

}

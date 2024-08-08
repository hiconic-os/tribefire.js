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

import com.braintribe.model.generic.reflection.TypeCode;
import com.google.gwt.core.client.JavaScriptObject;

import jsinterop.context.JsKeywords;

/**
 * @author peter.gazdik
 */
public class PropertyBinding {

	public GwtScriptProperty property;
	public boolean runtime; // property was first introduced in runtime (no super-type has it) -> no getter/setter is needed
	public String getterName;
	public String setterName;
	public JavaScriptObject getterFunction;
	public JavaScriptObject setterFunction;

	public static JavaScriptObject createAndSetForNonCollection( //
			GwtScriptProperty property, JavaScriptObject classProto, //
			int inheritedFromSuperclass, int isOverlay, TypeCode valueType) {

		return createAndSetForCollection(property, classProto, inheritedFromSuperclass, isOverlay, null, null, valueType);
	}

	/**
	 * Uses: {@link JsKeywords}
	 */
	public static native JavaScriptObject createAndSetForCollection( //
			GwtScriptProperty property, JavaScriptObject classProto, //
			int inheritedFromSuperclass, int isOverlay, TypeCode collectionType, TypeCode keyType, TypeCode valueType) /*-{
		var tmp, handler, getterName;

		if (inheritedFromSuperclass == 0 && !property.@GwtScriptProperty::isNullable()()) {
			tmp = property.@GwtScriptProperty::getFieldName()();
			classProto[tmp] = @DefaultLiterals::forType(*)(valueType);
		}

		handler = {
		    get: function(target, prop, receiver) {
		    	if (getterName == null) {
					getterName = prop;
					return receiver;
		    	}

		        var tmp = @GenericAccessorMethods::buildJsConvertingAccessors(*)(property, classProto[getterName], classProto[prop], collectionType, keyType, valueType);
				@JsReflectionTools::defineProperty(*)(classProto, @JsKeywords::javaIdentifierToJs(*)(property.@GwtScriptProperty::getName()()), tmp);

		        if (isOverlay == 0) {
			        var pb = @PropertyBinding::new()();
			        pb.@PropertyBinding::property = property;
			        pb.@PropertyBinding::getterFunction = classProto[getterName];
			        pb.@PropertyBinding::setterFunction = classProto[prop];
			        pb.@PropertyBinding::getterName = getterName;
			        pb.@PropertyBinding::setterName = prop;

			        property.@GwtScriptProperty::propertyBinding=pb;
		        }

		        return this;
		    }
		};

		return new Proxy({}, handler);
	}-*/;

}

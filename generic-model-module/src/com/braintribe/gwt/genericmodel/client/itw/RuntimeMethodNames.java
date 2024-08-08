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

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.type.custom.AbstractEntityType;

/**
 * Uses {@link JsNameReflector}
 * 
 * @author peter.gazdik
 */
public class RuntimeMethodNames {

	/** Uses {@link Object} */
	public static native String objectGetClass() /*-{
		return @JsNameReflector::JsNR.@Object::getClass();
	}-*/;

	/** Uses {@link Enum} */
	public static native String enumGetDeclaringClass()/*-{
		return @JsNameReflector::JsNR.@Enum::getDeclaringClass();
	}-*/;

	/** Uses {@link EnumBase} */
	public static native String entityBaseType()/*-{
		return @JsNameReflector::JsNR.@EnumBase::type();
	}-*/;
	
	/** Uses {@link Property} */
	public static native String propertyGetDirectUnsafe()/*-{
		return @JsNameReflector::JsNR.@Property::getDirectUnsafe(*);
	}-*/;
	
	/** Uses {@link Property} */
	public static native String propertySetDirectUnsafe()/*-{
		return @JsNameReflector::JsNR.@Property::setDirectUnsafe(*);
	}-*/;

	/** Uses {@link AbstractEntityType} */
	public static native String abstractEntityTypeToString()/*-{
		return @JsNameReflector::JsNR.@AbstractEntityType::toString(Lcom/braintribe/model/generic/GenericEntity;);
	}-*/;
	
	/** Uses {@link AbstractEntityType} */
	public static native String abstractEntityTypeGetSelectiveInformationFor()/*-{
		return @JsNameReflector::JsNR.@AbstractEntityType::getSelectiveInformationFor(Lcom/braintribe/model/generic/GenericEntity;);
	}-*/;

}

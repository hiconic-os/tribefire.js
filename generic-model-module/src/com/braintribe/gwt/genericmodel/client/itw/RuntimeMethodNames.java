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

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.base.EntityBase;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.type.custom.AbstractEntityType;
import com.google.gwt.core.client.GWT;

/**
 * @author peter.gazdik
 */
public interface RuntimeMethodNames {

	public static final RuntimeMethodNames instance = GWT.create(RuntimeMethodNames.class);
	
	@MethodIdentification(declarationClass=Object.class, name="getClass")
	String objectGetClass();

	@MethodIdentification(declarationClass=Enum.class, name="getDeclaringClass")
	String enumGetDeclaringClass();

	@MethodIdentification(declarationClass=EntityBase.class, name="type")
	String entityBaseType();
	
	@MethodIdentification(declarationClass=Property.class, name="getDirectUnsafe", parameterTypes = {GenericEntity.class})
	String propertyGetDirectUnsafe();
	
	@MethodIdentification(declarationClass=Property.class, name="setDirectUnsafe", parameterTypes = {GenericEntity.class, Object.class})
	String propertySetDirectUnsafe();

	@MethodIdentification(declarationClass=AbstractEntityType.class, name="toString", parameterTypes = {GenericEntity.class})
	String abstractEntityTypeToString();
	
	@MethodIdentification(declarationClass=AbstractEntityType.class, name="getSelectiveInformationFor", parameterTypes = {GenericEntity.class})
	String abstractEntityTypeGetSelectiveInformationFor();

}

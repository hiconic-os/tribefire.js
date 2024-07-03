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
package com.braintribe.model.processing.smood.manipulation;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.manipulation.LocalEntityProperty;
import com.braintribe.model.generic.manipulation.PropertyManipulation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.processing.manipulator.api.ManipulatorContext;

/**
 * 
 */
class Tools {

	static boolean propertyIsAbsent(PropertyManipulation manipulation, ManipulatorContext context) {
		LocalEntityProperty owner = context.resolveOwner(manipulation);
		GenericEntity entity = owner.getEntity();
		EntityType<GenericEntity> entityType = entity.entityType();
		Property property = entityType.getProperty(owner.getPropertyName());

		return property.isAbsent(entity);
	}

}

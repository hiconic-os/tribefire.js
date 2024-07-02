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
package com.braintribe.model.generic.enhance;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.PropertyAccessInterceptor;
import com.braintribe.model.generic.reflection.VdHolder;
import com.braintribe.model.generic.value.ValueDescriptor;

@SuppressWarnings("unusable-by-js")
public class SessionUnboundPropertyAccessInterceptor extends PropertyAccessInterceptor {

	public static final SessionUnboundPropertyAccessInterceptor INSTANCE = new SessionUnboundPropertyAccessInterceptor();

	private SessionUnboundPropertyAccessInterceptor() {
	}

	@Override
	public Object getProperty(Property property, GenericEntity entity, boolean isVd) {
		Object result = isVd ? property.getVdDirect(entity) : property.getDirectUnsafe(entity);
		GenericModelType propertyType = property.getType();

		if (result == null) {
			if (propertyType.isCollection()) {
				result = ((CollectionType) propertyType).createPlain();

				// We must set the value that we have created here!
				property.setDirectUnsafe(entity, result);
			}

			return result;
		}

		if (VdHolder.isAbsenceInfo(result)) {
			return property.isNullable() ? null : propertyType.getDefaultValue();
		}

		return result;
	}

	@Override
	public Object setProperty(Property property, GenericEntity entity, Object value, boolean isVd) {
		if (isVd)
			return property.setVdDirect(entity, (ValueDescriptor) value);
		else
			return property.setDirect(entity, value);
	}

}

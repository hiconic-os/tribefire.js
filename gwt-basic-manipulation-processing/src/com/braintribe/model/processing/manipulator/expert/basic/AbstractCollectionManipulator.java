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
package com.braintribe.model.processing.manipulator.expert.basic;

import java.util.List;
import java.util.Set;

import com.braintribe.common.lcd.UnknownEnumException;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.manipulation.CollectionManipulation;
import com.braintribe.model.generic.reflection.BaseType;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.CollectionType.CollectionKind;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.GenericModelTypeReflection;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.processing.manipulator.api.CollectionManipulator;
import com.braintribe.model.processing.manipulator.api.Manipulator;
import com.braintribe.model.processing.manipulator.expert.basic.collection.ListManipulator;
import com.braintribe.model.processing.manipulator.expert.basic.collection.MapManipulator;
import com.braintribe.model.processing.manipulator.expert.basic.collection.SetManipulator;

public abstract class AbstractCollectionManipulator<T extends CollectionManipulation> implements Manipulator<T> {
	
	protected <C, I> CollectionManipulator<C, I> getCollectionManipulator(Object value, CollectionType collectionType) {
		CollectionKind collectionKind = collectionType != null ? collectionType.getCollectionKind() : getCollectionKind(value);
		
		switch (collectionKind) {
			case list:
				return (CollectionManipulator<C, I>) ListManipulator.INSTANCE;
			case map:
				return (CollectionManipulator<C, I>) MapManipulator.INSTANCE;
			case set:
				return (CollectionManipulator<C, I>) SetManipulator.INSTANCE;
			default:
				throw new UnknownEnumException(collectionKind);
		}
	}

	private CollectionKind getCollectionKind(Object value) {
		if (value instanceof List) {
			return CollectionKind.list;

		} else if (value instanceof Set) {
			return CollectionKind.set;

		} else {
			return CollectionKind.map;
		}
	}

	protected GenericModelType getKeyType(Object value, CollectionType collectionType) {
		if (collectionType == null) {
			return value instanceof List ? GenericModelTypeReflection.TYPE_INTEGER : BaseType.INSTANCE;
		}
		
		switch (collectionType.getCollectionKind()) {
			case list:
				return GenericModelTypeReflection.TYPE_INTEGER;
			case set:
				return collectionType.getCollectionElementType();
			case map:
				return collectionType.getParameterization()[0];
			default:
				throw new UnknownEnumException(collectionType.getCollectionKind());
		}
	}

	protected GenericModelType getValueType(CollectionType collectionType) {
		return collectionType != null ? collectionType.getCollectionElementType() : BaseType.INSTANCE;
	}

	protected Object acquireCollectionPropertyValue(GenericEntity entity, EntityType<GenericEntity> entityType, String propertyName,
			GenericModelType propertyType) {

		Property property = entityType.getProperty(propertyName);
		Object collection = property.get(entity);

		if (collection == null) {
			/* NOTE regarding the cast of propertyType - this code (inside the if-block is only reachable iff propertyType is collection
			 * type. Otherwise (if it is object)), the collection must already have been set, so it's not null. */
			property.set(entity, ((CollectionType) propertyType).createPlain());
			collection = property.get(entity);
		}

		return collection;
	}

}

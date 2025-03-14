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
package com.braintribe.model.generic.reflection.type.collection;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.generic.collection.Collectionish;
import com.braintribe.model.generic.reflection.AbstractGenericModelType;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.GenericModelException;
import com.braintribe.model.generic.reflection.GenericModelType;

public abstract class AbstractCollectionType extends AbstractGenericModelType implements CollectionType {

	private Boolean customInstancesReachable;
	private Boolean entitiesReachable;

	public AbstractCollectionType(Class<?> javaType) {
		super(javaType);
	}

	public static final CollectionKind collectionKindFor(Class<?> javaType) {
		if (javaType == Map.class) {
			return CollectionKind.map;
		} else if (javaType == List.class) {
			return CollectionKind.list;
		} else if (javaType == Set.class) {
			return CollectionKind.set;
		}
		throw new GenericModelException("Unsupported java type for collection: " + javaType);
	}

	/** There should be no reason to call this. Use {@link #createPlain()} instead. */
	@Override
	public final Object create() {
		throw new UnsupportedOperationException("Method 'CollectionType.create' is not supported");
	}

	@Override
	public final boolean isAssignableFrom(GenericModelType type) {
		return this == type;
	}

	@Override
	public final boolean isCollection() {
		return true;
	}

	protected static final boolean isSimpleOrEnumContent(GenericModelType type) {
		switch (type.getTypeCode()) {
			case objectType:
			case entityType:
				return false;
			case listType:
			case mapType:
			case setType:
				throw new IllegalArgumentException("Collection parameter type cannot be a collection. Parameter type: " + type);
			default:
				return true;
		}
	}

	@Override
	public final boolean areCustomInstancesReachable() {
		if (customInstancesReachable == null) {
			customInstancesReachable = _areCustomInstancesReachable();
		}
		return customInstancesReachable;
	}

	private boolean _areCustomInstancesReachable() {
		for (GenericModelType type : getParameterization()) {
			if (type.areCustomInstancesReachable())
				return true;
		}
		return false;
	}

	@Override
	public final boolean areEntitiesReachable() {
		if (entitiesReachable == null) {
			entitiesReachable = _areEntitiesReachable();
		}
		return entitiesReachable;
	}

	private final boolean _areEntitiesReachable() {
		for (GenericModelType type : getParameterization()) {
			if (type.areEntitiesReachable())
				return true;
		}
		return false;
	}

	@Override
	public boolean isInstance(Object value) {
		return isInstanceOfThis(value);
	}

	protected abstract boolean isInstanceOfThis(Object value);

	@Override
	public boolean isEmptyJs(Object value) {
		return isInstanceJs(value) && //
				((Collectionish) value).isEmpty();
	}

}

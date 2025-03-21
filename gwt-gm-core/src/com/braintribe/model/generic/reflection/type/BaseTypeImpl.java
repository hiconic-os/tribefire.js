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
package com.braintribe.model.generic.reflection.type;

import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.reflection.AbstractGenericModelType;
import com.braintribe.model.generic.reflection.BaseType;
import com.braintribe.model.generic.reflection.CloningContext;
import com.braintribe.model.generic.reflection.GenericModelException;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.GenericModelTypeJs;
import com.braintribe.model.generic.reflection.GenericModelTypeReflection;
import com.braintribe.model.generic.reflection.GenericModelTypeReflectionJs;
import com.braintribe.model.generic.reflection.StrategyOnCriterionMatch;
import com.braintribe.model.generic.reflection.TraversingContext;
import com.braintribe.model.generic.reflection.TypeCode;

public class BaseTypeImpl extends AbstractGenericModelType implements BaseType {

	public static final BaseTypeImpl INSTANCE = new BaseTypeImpl();

	private BaseTypeImpl() {
		super(Object.class);
	}

	/* NOTE: This must be initialized lazily!!! */
	private GenericModelTypeReflection typeReflection;

	@Override
	public TypeCode getTypeCode() {
		return TypeCode.objectType;
	}

	@Override
	public boolean isBase() {
		return true;
	}

	@Override
	public boolean isEmpty(Object value) {
		if (value == null)
			return true;

		GenericModelType type = getActualType(value);
		return type.isEmpty(value);
	}

	@Override
	public boolean isEmptyJs(Object value) {
		if (value == null)
			return true;

		GenericModelTypeJs type = (GenericModelTypeJs) getActualTypeJs(value);
		return type.isEmptyJs(value);
	}

	public AbstractGenericModelType getActualAbsType(Object value) {
		return (AbstractGenericModelType) getActualType(value);
	}

	@Override
	public GenericModelType getActualType(Object value) {
		if (value == null)
			return this;

		return getTypeReflection().getType(value);
	}

	@Override
	public GenericModelType getActualTypeJs(Object value) {
		if (value == null)
			return this;

		GenericModelTypeReflectionJs tr = (GenericModelTypeReflectionJs) getTypeReflection();
		return tr.getTypeJs(value);
	}

	private GenericModelTypeReflection getTypeReflection() {
		if (typeReflection == null) {
			typeReflection = GMF.getTypeReflection();
		}

		return typeReflection;
	}

	@Override
	public Object cloneImpl(CloningContext cloningContext, Object instance, StrategyOnCriterionMatch strategy) throws GenericModelException {
		if (instance == null)
			return null;
		else
			return getActualAbsType(instance).cloneImpl(cloningContext, instance, strategy);
	}

	@Override
	public String getSelectiveInformation(Object instance) {
		if (instance == null)
			return null;
		else
			return getActualAbsType(instance).getSelectiveInformation(instance);
	}

	@Override
	public String getTypeName() {
		return "object";
	}

	/** {@inheritDoc} */
	@Override
	public Object getValueSnapshot(Object value) {
		return value != null ? getActualType(value).getValueSnapshot(value) : null;
	}

	@Override
	public void traverseImpl(TraversingContext traversingContext, Object instance) throws GenericModelException {
		if (instance != null) {
			getActualAbsType(instance).traverseImpl(traversingContext, instance);
		}
	}

	@Override
	public boolean isAssignableFrom(GenericModelType type) {
		return true;
	}

	@Override
	public boolean isValueAssignable(Object value) {
		return true;
	}

	@Override
	public boolean isInstance(Object value) {
		return value != null;
	}

	@Override
	public boolean isInstanceJs(Object value) {
		return value != null;
	}

	@Override
	public boolean areCustomInstancesReachable() {
		return true;
	}

	@Override
	public boolean areEntitiesReachable() {
		return true;
	}
}

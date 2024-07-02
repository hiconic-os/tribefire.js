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
package com.braintribe.model.generic.reflection;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.braintribe.model.generic.GenericEntity;

/**
 * @author peter.gazdik
 */
@SuppressWarnings("unusable-by-js")
public abstract class AbstractTransientProperty implements TransientProperty, JsInteropAttribute {

	private final String name;
	private final Class<?> type;
	private final Function<GenericEntity, Object> getter;
	private final BiConsumer<GenericEntity, Object> setter;

	public AbstractTransientProperty(String name, Class<?> type, Function<GenericEntity, Object> getter, BiConsumer<GenericEntity, Object> setter) {
		this.name = name;
		this.type = type;
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public Class<?> getJavaType() {
		return type;
	}

	@Override
	public final EntityType<?> getFirstDeclaringType() {
		return getDeclaringType();
	}

	@Override
	public boolean isNullable() {
		return type.isPrimitive();
	}

	@Override
	public <T> T getJs(GenericEntity entity) {
		return get(entity);
	}

	@Override
	public void setJs(GenericEntity entity, Object value) {
		set(entity, value);
	}

	@Override
	public <T> T get(GenericEntity entity) {
		return (T) getter.apply(entity);
	}

	@Override
	public void set(GenericEntity entity, Object value) {
		setter.accept(entity, value);
	}

	@Override
	public <T> T getDirectUnsafe(GenericEntity entity) {
		return get(entity);
	}

	@Override
	public void setDirectUnsafe(GenericEntity entity, Object value) {
		set(entity, value);
	}

	@Override
	public <T> T getDirect(GenericEntity entity) {
		return get(entity);
	}

	@Override
	public Object setDirect(GenericEntity entity, Object value) {
		Object result = get(entity);
		set(entity, value);
		return result;
	}

}

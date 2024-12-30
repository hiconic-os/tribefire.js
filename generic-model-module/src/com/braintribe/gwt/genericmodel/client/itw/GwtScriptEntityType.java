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

import static java.util.Collections.emptyList;

import java.util.List;

import com.braintribe.gwt.genericmodel.client.reflect.GwtEntityType;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.enhance.EnhancedEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.JsInteropEntityType;
import com.braintribe.model.generic.reflection.TransientProperty;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * An extension for {@link EntityType} useful for implementation of {@link EnhancedEntity} in GWT.
 */
public class GwtScriptEntityType<T extends GenericEntity> extends GwtEntityType<T> implements JsInteropEntityType<T> {

	private EntityTypeBinding entityTypeBinding;
	private static int scriptTypeIds = -1;
	private final int typeId = scriptTypeIds--;

	/**
	 * This is called iff there are not transient properties, including within super-types.
	 * 
	 * @see GwtEntityType#setDeclaredTransientProperties(TransientProperty[])
	 */
	public void setNoTransientProperties() {
		super.setTransientProperties(emptyList());
	}

	public EntityTypeBinding getEntityTypeBinding() {
		return entityTypeBinding;
	}

	public void setEntityTypeBinding(EntityTypeBinding entityBinding) {
		this.entityTypeBinding = entityBinding;
	}

	public void bindWith(EntityTypeBinding etb, GenericJavaScriptObject proto) {
		this.entityTypeBinding = etb;
		this.setProtoInstance(proto);

		etb.entityType = this;
		etb.enhancedProto = proto;
	}

	@Override
	public JavaScriptObject getPrototype() {
		return entityTypeBinding.enhancedProto;
	}

	public List<GwtScriptEntityType<?>> getGwtScriptSuperTypes() {
		return (List<GwtScriptEntityType<?>>) (List<?>) super.getSuperTypes();
	}

	public List<GwtScriptProperty> getGwtScriptProperties() {
		return (List<GwtScriptProperty>) (List<?>) super.getProperties();
	}

	public int getTypeId() {
		return typeId;
	}

	@Override
	public boolean isInstance(Object value) {
		CastableTypeMap typeMap = ScriptOnlyItwTools.getCastableTypeMap(value);
		return typeMap != null && typeMap.instanceOf(typeId);
	}
}

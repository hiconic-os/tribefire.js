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
package com.braintribe.model.processing.meta.editor;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.data.MetaData;
import com.braintribe.model.meta.info.GmEntityTypeInfo;
import com.braintribe.model.meta.info.GmPropertyInfo;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

/**
 * @author peter.gazdik
 */
@JsType (namespace = GmCoreApiInteropNamespaces.metadata)
@SuppressWarnings("unusable-by-js")
public interface EntityTypeMetaDataEditor {

	/** return the underlying {@link GmEntityType} */
	GmEntityType getEntityType();
	
	/** Configures MD for current entity type. */
	EntityTypeMetaDataEditor configure(Consumer<GmEntityTypeInfo> consumer);

	/** Configures MD for given property. */
	@JsMethod (name = "configureByName")
	EntityTypeMetaDataEditor configure(String propertyName, Consumer<GmPropertyInfo> consumer);

	/** Configures MD for given property. */
	@JsMethod (name = "configureByProperty")
	EntityTypeMetaDataEditor configure(Property property, Consumer<GmPropertyInfo> consumer);

	/** Configures MD for given property. */
	@JsMethod (name = "configureByPropertyInfo")
	EntityTypeMetaDataEditor configure(GmPropertyInfo gmPropertyInfo, Consumer<GmPropertyInfo> consumer);

	/** Adds MD to {@link GmEntityTypeInfo#getMetaData()} */
	EntityTypeMetaDataEditor addMetaData(MetaData... mds);

	/** Removes MD from {@link GmEntityTypeInfo#getMetaData()} */
	EntityTypeMetaDataEditor removeMetaData(Predicate<? super MetaData> filter);

	/** Adds MD to {@link GmEntityTypeInfo#getPropertyMetaData()} */
	EntityTypeMetaDataEditor addPropertyMetaData(MetaData... mds);

	/** Removes MD from {@link GmEntityTypeInfo#getPropertyMetaData()} */
	EntityTypeMetaDataEditor removePropertyMetaData(Predicate<? super MetaData> filter);

	/** Adds MD to {@link GmPropertyInfo#getMetaData()}, for property/propertyOverride of this entity. */
	@JsMethod (name = "addPropertyMetaDataByName")
	EntityTypeMetaDataEditor addPropertyMetaData(String propertyName, MetaData... mds);

	/** Removes MD from {@link GmPropertyInfo#getMetaData()}, for property/propertyOverride of this entity. */
	@JsMethod (name = "removePropertyMetaDataByName")
	EntityTypeMetaDataEditor removePropertyMetaData(String propertyName, Predicate<? super MetaData> filter);

	/** Similar to {@link #addPropertyMetaData(String, MetaData...)} */
	@JsMethod (name = "addPropertyMetaDataByProperty")
	EntityTypeMetaDataEditor addPropertyMetaData(Property property, MetaData... mds);

	/** Similar to {@link #removePropertyMetaData(String, Predicate)} */
	@JsMethod (name = "removePropertyMetaDataByProperty")
	EntityTypeMetaDataEditor removePropertyMetaData(Property property, Predicate<? super MetaData> filter);

	/** Similar to {@link #addPropertyMetaData(String, MetaData...)} */
	@JsMethod (name = "addPropertyMetaDataByPropertyInfo")
	EntityTypeMetaDataEditor addPropertyMetaData(GmPropertyInfo gmPropertyInfo, MetaData... mds);

	/** Similar to {@link #removePropertyMetaData(String, Predicate)} */
	@JsMethod (name = "removePropertyMetaDataByPropertyInfo")
	EntityTypeMetaDataEditor removePropertyMetaData(GmPropertyInfo gmPropertyInfo, Predicate<? super MetaData> filter);

}

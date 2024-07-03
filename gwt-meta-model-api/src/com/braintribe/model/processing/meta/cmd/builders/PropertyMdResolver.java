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
package com.braintribe.model.processing.meta.cmd.builders;

import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.meta.data.MetaData;
import com.braintribe.model.processing.meta.cmd.result.PropertyMdResult;

import jsinterop.annotations.JsType;

/**
 * Resolver for meta data defined on {@link GmProperty} level.
 */
@JsType (namespace = GmCoreApiInteropNamespaces.metadata)
@SuppressWarnings("unusable-by-js")
public interface PropertyMdResolver extends EntityRelatedMdResolver<PropertyMdResolver> {

	GmProperty getGmProperty();

	@Override
	<M extends MetaData> PropertyMdResult<M> meta(EntityType<M> metaDataType);
}

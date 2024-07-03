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
package com.braintribe.model.processing.service.common.context;

import java.util.function.Supplier;

import com.braintribe.model.processing.service.api.ServiceRequestContext;
import com.braintribe.model.processing.service.api.ServiceRequestContextManager;
import com.braintribe.utils.collection.impl.AttributeContexts;

/**
 * <p>
 * A standard {@link ServiceRequestContextManager} which complies with the {@link Supplier} interface, providing the
 * current {@link ServiceRequestContext} also through {@link #get()}.
 * 
 * @deprecated use {@link AttributeContexts} instead 
 */
@Deprecated
public class StandardServiceRequestContextManager extends ThreadLocalServiceRequestContextStack implements ServiceRequestContextManager {
	// noop
}

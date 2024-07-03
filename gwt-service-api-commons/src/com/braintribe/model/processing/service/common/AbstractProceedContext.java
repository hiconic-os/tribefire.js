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
package com.braintribe.model.processing.service.common;

import com.braintribe.model.processing.service.api.ProceedContext;
import com.braintribe.model.processing.service.api.ServiceRequestContext;

/**
 * <p>
 * Base class for {@link ProceedContext} implementations.
 * 
 */
public abstract class AbstractProceedContext implements ProceedContext {

	//private static final Logger log = Logger.getLogger(AbstractProceedContext.class);

	protected int index;
	protected ServiceRequestContext serviceRequestContext;

	public int currentIndex() {
		return index;
	}
}

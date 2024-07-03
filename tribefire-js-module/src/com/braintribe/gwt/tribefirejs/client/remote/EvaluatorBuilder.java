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
package com.braintribe.gwt.tribefirejs.client.remote;

import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.service.api.DomainRequest;
import com.braintribe.model.service.api.ExecuteInDomain;
import com.braintribe.model.service.api.ServiceRequest;

import jsinterop.annotations.JsType;

/**
 * @author peter.gazdik
 */
@JsType(namespace = GmCoreApiInteropNamespaces.remote)
public interface EvaluatorBuilder {

	/**
	 * Binds this domainId as the target domain for every {@link DomainRequest} with no {@link DomainRequest#getDomainId() domainId}. The binding is
	 * done using proper {@link ExecuteInDomain} wrapper.
	 */
	EvaluatorBuilder setDefaultDomain(String domainId);

	Evaluator<ServiceRequest> build();

}

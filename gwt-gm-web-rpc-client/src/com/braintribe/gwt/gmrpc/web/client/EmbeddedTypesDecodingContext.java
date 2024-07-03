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
package com.braintribe.gwt.gmrpc.web.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.braintribe.gwt.genericmodel.client.codec.api.DefaultDecodingContext;
import com.braintribe.gwt.gmrpc.api.client.user.EmbeddedRequiredTypesExpert;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.service.api.result.ServiceResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EmbeddedTypesDecodingContext extends DefaultDecodingContext {

	private Map<String, GenericModelType> types;
	
	public EmbeddedTypesDecodingContext(EmbeddedRequiredTypesExpert embeddedRequiredTypesExpert) {
		super();
		types = new HashMap<String, GenericModelType>();
		types.put(ServiceResult.T.getTypeSignature(), ServiceResult.T);
		for (GenericModelType type: embeddedRequiredTypesExpert.getMinimalTypes()) {
			types.put(type.getTypeSignature(), type);
		}
	}

	@Override
	public GenericModelType resolveType(String type) {
		return types.get(type);
	}

	@Override
	public void ensureTypes(Set<String> types, AsyncCallback<Void> callback) {
		// do nothing here
		callback.onSuccess(null);
	}

	@Override
	public boolean isLenientDecode() {
		return true;
	}
	
}

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
package com.braintribe.gwt.gmrpc.api.client.user;

import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.EvalContextAspect;

import jsinterop.annotations.JsType;

@JsType(namespace = GmCoreApiInteropNamespaces.eval)
@SuppressWarnings("unusable-by-js")
public interface RequestUploadProgressMonitor extends EvalContextAspect<RequestUploadProgressMonitor> {
	void onProgress(RequestUploadProgress progress);
	
	default void installOn(EvalContext<?> context) {
		context.setAttribute(AbstractRequestProgressUploadMonitor.class, this); 
	}
	
	static Class<RequestUploadProgressMonitor> ATTRIBUTE = RequestUploadProgressMonitor.class;
}

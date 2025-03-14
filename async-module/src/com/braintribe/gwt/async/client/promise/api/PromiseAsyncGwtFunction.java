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
package com.braintribe.gwt.async.client.promise.api;

import com.braintribe.gwt.async.client.JsInteropAsyncNamespace;
import com.braintribe.processing.async.api.AsyncCallback;

import jsinterop.annotations.JsType;

@JsType(namespace = JsInteropAsyncNamespace.async)
public interface PromiseAsyncGwtFunction<I,O> {
	void apply(I value, AsyncCallback<O> callback) throws Exception;
}

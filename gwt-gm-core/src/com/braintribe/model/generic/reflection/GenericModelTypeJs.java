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
package com.braintribe.model.generic.reflection;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsMethod;

/**
 * Some methods of our public APIs Like {@link GenericModelType#getTypeSignature()} are @JsIgnored, only to be
 * re-declared with a "Js" suffix and a @JsMethod mapping -{@link #getTypeSignatureJs()}.
 * <p>
 * This is because GWT outputs the JS interop name wherever the method is used internally, making the resulting library
 * bigger. So we want the internally used names obfuscated, and thus we {@link JsIgnore} them.
 * <p>
 * But we also want them to be exposed, so we offer new methods, not used internally, and map them to the right name.
 */
@SuppressWarnings("unusable-by-js")
public interface GenericModelTypeJs extends GenericModelType {

	@JsMethod(name = "getTypeSignature")
	default String getTypeSignatureJs() {
		return getTypeSignature();
	}

}
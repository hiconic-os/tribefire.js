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
package com.braintribe.utils.collection.impl;

import com.braintribe.common.attribute.AttributeContext;
import com.braintribe.common.attribute.AttributeContextBuilder;
import com.braintribe.common.scoping.Scoping;
import com.braintribe.utils.collection.api.Stack;

import jsinterop.annotations.JsType;
import jsinterop.context.JsInteropNamespaces;

@JsType(namespace = JsInteropNamespaces.attr)
public interface AttributeContexts {
	static AttributeContext emptyContext = new MapAttributeContext(null);
	
	static Stack<AttributeContext> stack = new AttributeContextStack();
	
	static AttributeContext empty() {
		return emptyContext;
	}
	
	static AttributeContextBuilder attributeContext() {
		return new AttributeContextBuilderImpl(new MapAttributeContext());
	}
	
	static AttributeContext peek() {
		return stack.get();
	}
	
	static AttributeContextBuilder derivePeek() {
		return peek().derive();
	}
	
	static void push(AttributeContext context) {
		stack.push(context);
	}
	
	static AttributeContext pop() {
		return stack.pop();
	}
	
	static Scoping with(AttributeContext context) {
		return Scoping.with( //
				() -> push(context), // 
				() -> pop() //
		);
	}
}

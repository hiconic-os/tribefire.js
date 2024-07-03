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
package com.braintribe.model.generic.proxy;

import java.util.List;

public class DeferredListInserter implements DeferredApplier {
	private List<Object> target;
	private int index;
	private ProxyValue value;
	
	
	
	public DeferredListInserter(List<Object> target, int index, ProxyValue proxyValue) {
		super();
		this.target = target;
		this.index = index;
		this.value = proxyValue;
	}


	@Override
	public void apply() {
		target.set(index, value.actualValue());
	}
}

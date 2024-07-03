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
package com.braintribe.gwt.codec.string.client;

import java.util.HashSet;
import java.util.Set;

import com.braintribe.codec.Codec;

public class SetCodec<T> extends AbstractCollectionCodec<Set<T>, T> {
	@SuppressWarnings({ "cast", "rawtypes" })
	public SetCodec(Codec<T, String> elementCodec) {
		super((Class<Set<T>>) (Class) Set.class, elementCodec);
	}
	
	@SuppressWarnings({ "cast", "rawtypes" })
	public SetCodec() {
		super((Class<Set<T>>) (Class) Set.class);
	}
	
	@Override
	protected Set<T> createCollection() {
		return new HashSet<T>();
	}
	
}

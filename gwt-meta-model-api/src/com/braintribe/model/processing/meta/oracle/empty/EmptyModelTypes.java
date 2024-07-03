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
package com.braintribe.model.processing.meta.oracle.empty;

import java.util.function.Predicate;
import java.util.stream.Stream;

import com.braintribe.model.generic.reflection.CustomType;
import com.braintribe.model.meta.GmCustomType;
import com.braintribe.model.processing.meta.oracle.ModelTypes;
import com.braintribe.model.processing.meta.oracle.TypeOracle;

/**
 * @author peter.gazdik
 */
@SuppressWarnings("unusable-by-js")
public class EmptyModelTypes implements ModelTypes {

	public static final EmptyModelTypes INSTANCE = new EmptyModelTypes();

	private EmptyModelTypes() {
	}

	@Override
	public ModelTypes filter(Predicate<? super GmCustomType> filter) {
		return this;
	}

	@Override
	public ModelTypes onlyDeclared() {
		return this;
	}

	@Override
	public ModelTypes onlyInherited() {
		return this;
	}

	@Override
	public ModelTypes onlyEnums() {
		return this;
	}

	@Override
	public ModelTypes onlyEntities() {
		return this;
	}

	@Override
	public <T extends GmCustomType> Stream<T> asGmTypes() {
		return Stream.empty();
	}

	@Override
	public <T extends CustomType> Stream<T> asTypes() {
		return Stream.empty();
	}

	@Override
	public <T extends TypeOracle> Stream<T> asTypeOracles() {
		return Stream.empty();
	}

}

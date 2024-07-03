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
package com.braintribe.model.processing.meta.cmd.empty;

import com.braintribe.model.generic.reflection.ScalarType;
import com.braintribe.model.generic.reflection.SimpleTypes;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.meta.cmd.MdSelectorResolver;
import com.braintribe.model.processing.meta.cmd.builders.ModelMdResolver;
import com.braintribe.model.processing.meta.oracle.ModelOracle;
import com.braintribe.model.processing.meta.oracle.empty.EmptyModelOracle;

public class EmptyCmdResolver implements CmdResolver {

	public static final EmptyCmdResolver INSTANCE = new EmptyCmdResolver();

	private EmptyCmdResolver() {
	}

	@Override
	public ModelOracle getModelOracle() {
		return EmptyModelOracle.INSTANCE;
	}

	@Override
	public ModelMdResolver getMetaData() {
		return EmptyModelMdResolver.INSTANCE;
	}

	@Override
	public MdSelectorResolver getMdSelectorResolver() {
		return EmptyMdSelectorResolver.INSTANCE;
	}

	@Override
	public <T extends ScalarType> T getIdType(String typeSignature) {
		return (T) SimpleTypes.TYPE_LONG;
	}

}

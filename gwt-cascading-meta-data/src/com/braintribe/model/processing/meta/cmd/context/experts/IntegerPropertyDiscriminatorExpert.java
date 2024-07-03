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
package com.braintribe.model.processing.meta.cmd.context.experts;

import com.braintribe.model.meta.selector.IntegerPropertyDiscriminator;
import com.braintribe.model.processing.meta.cmd.context.SelectorContext;

@SuppressWarnings("unusable-by-js")
public class IntegerPropertyDiscriminatorExpert extends SimplePropertyDiscriminatorExpert<IntegerPropertyDiscriminator, Integer> {

	public IntegerPropertyDiscriminatorExpert() {
		super(Integer.class);
	}

	@Override
	public boolean matches(IntegerPropertyDiscriminator selector, SelectorContext context) throws Exception {
		Integer actualValue = getPropertyCasted(selector, context);

		if (actualValue != null) {
			int discriminatorValue = selector.getDiscriminatorValue();
			return actualValue.intValue() == discriminatorValue;
		}

		return false;
	}

}

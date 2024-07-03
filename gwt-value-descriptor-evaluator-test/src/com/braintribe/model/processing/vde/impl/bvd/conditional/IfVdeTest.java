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
package com.braintribe.model.processing.vde.impl.bvd.conditional;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.braintribe.model.bvd.conditional.If;
import com.braintribe.model.bvd.predicate.Equal;
import com.braintribe.model.processing.vde.evaluator.impl.bvd.convert.ToStringVde;
import com.braintribe.model.processing.vde.impl.VDGenerator;
import com.braintribe.model.processing.vde.test.VdeTest;

/**
 * Provides tests for {@link ToStringVde}.
 * 
 */
public class IfVdeTest extends VdeTest {

	@Test
	public void testIfStringEqualThen() throws Exception {
		String left = "foo";
		String right = "foo";

		Equal equal = VDGenerator.$.equal(left, right);
		If _if = VDGenerator.$._if(equal, true, false);

		Object result = evaluate(_if);
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(true);
	}

	@Test
	public void testIfStringEqualElse() throws Exception {
		String left = "foo";
		String right = "foo2";

		Equal equal = VDGenerator.$.equal(left, right);
		If _if = VDGenerator.$._if(equal, true, false);

		Object result = evaluate(_if);
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(false);
	}

}

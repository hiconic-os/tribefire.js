// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.processing.vde.impl.bvd.cast;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.braintribe.model.bvd.cast.DoubleCast;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.evaluator.impl.bvd.cast.DoubleCastVde;
import com.braintribe.model.processing.vde.test.VdeTest;

/**
 * Provides tests for {@link DoubleCastVde}.
 * 
 */
public class DoubleCastVdeTest extends VdeTest {

	@Test
	public void testNumberToDoubleCast() throws Exception {

		Object[] numbers = CastUtil.getAllPossibleNumberTypesArray();
		DoubleCast cast = $.doubleCast();
		for (Object number : numbers) {

			cast.setOperand(number);
			Object result = evaluate(cast);

			assertThat(result).isNotNull();
			assertThat(result).isInstanceOf(Double.class);
		}
	}

	@Test(expected = VdeRuntimeException.class)
	public void testBooleanToDoubleCastFail() throws Exception {
		Boolean x = new Boolean(true);

		DoubleCast cast = $.doubleCast();
		cast.setOperand(x);

		evaluate(cast);
	}
}

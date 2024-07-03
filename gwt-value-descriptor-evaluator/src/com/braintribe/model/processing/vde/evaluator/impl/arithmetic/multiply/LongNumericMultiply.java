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
package com.braintribe.model.processing.vde.evaluator.impl.arithmetic.multiply;

import com.braintribe.model.bvd.math.Multiply;
import com.braintribe.model.processing.vde.evaluator.api.arithmetic.ArithmeticEvalExpert;

/**
 * Expert for {@link Multiply} that operates on first operand of type Number and
 * second operand of type Number, where internally they are converted to
 * Long
 * 
 */
public class LongNumericMultiply implements ArithmeticEvalExpert<Number, Number> {

	private static LongNumericMultiply instance = null;

	protected LongNumericMultiply() {
		// empty
	}

	public static LongNumericMultiply getInstance() {
		if (instance == null) {
			instance = new LongNumericMultiply();
		}
		return instance;
	}
	
	@Override
	public Object evaluate(Number firstOperand, Number secondOperand) {
		Long firstValue = firstOperand.longValue();
		Long secondValue = secondOperand.longValue();
		return firstValue * secondValue;
	}

}

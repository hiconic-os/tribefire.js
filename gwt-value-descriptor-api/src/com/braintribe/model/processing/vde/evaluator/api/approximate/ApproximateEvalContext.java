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
package com.braintribe.model.processing.vde.evaluator.api.approximate;

import com.braintribe.model.bvd.math.ApproximateOperation;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;

/**
 * Context that is responsible for evaluation of all possible
 * {@link ApproximateOperation}. It identifies the correct implementation of the
 * {@link ApproximateOperation} that adheres to the types of the provided
 * operands and returns the result of that implementation
 * 
 */
public interface ApproximateEvalContext {

	/**
	 * Evaluates the operands according to the {@link ApproximateOperator}. It
	 * selects the correct {@link ApproximateEvalExpert} implementation that
	 * satisfies the types of the operands and the given operator
	 * 
	 * @param firstOperand
	 *            The value that will be approximated
	 * @param secondOperand
	 *            The precision that will be used to evaluate the value
	 * @param operator
	 *            The type of approximation that will be performed
	 * @return An approximation of value with respect to the precision
	 */
	<T> T evaluate(Object firstOperand, Object secondOperand, ApproximateOperator operator) throws VdeRuntimeException;

}

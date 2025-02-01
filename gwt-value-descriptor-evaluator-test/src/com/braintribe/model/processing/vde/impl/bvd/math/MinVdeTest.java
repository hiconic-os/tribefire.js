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
package com.braintribe.model.processing.vde.impl.bvd.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.braintribe.model.bvd.math.Min;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.evaluator.impl.bvd.math.MinVde;

/**
 * Provides tests for {@link MinVde}.
 * 
 */
public class MinVdeTest extends AbstractArithmeticVdeTest {

	@Test(expected = VdeRuntimeException.class)
	public void testNullOperandsMin() throws Exception {

		Min math = $.min();

		testNullEmptyOperands(math);
	}

	@Test(expected = VdeRuntimeException.class)
	public void testEmptyOperandsMin() throws Exception {

		Min math = $.min();
		List<Object> operands = new ArrayList<Object>();
		math.setOperands(operands);

		testNullEmptyOperands(math);
	}

	@Test
	public void testSingleOperandMin() throws Exception {

		Min math = $.min();
		List<Object> operands = new ArrayList<Object>();
		operands.add(3);
		math.setOperands(operands);

		testSingleOperand(math);
	}

	@Test
	public void testIntegerMin() throws Exception {

		Min math = $.min();
		List<Object> operands = new ArrayList<Object>();
		operands.add(2);
		operands.add(3);
		operands.add(1);
		math.setOperands(operands);

		Object result = evaluate(math);
		validateIntegerResult(result, 1);
	}

	@Test
	public void testLongMin() throws Exception {

		Min math = $.min();
		List<List<Object>> operandsMin = getLongOperandsMin();

		for (List<Object> operandList : operandsMin) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateLongResult(result, 2L);
		}

		operandsMin = getLongOperandsMin();
		Collections.reverse(operandsMin);

		for (List<Object> operandList : operandsMin) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateLongResult(result, 2L);
		}
	}

	@Test
	public void testFloatMin() throws Exception {

		Min math = $.min();
		List<List<Object>> operandsMin = getFloatOperandsMin();

		for (List<Object> operandList : operandsMin) {
			math.setOperands(operandList);
			Object result = evaluate(math);
			validateFloatResult(result, 2F);
		}

		operandsMin = getFloatOperandsMin();
		Collections.reverse(operandsMin);

		for (List<Object> operandList : operandsMin) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateFloatResult(result, 2F);
		}
	}

	@Test
	public void testDoubleMin() throws Exception {

		Min math = $.min();
		List<List<Object>> operandsMin = getDoubleOperandsMin();

		for (List<Object> operandList : operandsMin) {
			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result, 2D);
		}

		operandsMin = getDoubleOperandsMin();
		Collections.reverse(operandsMin);

		for (List<Object> operandList : operandsMin) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result, 2D);
		}
	}

	@Test
	public void testDecimalMin() throws Exception {
		Min math = $.min();
		List<List<Object>> operandsMin = getDecimalOperandsMin();

		for (List<Object> operandList : operandsMin) {
			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDecimalResult(result, new BigDecimal(2));
		}

		operandsMin = getDecimalOperandsMin();
		Collections.reverse(operandsMin);

		for (List<Object> operandList : operandsMin) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDecimalResult(result, new BigDecimal(2));
		}
	}

	private List<List<Object>> getLongOperandsMin() {
		List<List<Object>> result = new ArrayList<List<Object>>();

		List<Object> list = new ArrayList<Object>();
		list.add(2L);
		list.add(3L);
		list.add(4L);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(2L);
		list.add(3);
		list.add(4L);
		result.add(list);

		return result;
	}

	private List<List<Object>> getFloatOperandsMin() {
		List<List<Object>> result = new ArrayList<List<Object>>();

		List<Object> list = new ArrayList<Object>();
		list.add(2F);
		list.add(3F);
		list.add(4F);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(2F);
		list.add(3);
		list.add(4F);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(2F);
		list.add(3L);
		list.add(4F);
		result.add(list);

		return result;
	}

	private List<List<Object>> getDoubleOperandsMin() {
		List<List<Object>> result = new ArrayList<List<Object>>();

		List<Object> list = new ArrayList<Object>();
		list.add(2D);
		list.add(3D);
		list.add(4D);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(2D);
		list.add(3);
		list.add(4D);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(2D);
		list.add(3L);
		list.add(4D);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(2D);
		list.add(3F);
		list.add(4D);
		result.add(list);

		return result;
	}

	private List<List<Object>> getDecimalOperandsMin() {
		List<List<Object>> result = new ArrayList<List<Object>>();

		List<Object> list = new ArrayList<Object>();
		list.add(new BigDecimal(2));
		list.add(new BigDecimal(3));
		list.add(new BigDecimal(4));
		result.add(list);

		list = new ArrayList<Object>();
		list.add(new BigDecimal(2));
		list.add(3);
		list.add(new BigDecimal(4));
		result.add(list);

		list = new ArrayList<Object>();
		list.add(new BigDecimal(2));
		list.add(3L);
		list.add(new BigDecimal(4));
		result.add(list);

		list = new ArrayList<Object>();
		list.add(new BigDecimal(2));
		list.add(3F);
		list.add(new BigDecimal(4));
		result.add(list);

		list = new ArrayList<Object>();
		list.add(new BigDecimal(2));
		list.add(3D);
		list.add(new BigDecimal(4));
		result.add(list);

		return result;
	}
}

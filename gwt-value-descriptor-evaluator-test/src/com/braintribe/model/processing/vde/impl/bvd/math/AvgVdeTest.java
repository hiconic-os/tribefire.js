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

import com.braintribe.model.bvd.math.Avg;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.evaluator.impl.bvd.math.AvgVde;

/**
 * Provides tests for {@link AvgVde}.
 * 
 */
public class AvgVdeTest extends AbstractArithmeticVdeTest {

	@Test(expected = VdeRuntimeException.class)
	public void testNullOperandsAvg() throws Exception {

		Avg math = $.avg();

		testNullEmptyOperands(math);
	}

	@Test(expected = VdeRuntimeException.class)
	public void testEmptyOperandsAvg() throws Exception {

		Avg math = $.avg();
		List<Object> operands = new ArrayList<Object>();
		math.setOperands(operands);

		testNullEmptyOperands(math);
	}

	@Test
	public void testSingleOperandAvg() throws Exception {

		Avg math = $.avg();
		List<Object> operands = new ArrayList<Object>();
		operands.add(3D);
		math.setOperands(operands);

		testSingleOperand(math);
	}

	@Test
	public void testIntegerAvg() throws Exception {

		Avg math = $.avg();
		List<Object> operands = new ArrayList<Object>();
		operands.add(2);
		operands.add(3);
		operands.add(1);
		math.setOperands(operands);

		Object result = evaluate(math);
		validateDoubleResult(result, 2D);
	}

	@Test
	public void testLongAvg() throws Exception {

		Avg math = $.avg();
		List<List<Object>> operandsAvg = getLongOperandsAvg();

		for (List<Object> operandList : operandsAvg) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result, 3D);
		}

		operandsAvg = getLongOperandsAvg();
		Collections.reverse(operandsAvg);

		for (List<Object> operandList : operandsAvg) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result, 3D);
		}
	}

	@Test
	public void testFloatAvg() throws Exception {

		Avg math = $.avg();
		List<List<Object>> operandsAvg = getFloatOperandsAvg();

		for (List<Object> operandList : operandsAvg) {
			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result, 3D);
		}

		operandsAvg = getFloatOperandsAvg();
		Collections.reverse(operandsAvg);

		for (List<Object> operandList : operandsAvg) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result, 3D);
		}
	}

	@Test
	public void testDoubleAvg() throws Exception {

		Avg math = $.avg();
		List<List<Object>> operandsAvg = getDoubleOperandsAvg();

		for (List<Object> operandList : operandsAvg) {
			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result, 3D);
		}

		operandsAvg = getDoubleOperandsAvg();
		Collections.reverse(operandsAvg);

		for (List<Object> operandList : operandsAvg) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result, 3D);
		}
	}

	@Test
	public void testDecimalAvg() throws Exception {
		Avg math = $.avg();
		List<List<Object>> operandsAvg = getDecimalOperandsAvg();

		for (List<Object> operandList : operandsAvg) {
			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDecimalResult(result, new BigDecimal(3));
		}

		operandsAvg = getDecimalOperandsAvg();
		Collections.reverse(operandsAvg);

		for (List<Object> operandList : operandsAvg) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDecimalResult(result, new BigDecimal(3));
		}
	}

	private List<List<Object>> getLongOperandsAvg() {
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

	private List<List<Object>> getFloatOperandsAvg() {
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

	private List<List<Object>> getDoubleOperandsAvg() {
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

	private List<List<Object>> getDecimalOperandsAvg() {
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

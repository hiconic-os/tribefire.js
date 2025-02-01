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

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.braintribe.model.bvd.math.Divide;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.evaluator.impl.bvd.math.DivideVde;
import com.braintribe.model.time.TimeSpan;
import com.braintribe.model.time.TimeUnit;

/**
 * Provides tests for {@link DivideVde}.
 * 
 */
public class DivideVdeTest extends AbstractArithmeticVdeTest {

	@Test(expected = VdeRuntimeException.class)
	public void testNullOperandsDivide() throws Exception {

		Divide math = $.divide();

		testNullEmptyOperands(math);
	}

	@Test(expected = VdeRuntimeException.class)
	public void testEmptyOperandsDivide() throws Exception {

		Divide math = $.divide();
		List<Object> operands = new ArrayList<Object>();
		math.setOperands(operands);

		testNullEmptyOperands(math);
	}

	@Test
	public void testSingleOperandDivide() throws Exception {

		Divide math = $.divide();
		List<Object> operands = new ArrayList<Object>();
		operands.add(3);
		math.setOperands(operands);

		testSingleOperand(math);
	}

	@Test
	public void testIntegerDivide() throws Exception {

		Divide math = $.divide();
		List<Object> operands = new ArrayList<Object>();
		operands.add(50);
		operands.add(10);
		math.setOperands(operands);

		Object result = evaluate(math);
		validateIntegerResult(result);
	}

	@Test
	public void testLongDivide() throws Exception {

		Divide math = $.divide();
		List<List<Object>> operandsDivide = getLongOperandsDivide();

		for (List<Object> operandList : operandsDivide) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateLongResult(result);
		}

		operandsDivide = getLongOperandsDivide();
		Collections.reverse(operandsDivide);

		for (List<Object> operandList : operandsDivide) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateLongResult(result);
		}
	}

	@Test
	public void testFloatDivide() throws Exception {

		Divide math = $.divide();
		List<List<Object>> operandsDivide = getFloatOperandsDivide();

		for (List<Object> operandList : operandsDivide) {
			math.setOperands(operandList);
			Object result = evaluate(math);
			validateFloatResult(result);
		}

		operandsDivide = getFloatOperandsDivide();
		Collections.reverse(operandsDivide);

		for (List<Object> operandList : operandsDivide) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateFloatResult(result);
		}
	}

	@Test
	public void testDoubleDivide() throws Exception {

		Divide math = $.divide();
		List<List<Object>> operandsDivide = getDoubleOperandsDivide();

		for (List<Object> operandList : operandsDivide) {
			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result);
		}

		operandsDivide = getDoubleOperandsDivide();
		Collections.reverse(operandsDivide);

		for (List<Object> operandList : operandsDivide) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDoubleResult(result);
		}
	}

	@Test
	public void testDecimalDivide() throws Exception {
		Divide math = $.divide();
		List<List<Object>> operandsDivide = getDecimalOperandsDivide();

		for (List<Object> operandList : operandsDivide) {
			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDecimalResult(result);
		}

		operandsDivide = getDecimalOperandsDivide();
		Collections.reverse(operandsDivide);

		for (List<Object> operandList : operandsDivide) {

			math.setOperands(operandList);
			Object result = evaluate(math);
			validateDecimalResult(result);
		}
	}

	@Test
	public void testTimeSpanDivideNumber() throws Exception {

		Divide math = $.divide();
		Object[] multiples = getTimeSpanMultipliers();

		int length = TimeUnit.values().length;

		for (int i = multiples.length - 1; i > 0; i--) {

			Object number = multiples[i];
			for (int j = 0; j < length; j++) {

				TimeUnit timeUnit = TimeUnit.values()[j];
				if (timeUnit == TimeUnit.planckTime) {
					continue;
				}
				TimeSpan span = TimeSpan.T.create();
				span.setUnit(timeUnit);
				span = getSpanforTimeSpanDivide(span, 4.0);

				List<Object> operands = new ArrayList<Object>();
				operands.add(span);
				operands.add(number);
				math.setOperands(operands);

				Object result = evaluate(math);
				validateTimeSpanDivide(timeUnit, result);
			}
		}
	}

	@Test(expected = VdeRuntimeException.class)
	public void testNumberDivideTimeSpanFail() throws Exception {

		Divide math = $.divide();

		TimeSpan span = TimeSpan.T.create();
		span.setUnit(TimeUnit.hour);
		span.setValue(3);

		List<Object> operands = new ArrayList<Object>(); // this order is not allowed
		operands.add(2);
		operands.add(span);
		math.setOperands(operands);

		evaluate(math);
	}

	private Object[] getTimeSpanMultipliers() {
		Object[] result = new Object[5];
		result[0] = 2;
		result[1] = 2L;
		result[2] = 2F;
		result[3] = 2D;
		result[4] = new BigDecimal(2);
		return result;
	}

	private void validateTimeSpanDivide(TimeUnit targetTimeUnit, Object result) {
		assertThat(result).isNotNull();
		assertThat(result).isInstanceOf(TimeSpan.class);
		TimeSpan span = (TimeSpan) result;

		assertThat(span.getUnit()).isEqualTo(targetTimeUnit);
		double value = span.getValue();
		switch (span.getUnit()) {
			case day:
				assertThat(value).isEqualTo(2);
				break;
			case hour:
				assertThat(value).isEqualTo(48);
				break;
			case minute:
				assertThat(value).isEqualTo(2880);
				break;
			case second:
				assertThat(value).isEqualTo(172800);
				break;
			case milliSecond:
				assertThat(value).isEqualTo(1.728E8);
				break;
			case microSecond:
				assertThat(value).isEqualTo(1.728E11);
				break;
			case nanoSecond:
				assertThat(value).isEqualTo(1.728E14);
				break;
			case planckTime:
				// just for fun
				break;
		}

	}

	private TimeSpan getSpanforTimeSpanDivide(TimeSpan span, double factor) {
		switch (span.getUnit()) {
			case day:
				span.setValue(1.0);
				break;
			case hour:
				span.setValue(24.0);
				break;
			case minute:
				span.setValue(24.0 * 60.0);
				break;
			case second:
				span.setValue(24.0 * 60.0 * 60.0);
				break;
			case milliSecond:
				span.setValue(24.0 * 60.0 * 60.0 * 1000.0);
				break;
			case microSecond:
				span.setValue(24.0 * 60.0 * 60.0 * 1000000.0);
				break;
			case nanoSecond:
				span.setValue(24.0 * 60.0 * 60.0 * 1000000000.0);
				break;
			case planckTime:
				span.setValue(0); // just for fun
				break;
		}
		span.setValue(span.getValue() * factor);
		return span;
	}

	private List<List<Object>> getLongOperandsDivide() {
		List<List<Object>> result = new ArrayList<List<Object>>();

		List<Object> list = new ArrayList<Object>();
		list.add(50L);
		list.add(10L);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(50L);
		list.add(10);
		result.add(list);

		return result;
	}

	private List<List<Object>> getFloatOperandsDivide() {
		List<List<Object>> result = new ArrayList<List<Object>>();

		List<Object> list = new ArrayList<Object>();
		list.add(50F);
		list.add(10F);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(50F);
		list.add(10);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(50F);
		list.add(10L);
		result.add(list);

		return result;
	}

	private List<List<Object>> getDoubleOperandsDivide() {
		List<List<Object>> result = new ArrayList<List<Object>>();

		List<Object> list = new ArrayList<Object>();
		list.add(50D);
		list.add(10D);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(50D);
		list.add(10);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(50D);
		list.add(10L);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(50D);
		list.add(10F);
		result.add(list);

		return result;
	}

	private List<List<Object>> getDecimalOperandsDivide() {
		List<List<Object>> result = new ArrayList<List<Object>>();

		List<Object> list = new ArrayList<Object>();
		list.add(new BigDecimal(50));
		list.add(new BigDecimal(10));
		result.add(list);

		list = new ArrayList<Object>();
		list.add(new BigDecimal(50));
		list.add(10);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(new BigDecimal(50));
		list.add(10L);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(new BigDecimal(50));
		list.add(10F);
		result.add(list);

		list = new ArrayList<Object>();
		list.add(new BigDecimal(50));
		list.add(10D);
		result.add(list);

		return result;
	}
}

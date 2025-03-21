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
package com.braintribe.model.processing.vde.impl.bvd.predicate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.braintribe.model.bvd.predicate.GreaterOrEqual;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.impl.misc.Name;
import com.braintribe.model.processing.vde.impl.misc.SalaryRange;
import com.braintribe.model.processing.vde.impl.misc.SizeRange;

public class GreaterOrEqualVdeTest extends AbstractPredicateVdeTest {

	@Test
	public void testObjectComparableGreaterOrEqual() throws Exception {

		GreaterOrEqual predicate = $.greaterOrEqual();
		Name n1 = Name.T.create();
		n1.setFirst("c");
		Name n2 = Name.T.create();
		n2.setFirst("c");

		predicate.setLeftOperand(n1);
		predicate.setRightOperand(n2);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		n2.setFirst("w");

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test(expected = VdeRuntimeException.class)
	public void testEnumDifferentTypeGreaterOrEqual() throws Exception {

		GreaterOrEqual predicate = $.greaterOrEqual();
		predicate.setLeftOperand(SizeRange.medium);
		predicate.setRightOperand(SalaryRange.medium);

		evaluate(predicate);

	}

	@Test
	public void testEnumGreaterOrEqual() throws Exception {

		GreaterOrEqual predicate = $.greaterOrEqual();
		predicate.setLeftOperand(SalaryRange.medium);
		predicate.setRightOperand(SalaryRange.low);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(SalaryRange.medium);
		result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(SalaryRange.high);
		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testStringGreaterOrEqual() throws Exception {

		GreaterOrEqual predicate = $.greaterOrEqual();
		predicate.setLeftOperand(new String("abcd"));
		predicate.setRightOperand(new String("abc"));

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(new String("abcd"));
		result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(new String("dcba"));
		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testDateGreaterOrEqual() throws Exception {
		Calendar cal = Calendar.getInstance();

		Date date = cal.getTime();
		cal.set(Calendar.YEAR, 2011);
		Date otherDate = cal.getTime();

		GreaterOrEqual predicate = $.greaterOrEqual();
		predicate.setLeftOperand(date);
		predicate.setRightOperand(otherDate);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(date);
		result = evaluate(predicate);
		validatePositiveResult(result);

		cal.set(Calendar.YEAR, 3010);
		Date anotherDate = cal.getTime();
		predicate.setRightOperand(anotherDate);

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testIntegerGreaterOrEqual() throws Exception {

		GreaterOrEqual predicate = $.greaterOrEqual();
		predicate.setLeftOperand(3);
		predicate.setRightOperand(2);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(3);
		result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(4);
		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testLongGreaterOrEqual() throws Exception {

		GreaterOrEqual predicate = $.greaterOrEqual();

		predicate.setLeftOperand(3L);
		List<Object> operandsList = getLongOperandsGreaterOrEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}

		// commutative
		predicate.setRightOperand(4L);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}

		// equal
		predicate.setRightOperand(2L);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}
	}

	@Test
	public void testFloatGreaterOrEqual() throws Exception {

		GreaterOrEqual predicate = $.greaterOrEqual();

		predicate.setLeftOperand(3F);
		List<Object> operandsList = getFloatOperandsGreaterOrEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}

		// commutative
		predicate.setRightOperand(4F);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}

		// equal
		predicate.setRightOperand(2F);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}
	}

	@Test
	public void testDoubleGreaterOrEqual() throws Exception {

		GreaterOrEqual predicate = $.greaterOrEqual();

		predicate.setLeftOperand(3D);
		List<Object> operandsList = getDoubleOperandsGreaterOrEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}

		// commutative
		predicate.setRightOperand(4D);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}

		// equal
		predicate.setRightOperand(2D);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}
	}

	@Test
	public void testDecimalGreaterOrEqual() throws Exception {

		GreaterOrEqual predicate = $.greaterOrEqual();

		predicate.setLeftOperand(new BigDecimal(3));
		List<Object> operandsList = getDecimalOperandsGreaterOrEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}

		// commutative
		predicate.setRightOperand(new BigDecimal(4));
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}

		// equal
		predicate.setRightOperand(new BigDecimal(2));
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}
	}

	private List<Object> getDecimalOperandsGreaterOrEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(new BigDecimal(2));
		result.add(2D);
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getDoubleOperandsGreaterOrEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(2D);
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getFloatOperandsGreaterOrEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getLongOperandsGreaterOrEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(2L);
		result.add(2);
		return result;
	}

}

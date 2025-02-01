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

import com.braintribe.model.bvd.predicate.NotEqual;
import com.braintribe.model.processing.vde.impl.misc.Name;
import com.braintribe.model.processing.vde.impl.misc.SalaryRange;

public class NotEqualVdeTest extends AbstractPredicateVdeTest {

	@Test
	public void testObjectComparableNotEqual() throws Exception {

		NotEqual predicate = $.notEqual();
		Name n1 = Name.T.create();
		n1.setFirst("c");
		Name n2 = Name.T.create();
		n2.setFirst("c");

		predicate.setLeftOperand(n1);
		predicate.setRightOperand(n2);

		Object result = evaluate(predicate);
		validateNegativeResult(result);

		n2.setFirst("w");

		result = evaluate(predicate);
		validatePositiveResult(result);

	}

	@Test
	public void testEnumNotEqual() throws Exception {

		NotEqual predicate = $.notEqual();
		predicate.setLeftOperand(SalaryRange.medium);
		predicate.setRightOperand(SalaryRange.medium);

		Object result = evaluate(predicate);
		validateNegativeResult(result);

		predicate.setRightOperand(SalaryRange.high);

		result = evaluate(predicate);
		validatePositiveResult(result);

	}

	@Test
	public void testStringNotEqual() throws Exception {

		NotEqual predicate = $.notEqual();
		predicate.setLeftOperand(new String("abcd"));
		predicate.setRightOperand(new String("abcd"));

		Object result = evaluate(predicate);
		validateNegativeResult(result);

		predicate.setRightOperand(new String("dcba"));

		result = evaluate(predicate);
		validatePositiveResult(result);

	}

	@Test
	public void testDateNotEqual() throws Exception {
		Calendar cal = Calendar.getInstance();

		Date date = cal.getTime();
		NotEqual predicate = $.notEqual();
		predicate.setLeftOperand(date);
		predicate.setRightOperand(date);

		Object result = evaluate(predicate);
		validateNegativeResult(result);

		cal.set(Calendar.YEAR, 2010);
		Date otherDate = cal.getTime();
		predicate.setRightOperand(otherDate);

		result = evaluate(predicate);
		validatePositiveResult(result);

	}

	@Test
	public void testBooleanNotEqual() throws Exception {

		NotEqual predicate = $.notEqual();
		predicate.setLeftOperand(Boolean.TRUE);
		predicate.setRightOperand(Boolean.TRUE);

		Object result = evaluate(predicate);
		validateNegativeResult(result);

		predicate.setRightOperand(Boolean.FALSE);

		result = evaluate(predicate);
		validatePositiveResult(result);

	}

	@Test
	public void testIntegerNotEqual() throws Exception {

		NotEqual predicate = $.notEqual();
		predicate.setLeftOperand(2);
		predicate.setRightOperand(2);

		Object result = evaluate(predicate);
		validateNegativeResult(result);

		predicate.setRightOperand(4);

		result = evaluate(predicate);
		validatePositiveResult(result);

	}

	@Test
	public void testLongNotEqual() throws Exception {

		NotEqual predicate = $.notEqual();

		predicate.setLeftOperand(2L);
		List<Object> operandsList = getLongOperandsNotEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}

		// commutative
		predicate.setRightOperand(3L);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}
	}

	@Test
	public void testFloatNotEqual() throws Exception {

		NotEqual predicate = $.notEqual();

		predicate.setLeftOperand(2F);
		List<Object> operandsList = getFloatOperandsNotEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}

		// commutative
		predicate.setRightOperand(3F);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}
	}

	@Test
	public void testDoubleNotEqual() throws Exception {

		NotEqual predicate = $.notEqual();

		predicate.setLeftOperand(2D);
		List<Object> operandsList = getDoubleOperandsNotEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}

		// commutative
		predicate.setRightOperand(3D);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}
	}

	@Test
	public void testDecimalNotEqual() throws Exception {

		NotEqual predicate = $.notEqual();

		predicate.setLeftOperand(new BigDecimal(2));
		List<Object> operandsList = getDecimalOperandsNotEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}

		// commutative
		predicate.setRightOperand(new BigDecimal(3));
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}
	}

	private List<Object> getDecimalOperandsNotEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(new BigDecimal(2));
		result.add(2D);
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getDoubleOperandsNotEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(2D);
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getFloatOperandsNotEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getLongOperandsNotEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(2L);
		result.add(2);
		return result;
	}

}

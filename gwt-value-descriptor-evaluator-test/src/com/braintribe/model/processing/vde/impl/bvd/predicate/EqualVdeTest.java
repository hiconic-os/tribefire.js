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

import com.braintribe.model.bvd.predicate.Equal;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.impl.misc.Name;
import com.braintribe.model.processing.vde.impl.misc.Person;
import com.braintribe.model.processing.vde.impl.misc.SalaryRange;
import com.braintribe.model.processing.vde.impl.misc.SizeRange;

public class EqualVdeTest extends AbstractPredicateVdeTest {

	@Test
	public void testObjectComparableEqual() throws Exception {

		Equal predicate = $.equal();
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
	public void testNullRightOperandEqual() throws Exception {

		Equal predicate = $.equal();
		predicate.setLeftOperand(Person.T.create());

		evaluate(predicate);

	}

	@Test(expected = VdeRuntimeException.class)
	public void testNullLeftOperandEqual() throws Exception {

		Equal predicate = $.equal();
		predicate.setRightOperand(Person.T.create());

		evaluate(predicate);

	}

	@Test(expected = VdeRuntimeException.class)
	public void testRandomEqual() throws Exception {

		Equal predicate = $.equal();
		predicate.setLeftOperand(new Date());
		predicate.setRightOperand(Person.T.create());

		evaluate(predicate);

	}

	@Test
	public void testDateStringEqual() throws Exception {

		Equal predicate = $.equal();
		predicate.setLeftOperand(new Date());
		predicate.setRightOperand("date");

		Object result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testEnumDifferentTypeEqual() throws Exception {

		Equal predicate = $.equal();
		predicate.setLeftOperand(SizeRange.medium);
		predicate.setRightOperand(SalaryRange.medium);

		Object result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testEnumEqual() throws Exception {

		Equal predicate = $.equal();
		predicate.setLeftOperand(SalaryRange.medium);
		predicate.setRightOperand(SalaryRange.medium);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(SalaryRange.high);

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testStringEqual() throws Exception {

		Equal predicate = $.equal();
		predicate.setLeftOperand(new String("abcd"));
		predicate.setRightOperand(new String("abcd"));

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(new String("dcba"));

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testDateEqual() throws Exception {
		Calendar cal = Calendar.getInstance();

		Date date = cal.getTime();
		Equal predicate = $.equal();
		predicate.setLeftOperand(date);
		predicate.setRightOperand(date);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		cal.set(Calendar.YEAR, 2010);
		Date otherDate = cal.getTime();
		predicate.setRightOperand(otherDate);

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testBooleanEqual() throws Exception {

		Equal predicate = $.equal();
		predicate.setLeftOperand(Boolean.TRUE);
		predicate.setRightOperand(Boolean.TRUE);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(Boolean.FALSE);

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testIntegerEqual() throws Exception {

		Equal predicate = $.equal();
		predicate.setLeftOperand(2);
		predicate.setRightOperand(2);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(4);

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testLongEqual() throws Exception {

		Equal predicate = $.equal();

		predicate.setLeftOperand(2L);
		List<Object> operandsList = getLongOperandsEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}

		// commutative
		predicate.setRightOperand(3L);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}
	}

	@Test
	public void testFloatEqual() throws Exception {

		Equal predicate = $.equal();

		predicate.setLeftOperand(2F);
		List<Object> operandsList = getFloatOperandsEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}

		// commutative
		predicate.setRightOperand(3F);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}
	}

	@Test
	public void testDoubleEqual() throws Exception {

		Equal predicate = $.equal();

		predicate.setLeftOperand(2D);
		List<Object> operandsList = getDoubleOperandsEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}

		// commutative
		predicate.setRightOperand(3D);
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}
	}

	@Test
	public void testDecimalEqual() throws Exception {

		Equal predicate = $.equal();

		predicate.setLeftOperand(new BigDecimal(2));
		List<Object> operandsList = getDecimalOperandsEqual();
		for (Object object : operandsList) {
			predicate.setRightOperand(object);
			Object result = evaluate(predicate);
			validatePositiveResult(result);
		}

		// commutative
		predicate.setRightOperand(new BigDecimal(3));
		for (Object object : operandsList) {
			predicate.setLeftOperand(object);
			Object result = evaluate(predicate);
			validateNegativeResult(result);
		}
	}

	private List<Object> getDecimalOperandsEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(new BigDecimal(2));
		result.add(2D);
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getDoubleOperandsEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(2D);
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getFloatOperandsEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getLongOperandsEqual() {
		List<Object> result = new ArrayList<Object>();
		result.add(2L);
		result.add(2);
		return result;
	}

}

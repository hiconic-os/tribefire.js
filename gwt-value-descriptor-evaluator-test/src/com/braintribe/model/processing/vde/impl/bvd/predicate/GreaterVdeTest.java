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

import com.braintribe.model.bvd.predicate.Greater;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.impl.misc.Child;
import com.braintribe.model.processing.vde.impl.misc.Name;
import com.braintribe.model.processing.vde.impl.misc.Person;
import com.braintribe.model.processing.vde.impl.misc.SalaryRange;
import com.braintribe.model.processing.vde.impl.misc.SizeRange;

public class GreaterVdeTest extends AbstractPredicateVdeTest {

	@Test
	public void testObjectComparableGreater() throws Exception {

		Greater predicate = $.greater();
		Name n1 = Name.T.create();
		n1.setFirst("c");
		Name n2 = Name.T.create();
		n2.setFirst("b");

		predicate.setLeftOperand(n1);
		predicate.setRightOperand(n2);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		n2.setFirst("d");

		result = evaluate(predicate);
		validateNegativeResult(result);
	}

	@Test(expected = VdeRuntimeException.class)
	public void testObjectNotComparableToEachOtherGreater() throws Exception {

		Greater predicate = $.greater();
		Name n = Name.T.create();
		n.setFirst("c");
		Person p = Person.T.create();

		predicate.setLeftOperand(n);
		predicate.setRightOperand(p);

		evaluate(predicate);
	}

	@Test(expected = VdeRuntimeException.class)
	public void testObjectNotComparableGreater() throws Exception {

		Greater predicate = $.greater();
		Name n = Name.T.create();
		n.setFirst("c");
		Child c = Child.T.create();

		predicate.setLeftOperand(n);
		predicate.setRightOperand(c);

		evaluate(predicate);
	}

	@Test(expected = VdeRuntimeException.class)
	public void testEnumDifferentTypeGreater() throws Exception {

		Greater predicate = $.greater();
		predicate.setLeftOperand(SizeRange.medium);
		predicate.setRightOperand(SalaryRange.medium);

		evaluate(predicate);

	}

	@Test
	public void testEnumGreater() throws Exception {

		Greater predicate = $.greater();
		predicate.setLeftOperand(SalaryRange.medium);
		predicate.setRightOperand(SalaryRange.low);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(SalaryRange.high);

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testStringGreater() throws Exception {

		Greater predicate = $.greater();
		predicate.setLeftOperand(new String("abcd"));
		predicate.setRightOperand(new String("abc"));

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(new String("dcba"));

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testDateGreater() throws Exception {
		Calendar cal = Calendar.getInstance();

		Date date = cal.getTime();
		cal.set(Calendar.YEAR, 2011);
		Date otherDate = cal.getTime();

		Greater predicate = $.greater();
		predicate.setLeftOperand(date);
		predicate.setRightOperand(otherDate);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		cal.set(Calendar.YEAR, 3010);
		Date anotherDate = cal.getTime();
		predicate.setRightOperand(anotherDate);

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testIntegerGreater() throws Exception {

		Greater predicate = $.greater();
		predicate.setLeftOperand(3);
		predicate.setRightOperand(2);

		Object result = evaluate(predicate);
		validatePositiveResult(result);

		predicate.setRightOperand(4);

		result = evaluate(predicate);
		validateNegativeResult(result);

	}

	@Test
	public void testLongGreater() throws Exception {

		Greater predicate = $.greater();

		predicate.setLeftOperand(3L);
		List<Object> operandsList = getLongOperandsGreater();
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
	}

	@Test
	public void testFloatGreater() throws Exception {

		Greater predicate = $.greater();

		predicate.setLeftOperand(3F);
		List<Object> operandsList = getFloatOperandsGreater();
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
	}

	@Test
	public void testDoubleGreater() throws Exception {

		Greater predicate = $.greater();

		predicate.setLeftOperand(3D);
		List<Object> operandsList = getDoubleOperandsGreater();
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
	}

	@Test
	public void testDecimalGreater() throws Exception {

		Greater predicate = $.greater();

		predicate.setLeftOperand(new BigDecimal(3));
		List<Object> operandsList = getDecimalOperandsGreater();
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
	}

	private List<Object> getDecimalOperandsGreater() {
		List<Object> result = new ArrayList<Object>();
		result.add(new BigDecimal(2));
		result.add(2D);
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getDoubleOperandsGreater() {
		List<Object> result = new ArrayList<Object>();
		result.add(2D);
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getFloatOperandsGreater() {
		List<Object> result = new ArrayList<Object>();
		result.add(2F);
		result.add(2L);
		result.add(2);
		return result;
	}

	private List<Object> getLongOperandsGreater() {
		List<Object> result = new ArrayList<Object>();
		result.add(2L);
		result.add(2);
		return result;
	}

}

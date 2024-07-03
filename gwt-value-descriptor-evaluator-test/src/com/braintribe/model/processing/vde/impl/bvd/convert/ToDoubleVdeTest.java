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
package com.braintribe.model.processing.vde.impl.bvd.convert;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import com.braintribe.model.bvd.convert.ToDouble;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.evaluator.impl.bvd.convert.ToDoubleVde;
import com.braintribe.model.processing.vde.test.VdeTest;

/**
 * Provides tests for {@link ToDoubleVde}.
 * 
 */
public class ToDoubleVdeTest extends VdeTest {

	@Test
	public void testStringOperandNullFormatToDoubleConvert() throws Exception {

		ToDouble convert = $.toDouble();
		convert.setOperand("4");

		Object result = evaluate(convert);
		assertThat(result).isNotNull();
		assertThat(result).isInstanceOf(Double.class);
		assertThat(result).isEqualTo(new Double(4));
	}

	@Test(expected = VdeRuntimeException.class)
	public void testStringOperandRandomFormatToDoubleConvertFormatFail() throws Exception {

		ToDouble convert = $.toDouble();
		convert.setOperand("4");
		convert.setFormat(" "); // random format

		evaluate(convert);
	}

	@Test
	public void testBooleanOperandNullFormatToDoubleConvert() throws Exception {

		ToDouble convert = $.toDouble();
		convert.setOperand(true);

		Object result = evaluate(convert);
		assertThat(result).isNotNull();
		assertThat(result).isInstanceOf(Double.class);
		assertThat(result).isEqualTo(new Double(1));

		convert.setOperand(false);

		result = evaluate(convert);
		assertThat(result).isNotNull();
		assertThat(result).isInstanceOf(Double.class);
		assertThat(result).isEqualTo(new Double(0));
	}

	@Test(expected = VdeRuntimeException.class)
	public void testRandomOperandNullFormatToDoubleConvertTypeFail() throws Exception {

		ToDouble convert = $.toDouble();
		convert.setOperand(new Date()); // only string, boolean allowed
		evaluate(convert);
	}

}

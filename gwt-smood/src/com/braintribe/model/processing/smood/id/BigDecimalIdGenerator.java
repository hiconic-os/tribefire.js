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
package com.braintribe.model.processing.smood.id;

import java.math.BigDecimal;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.smood.IdGenerator;

public class BigDecimalIdGenerator implements IdGenerator<BigDecimal> {

	private BigDecimal maxId = BigDecimal.ZERO;

	@Override
	public BigDecimal generateId(GenericEntity entity) {
		return generateId();
	}

	protected synchronized BigDecimal generateId() {
		return maxId = maxId.add(BigDecimal.ONE);
	}

	@Override
	public synchronized void recognizeUsedId(BigDecimal id) {
		if (firstIsBiggerThanSecond(id, maxId)) {
			maxId = id;
		}
	}

	private static boolean firstIsBiggerThanSecond(BigDecimal n1, BigDecimal n2) {
		return n1.compareTo(n2) > 0;
	}
}

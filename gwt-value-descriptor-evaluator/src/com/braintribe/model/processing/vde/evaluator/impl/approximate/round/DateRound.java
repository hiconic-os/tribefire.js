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
package com.braintribe.model.processing.vde.evaluator.impl.approximate.round;

import java.util.Date;

import com.braintribe.model.bvd.math.Round;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.evaluator.api.approximate.ApproximateEvalExpert;
import com.braintribe.model.processing.vde.evaluator.impl.approximate.ApproximateVdeUtil;
import com.braintribe.model.time.DateOffset;
import com.braintribe.model.time.DateOffsetUnit;

/**
 * Expert for {@link Round} that operates on value of type Date and precision of type DateOffset 
 *
 */
public class DateRound implements ApproximateEvalExpert<Date, DateOffset> {

	private static DateRound instance = null;

	protected DateRound() {
		// empty
	}

	public static DateRound getInstance() {
		if (instance == null) {
			instance = new DateRound();
		}
		return instance;
	}

	@Override
	public Object evaluate(Date date, DateOffset offset) throws VdeRuntimeException {
		
		boolean offsetValidation = ApproximateVdeUtil.validateDateOffset(offset);
		
		if(!offsetValidation){
			throw new VdeRuntimeException("Date offset " + offset + " is not valid for an approximation");
		}

		DateOffsetUnit offsetUnit = offset.getOffset();
		int offsetValue = offset.getValue();
		
		// get original value
		int origValue = ApproximateVdeUtil.getOriginalDateComponentValue(date, offsetUnit);
		double fractionOfRealUnit = ApproximateVdeUtil.getSmallerUnitValue(date, offsetUnit);
		// reset everything lower than current threshold
		Date freshDate = ApproximateVdeUtil.resetBelowThreshold(date, offsetUnit);
		
		int round = ApproximateVdeUtil.getRound(Double.valueOf(origValue + fractionOfRealUnit), Double.valueOf(offsetValue)).intValue();
		// set updated value
		Date evaluatedDate =  ApproximateVdeUtil.setOriginalDateComponentValue(freshDate, offsetUnit, round);
		
		return evaluatedDate;
	}

}

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
package com.braintribe.model.processing.vde.evaluator.impl.bvd.time;

import java.util.Date;

import com.braintribe.model.bvd.time.Now;
import com.braintribe.model.processing.vde.evaluator.api.ValueDescriptorEvaluator;
import com.braintribe.model.processing.vde.evaluator.api.VdeContext;
import com.braintribe.model.processing.vde.evaluator.api.VdeResult;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.evaluator.api.aspects.DateAspect;
import com.braintribe.model.processing.vde.evaluator.impl.VdeResultImpl;

/**
 * {@link ValueDescriptorEvaluator} for {@link Now}
 * 
 */
public class NowVde implements ValueDescriptorEvaluator<Now> {

	@Override
	public VdeResult evaluate(VdeContext context, Now valueDescriptor) throws VdeRuntimeException {

		VdeResultImpl result = new VdeResultImpl(((valueDescriptor != null) ? getNow(context) : null), false);
		return result;
	}

	private Date getNow(VdeContext context) {
		Date date = context.get(DateAspect.class);
		if (date == null) {
			date = new Date();
		}
		return date;
	}

}

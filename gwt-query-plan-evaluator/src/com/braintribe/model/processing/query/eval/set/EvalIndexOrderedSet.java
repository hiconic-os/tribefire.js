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
package com.braintribe.model.processing.query.eval.set;

import java.util.Iterator;

import com.braintribe.model.processing.query.eval.api.EvalTupleSet;
import com.braintribe.model.processing.query.eval.api.QueryEvaluationContext;
import com.braintribe.model.processing.query.eval.api.Tuple;
import com.braintribe.model.queryplan.set.IndexOrderedSet;

/** {@link EvalTupleSet} for {@link IndexOrderedSet}. */
public class EvalIndexOrderedSet implements EvalTupleSet {

	protected final Iterable<Tuple> tuples;

	public EvalIndexOrderedSet(IndexOrderedSet indexOrderedSet, QueryEvaluationContext context) {
		tuples = context.getFullRange(indexOrderedSet.getMetricIndex(), indexOrderedSet.getDescending());
	}

	@Override
	public Iterator<Tuple> iterator() {
		return tuples.iterator();
	}

}

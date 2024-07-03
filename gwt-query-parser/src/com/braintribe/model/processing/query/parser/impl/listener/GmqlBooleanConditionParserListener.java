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
package com.braintribe.model.processing.query.parser.impl.listener;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.AndPredicateContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.BooleanAtomicExpressionContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.BooleanValueExpressionContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.NegationPredicateContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.OrPredicateContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.ParenthesizedBooleanValueExpressionContext;
import com.braintribe.model.processing.query.parser.impl.context.ConditionCustomContext;
import com.braintribe.model.processing.query.parser.impl.context.ObjectCustomContext;

public abstract class GmqlBooleanConditionParserListener extends GmqlLiteralParserListener {

	@Override
	public void exitBooleanValueExpression(BooleanValueExpressionContext ctx) {
		setValue(ctx, takeValue(ctx.orPredicate()));
	}

	@Override
	public void exitOrPredicate(OrPredicateContext ctx) {
		List<AndPredicateContext> andPredicateList = ctx.andPredicate();
		if (andPredicateList.size() > 1) {
			List<Object> operands = new ArrayList<Object>();
			while (!andPredicateList.isEmpty()) {
				Object currentObject = takeValue(andPredicateList.remove(0)).cast().getReturnValue();
				operands.add(currentObject);
			}
			setValue(ctx, new ConditionCustomContext($.disjunction(operands)));

		} else {
			setValue(ctx, new ObjectCustomContext(takeValue(ctx.andPredicate(0)).getReturnValue()));
		}
	}

	@Override
	public void exitAndPredicate(AndPredicateContext ctx) {

		List<NegationPredicateContext> negationPredicateList = ctx.negationPredicate();
		if (negationPredicateList.size() > 1) {
			List<Object> operands = new ArrayList<Object>();
			while (!negationPredicateList.isEmpty()) {
				Object currentObject = takeValue(negationPredicateList.remove(0)).cast().getReturnValue();
				operands.add(currentObject);
			}
			setValue(ctx, new ConditionCustomContext($.conjunction(operands)));

		} else {
			setValue(ctx, new ObjectCustomContext(takeValue(ctx.negationPredicate(0)).getReturnValue()));
		}
	}

	@Override
	public void exitNegationPredicate(NegationPredicateContext ctx) {
		if (ctx.Not() != null) {
			setValue(ctx, new ConditionCustomContext($.negation(takeValue(ctx.booleanAtomicExpression()).getReturnValue())));
		} else {
			setValue(ctx, takeValue(ctx.booleanAtomicExpression()));
		}
	}

	@Override
	public void exitBooleanAtomicExpression(BooleanAtomicExpressionContext ctx) {
		propagateChildResult(ctx);
	}

	@Override
	public void exitParenthesizedBooleanValueExpression(ParenthesizedBooleanValueExpressionContext ctx) {
		setValue(ctx, takeValue(ctx.booleanValueExpression()));
	}

}

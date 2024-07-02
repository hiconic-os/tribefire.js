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
package com.braintribe.model.processing.query.parser.impl.listener;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.ConcatenationContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.LocalizeContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.LowerContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.StringFunctionContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.StringFunctionParameterContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.ToStringRuleContext;
import com.braintribe.model.processing.query.parser.impl.autogenerated.GmqlParser.UpperContext;
import com.braintribe.model.processing.query.parser.impl.context.ObjectCustomContext;
import com.braintribe.model.processing.query.parser.impl.context.ValueCustomContext;
import com.braintribe.model.processing.query.parser.impl.context.basetype.DefaultCustomContext;

public abstract class GmqlStringFunctionParserListener extends GmqlAggregateFunctionParserListener {

	@Override
	public void exitStringFunction(StringFunctionContext ctx) {
		propagateChildResult(ctx);
	}

	@Override
	public void exitStringFunctionParameter(StringFunctionParameterContext ctx) {
		propagateChildResult(ctx);
	}

	@Override
	public void exitLower(LowerContext ctx) {
		Object operand = ((ValueCustomContext<?>) takeValue(ctx.stringFunctionParameter()).cast()).getReturnValue();
		setValue(ctx, new ObjectCustomContext($.lower(operand)));
	}

	@Override
	public void exitUpper(UpperContext ctx) {
		Object operand = ((ValueCustomContext<?>) takeValue(ctx.stringFunctionParameter()).cast()).getReturnValue();
		setValue(ctx, new ObjectCustomContext($.upper(operand)));
	}

	@Override
	public void exitToStringRule(ToStringRuleContext ctx) {
		Object operand = ((ValueCustomContext<?>) takeValue(ctx.stringFunctionParameter()).cast()).getReturnValue();
		setValue(ctx, new ObjectCustomContext($.toString(operand)));
	}

	@Override
	public void exitConcatenation(ConcatenationContext ctx) {
		List<StringFunctionParameterContext> stringFunctionParameterList = ctx.stringFunctionParameter();
		List<Object> operandList = new ArrayList<Object>();
		while (!stringFunctionParameterList.isEmpty()) {
			Object operand = ((ValueCustomContext<?>) takeValue(stringFunctionParameterList.remove(0)).cast()).getReturnValue();
			operandList.add(operand);
		}
		setValue(ctx, new ObjectCustomContext($.concatenation(operandList)));
	}

	@Override
	public void exitLocalize(LocalizeContext ctx) {
		Object localiseString = ((ValueCustomContext<?>) takeValue(ctx.value()).cast()).getReturnValue();
		String locale = ((DefaultCustomContext) takeValue(ctx.stringValue()).cast()).getReturnValue();
		setValue(ctx, new ObjectCustomContext($.localise(localiseString, locale)));
	}
}

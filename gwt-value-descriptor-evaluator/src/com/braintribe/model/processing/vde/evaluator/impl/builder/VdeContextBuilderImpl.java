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
package com.braintribe.model.processing.vde.evaluator.impl.builder;

import com.braintribe.model.processing.vde.evaluator.api.VdeContext;
import com.braintribe.model.processing.vde.evaluator.api.VdeContextAspect;
import com.braintribe.model.processing.vde.evaluator.api.VdeEvaluationMode;
import com.braintribe.model.processing.vde.evaluator.api.VdeRegistry;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.evaluator.api.builder.VdeContextBuilder;
import com.braintribe.model.processing.vde.evaluator.impl.VdeContextImpl;
import com.braintribe.processing.async.api.AsyncCallback;

/**
 * Implementation of VdeContextBuilder
 * 
 * @see VdeContextBuilder
 * 
 */
public class VdeContextBuilderImpl implements VdeContextBuilder {

	protected VdeContext context = new VdeContextImpl();

	@Override
	public <T, A extends VdeContextAspect<? super T>> VdeContextBuilder with(Class<A> aspect, T value) {
		this.context.put(aspect, value);
		return this;
	}

	@Override
	public <T> T forValue(Object value) throws VdeRuntimeException {
		return (T) this.context.evaluate(value);
	}
	
	@Override
	public <T> void forValue(Object value, AsyncCallback<T> callback) {
		this.context.evaluate(value, callback);
	}
	
	@Override
	public VdeContextBuilder withEvaluationMode(VdeEvaluationMode mode) {
		this.context.setEvaluationMode(mode);
		return this;
	}

	@Override
	public VdeContextBuilder withRegistry(VdeRegistry registry) {
		this.context.setVdeRegistry(registry);
		return this;
	}

}

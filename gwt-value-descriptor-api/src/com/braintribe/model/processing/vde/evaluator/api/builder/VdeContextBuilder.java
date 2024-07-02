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
package com.braintribe.model.processing.vde.evaluator.api.builder;

import com.braintribe.model.processing.vde.evaluator.api.VdeContextAspect;
import com.braintribe.model.processing.vde.evaluator.api.VdeEvaluationMode;
import com.braintribe.model.processing.vde.evaluator.api.VdeRegistry;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.processing.async.api.AsyncCallback;

/**
 * The builder of the VDE context
 * 
 */
public interface VdeContextBuilder {

	/**
	 * Adds a {@link VdeRegistry} to be used for evaluation
	 * 
	 * @return A context builder with a registry
	 */
	VdeContextBuilder withRegistry(VdeRegistry registry);

	/**
	 * Adds a custom aspect to the context
	 * 
	 * @param aspect
	 *            An aspect that extends VdeContextAspect
	 * @param value
	 *            The value of the aspect
	 */
	<T, A extends VdeContextAspect<? super T>> VdeContextBuilder with(Class<A> aspect, T value);

	/**
	 * Performs the evaluation of object against all the aspects and experts stored in the context. If the value is not of type valueDescriptor the
	 * same object is returned
	 * 
	 * @param value
	 *            Object to be evaluated
	 * @return the result of evaluation
	 */
	<T> T forValue(Object value) throws VdeRuntimeException;

	/**
	 * Performs the evaluation of object against all the aspects and experts stored in the context. If the value is not of type valueDescriptor the
	 * same object is returned
	 * 
	 * @param value
	 *            Object to be evaluated
	 * @param callback
	 *            the callback to be notified about successful results or exceptions
	 */
	<T> void forValue(Object value, AsyncCallback<T> callback);

	/**
	 * Adjusts the evaluation mode of the VDE according to the {@link VdeEvaluationMode}
	 * 
	 * @param mode
	 *            Evaluation mode to be used
	 * @return A context builder with the updated evaluation mode
	 */
	VdeContextBuilder withEvaluationMode(VdeEvaluationMode mode);
}

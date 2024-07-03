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
package com.braintribe.model.processing.vde.evaluator;

import com.braintribe.model.generic.value.ValueDescriptor;
import com.braintribe.model.processing.vde.builder.api.VdBuilder;
import com.braintribe.model.processing.vde.builder.impl.VdBuilderImpl;
import com.braintribe.model.processing.vde.evaluator.api.ValueDescriptorEvaluator;
import com.braintribe.model.processing.vde.evaluator.api.VdeContextAspect;
import com.braintribe.model.processing.vde.evaluator.api.VdeRegistry;
import com.braintribe.model.processing.vde.evaluator.api.VdeRuntimeException;
import com.braintribe.model.processing.vde.evaluator.api.builder.VdeContextBuilder;
import com.braintribe.model.processing.vde.evaluator.api.builder.VdeRegistryBuilder;
import com.braintribe.model.processing.vde.evaluator.impl.builder.VdeContextBuilderImpl;
import com.braintribe.model.processing.vde.evaluator.impl.builder.VdeRegistryBuilderImpl;
import com.braintribe.processing.async.api.AsyncCallback;

/**
 * Main Access point to all value descriptor evaluation actions.
 * 
 * All methods are declared as static.
 * 
 */
public class VDE {

	/**
	 * Evaluates an object with the respect to a Context (In theory, the context will include all the preset values in a default context, add
	 * optionally more)
	 * 
	 * @param object
	 *            The object that needs to be evaluated
	 * @param contextBuilder
	 *            A VdeContextBuilder that has been filled with valid aspects and experts.
	 * @return evaluated object
	 */
	public static Object evaluate(Object object, VdeContextBuilder contextBuilder) throws VdeRuntimeException {
		return contextBuilder.forValue(object);
	}

	/**
	 * Evaluates an object with the preset values of a VDE Context.
	 * 
	 * @param object
	 *            The object that needs to be evaluated
	 * @return evaluated object
	 */
	public static Object evaluate(Object object) throws VdeRuntimeException {
		return evaluate().forValue(object);
	}

	public static <T> void evaluate(Object object, AsyncCallback<T> callback) throws VdeRuntimeException {
		evaluate().forValue(object, callback);
	}

	/**
	 * Evaluates an object with the preset values of a VDE Context. In addition to the provided aspect and its value.
	 * 
	 * @see VdeContextBuilder#with(Class, Object)
	 * 
	 * @param aspect
	 *            An aspect that might be used in evaluation
	 * @param value
	 *            The value of the aspect
	 * @param object
	 *            The object that needs to be evaluated
	 * @return evaluated object
	 */
	public static <T, A extends VdeContextAspect<? super T>> Object evaluateWith(Class<A> aspect, T value, Object object) throws VdeRuntimeException {
		return evaluate().with(aspect, value).forValue(object);
	}

	/**
	 * Evaluates an object with the preset values of a VDE Context. In addition to the provided concrete evaluator.
	 * 
	 * @see VdeRegistryBuilder#withConcreteExpert(Class, ValueDescriptorEvaluator)
	 * 
	 * @param vdType
	 *            A class type that extends ValueDescriptor
	 * @param vdEvaluator
	 *            A ValueDescriptorEvaluator for the provided class type
	 * @param object
	 *            The object that needs to be evaluated
	 * @return evaluated object
	 */
	public static <D extends ValueDescriptor> Object evaluateWithConcreteExpert(Class<D> vdType, ValueDescriptorEvaluator<? super D> vdEvaluator,
			Object object) throws VdeRuntimeException {
		VdeRegistry registry = extendedRegistry().withConcreteExpert(vdType, vdEvaluator).done();
		return evaluate().withRegistry(registry).forValue(object);
	}

	/**
	 * Evaluates an object with the preset values of a VDE Context. In addition to the provided abstract evaluator.
	 * 
	 * @see VdeContextBuilder#withRegistry(VdeRegistry)
	 * 
	 * @param vdType
	 *            A class type that extends ValueDescriptor
	 * @param vdEvaluator
	 *            A ValueDescriptorEvaluator for the provided class type
	 * @param object
	 *            The object that needs to be evaluated
	 * @return evaluated object
	 */
	public static <D extends ValueDescriptor> Object evaluateWithAbstractExpert(Class<D> vdType, ValueDescriptorEvaluator<? super D> vdEvaluator,
			Object object) throws VdeRuntimeException {
		VdeRegistry registry = extendedRegistry().withAbstractExpert(vdType, vdEvaluator).done();
		return evaluate().withRegistry(registry).forValue(object);
	}

	/**
	 * @return A new instance of {@link VdeContextBuilder}
	 */
	public static VdeContextBuilder evaluate() {
		return new VdeContextBuilderImpl();
	}

	/** Instantiates a {@link VdBuilder} */
	public static VdBuilder builder() {
		return new VdBuilderImpl();
	}

	/** @return A new {@link VdeRegistryBuilder} pre-loaded with {@link VdeRegistryBuilder#defaultSetup() default setup}. */
	public static VdeRegistryBuilder extendedRegistry() {
		return registryBuilder().loadDefaultSetup();
	}

	/** @return A new {@link VdeRegistryBuilder} */
	public static VdeRegistryBuilder registryBuilder() {
		return new VdeRegistryBuilderImpl();
	}

}

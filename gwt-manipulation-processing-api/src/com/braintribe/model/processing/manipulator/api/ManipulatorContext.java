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
package com.braintribe.model.processing.manipulator.api;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.manipulation.LocalEntityProperty;
import com.braintribe.model.generic.manipulation.Manipulation;
import com.braintribe.model.generic.manipulation.PropertyManipulation;
import com.braintribe.model.generic.reflection.GenericModelType;

public interface ManipulatorContext {

	void apply(Manipulation manipulation);
	<T> T resolveValue(Object value);
	<T> T resolveValue(GenericModelType type, Object valueDescriptor);

	LocalEntityProperty resolveOwner(PropertyManipulation manipulation);

	GenericEntity createPreliminaryEntity(GenericEntity entity);
	void deleteEntityIfPreliminary(GenericEntity entity);

}

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
package com.braintribe.model.processing.itw.synthesis.gm;

import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.TransientProperty;

/**
 * Common interface for {@link PreliminaryEntityType} and {@link JvmEntityType} so that some of the ensure methods can have this as their
 * return type.
 * 
 * @author peter.gazdik
 */
public interface ItwEntityType {

	Property findProperty(String name);

	TransientProperty findTransientProperty(String name);

}

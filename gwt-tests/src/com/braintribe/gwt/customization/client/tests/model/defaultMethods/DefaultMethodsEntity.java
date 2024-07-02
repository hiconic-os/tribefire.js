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
package com.braintribe.gwt.customization.client.tests.model.defaultMethods;

import java.util.LinkedList;
import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface DefaultMethodsEntity extends DefaultMethodsAbstractEntity {

	EntityType<DefaultMethodsEntity> T = EntityTypes.T(DefaultMethodsEntity.class);
	
	String DESCRIPTION = "DME";

	default String description() throws Exception {
		return DESCRIPTION;
	}

	default int identityHash() {
		return System.identityHashCode(this);
	}

	default void addToList(List<GenericEntity> list) {
		list.add(this);
	}

	default void addToList(LinkedList<GenericEntity> list) {
		list.add(this);
		list.add(this);
	}

}

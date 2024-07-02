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
package com.braintribe.gwt.customization.client.tests.model.tostring;

import com.braintribe.model.generic.annotation.SelectiveInformation;
import com.braintribe.model.generic.annotation.ToStringInformation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@SelectiveInformation("${#type} ${#type_short} ${#id} ${#runtimeId} ${N/A} SelectiveSub")
@ToStringInformation("${#type} ${#type_short} ${#id} ${#runtimeId} ${N/A} ToStringSub")

public interface ToStringSubEntity extends ToStringEntity {

	EntityType<ToStringSubEntity> T = EntityTypes.T(ToStringSubEntity.class);

}

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
package com.braintribe.gwt.genericmodel.client.gwtresource;

import com.braintribe.gwt.genericmodel.build.GenericModelResourceGenerator;
import com.google.gwt.resources.client.ResourcePrototype;
import com.google.gwt.resources.ext.DefaultExtensions;
import com.google.gwt.resources.ext.ResourceGeneratorType;

/**
 * A resource that contains a GenericModel entity assembly that should be incorporated into the compiled
 * output.
 */
@DefaultExtensions(value = {".gm.xml", ".gm.json"})
@ResourceGeneratorType(GenericModelResourceGenerator.class)
public interface GenericModelResource extends ResourcePrototype {
	public <T> T getAssembly();
}

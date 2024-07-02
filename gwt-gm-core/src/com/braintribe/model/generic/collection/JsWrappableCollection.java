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
package com.braintribe.model.generic.collection;

/**
 * Interface implemented by Plain/Enhanced collections, so they can be 1 to 1 associated with a JS wrapper.
 * <p>
 * This JS collection wrapper is something that (mostly) fulfills the Array/Set/Map interfaces in JavaScript, to achieve the goal that our entities
 * interface with native JS objects, rather than gwt-compiled Java objects (e.g. a date property looks like it holds a JS Date).
 * <p>
 * See Arrayish, Setish and Mapish in generic-model-module.
 */
public interface JsWrappableCollection {

	Collectionish getCollectionWrapper();

	void setCollectionWrapper(Collectionish wrapper);

}

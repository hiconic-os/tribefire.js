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
package com.braintribe.gwt.customization.client.labs.types;

import com.braintribe.gwt.customization.client.tests.AbstractGwtTest;

/**
 * This is just to analyze the generated JS code, to see how Java types are mapped to JS.
 */
public class SimpleTypesOverview extends AbstractGwtTest {

	@Override
	public void tryRun() {
		PojoJoat pojo = new PojoJoat();
		pojo.primitiveBoolean = true;
		pojo.objectBoolean = true;
		pojo.primitiveInt = 1;
		pojo.objectInt = 1;
		pojo.primitiveLong = 1L;
		pojo.objectLong = 1L;
		pojo.primitiveFloat = 1.0f;
		pojo.objectFloat = 1.0f;
		pojo.primitiveDouble = 1.0;
		pojo.objectDouble = 1.0;
		pojo.string = "hello";
		
		log("Simple types set.");
	}


}

class PojoJoat {
	public boolean primitiveBoolean;
	public Boolean objectBoolean;
	public int primitiveInt;
	public Integer objectInt;
	public long primitiveLong;
	public Long objectLong;
	public float primitiveFloat;
	public Float objectFloat;
	public double primitiveDouble;
	public Double objectDouble;
	public String string;
}
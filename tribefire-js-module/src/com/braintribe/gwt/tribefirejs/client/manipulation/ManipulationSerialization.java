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
package com.braintribe.gwt.tribefirejs.client.manipulation;

import java.util.List;

import com.braintribe.gwt.async.client.Future;
import com.braintribe.gwt.browserfeatures.client.JsArray;
import com.braintribe.gwt.genericmodel.client.codec.api.DefaultDecodingContext;
import com.braintribe.gwt.genericmodel.client.codec.api.GmDecodingContext;
import com.braintribe.gwt.genericmodel.client.codec.dom4.GmXmlCodec;
import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.braintribe.model.generic.manipulation.CompoundManipulation;
import com.braintribe.model.generic.manipulation.Manipulation;
import com.braintribe.model.processing.manipulation.basic.tools.ManipulationRemotifier;
import com.braintribe.processing.async.api.JsPromise;

import jsinterop.annotations.JsType;

@JsType(namespace = GmCoreApiInteropNamespaces.manipulation)
public interface ManipulationSerialization {

	static JsPromise<String> serializeManipulations(JsArray<Manipulation> manipulations, boolean local) {
		CompoundManipulation manipulation = CompoundManipulation.T.create();
		List<Manipulation> compoundManipulationList = manipulation.getCompoundManipulationList();

		int count = manipulations.length();
		
		for (int i = 0; i < count; i++)
			compoundManipulationList.add(manipulations.get(i));

		return serializeManipulation(manipulation, local);
	}
	
	static JsPromise<String> serializeManipulationList(List<Manipulation> manipulations, boolean local) {
		return serializeManipulation(CompoundManipulation.create(manipulations), local);
	}

	static JsPromise<String> serializeManipulation(Manipulation manipulation, boolean local) {
		manipulation = local? ManipulationRemotifier.remotify(manipulation): manipulation;

		GmXmlCodec<Object> gmXmlCodec = new GmXmlCodec<>();
		Future<String> future = gmXmlCodec.encodeAsync(manipulation, null);
		
		return future.toJsPromise();
	}
	
	static JsPromise<Manipulation> deserializeManipulation(String s) {
		GmXmlCodec<Object> gmXmlCodec = new GmXmlCodec<>();
		GmDecodingContext dc = new DefaultDecodingContext();
		Future<Manipulation> future = gmXmlCodec.decodeAsync(s, dc);
		return future.toJsPromise();
	}
}

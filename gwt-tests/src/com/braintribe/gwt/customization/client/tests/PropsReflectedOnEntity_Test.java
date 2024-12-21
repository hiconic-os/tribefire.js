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
package com.braintribe.gwt.customization.client.tests;

import java.util.List;
import java.util.stream.Collectors;

import com.braintribe.gwt.customization.client.tests.model.simple.PropsEntity;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.reflection.Property;

/**
 * @author peter.gazdik
 */
public class PropsReflectedOnEntity_Test extends AbstractGmGwtTest {

	@Override
	protected void tryRun() throws GmfException {
		PropsEntity entity = PropsEntity.T.create();

		String[] propNamesJs = getPropNamesJs(entity);
		String[] propNamesJava = getPropNamesJava(entity);

		assertArraysEquals(propNamesJs, propNamesJava, "Entity.PropertyNames");

		Property[] propsJs = getPropsJs(entity);
		Property[] propsJava = getsJava(entity);

		assertArraysEquals(propsJs, propsJava, "Entity.Properties");

	}

	private native String[] getPropNamesJs(GenericEntity e) /*-{
		return e.PropertyNames();
	}-*/;

	private String[] getPropNamesJava(GenericEntity e) {
		List<String> ps = e.entityType().getProperties().stream() //
				.map(Property::getName) //
				.collect(Collectors.toList());
		return ps.toArray(new String[ps.size()]);
	}

	private native Property[] getPropsJs(GenericEntity e) /*-{
		return e.Properties();
	}-*/;

	private Property[] getsJava(GenericEntity e) {
		return e.entityType().getProperties().toArray(new Property[0]);
	}

}

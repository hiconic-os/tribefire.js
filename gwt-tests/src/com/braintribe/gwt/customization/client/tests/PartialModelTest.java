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

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import com.braintribe.gwt.customization.client.tests.model.initializer.InitializedEntity;
import com.braintribe.gwt.customization.client.tests.model.initializer.InitializedSubEntity;
import com.braintribe.gwt.customization.client.tests.model.partial.PartialEntity;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.StandardCloningContext;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.util.meta.NewMetaModelGeneration;

/**
 * Tests that a model can be woven even if all the dependencies / types from dependencies are shallow.
 * 
 * @author peter.gazdik
 */
public class PartialModelTest extends AbstractGmGwtTest {

	@Override
	protected void tryRun() throws GmfException {
		GmMetaModel metaModel = generateModel();

		makeSignaturesDynamic(metaModel);

		metaModel = shallowify(metaModel);
		ensureModelTypes(metaModel);

		EntityType<?> et = typeReflection.getEntityType(makeSignatureDynamic(PartialEntity.class.getName()));
		GenericEntity instance = et.create();

		if (instance == null) {
			logError("ITW FAILED");
		} else {
			log("Properly woven shallow entity type:" + et.getTypeSignature());
		}
	}

	private GmMetaModel shallowify(GmMetaModel metaModel) {
		metaModel.getDependencies().clear();

		return GmMetaModel.T.clone(new ShallowifyingCloningContext(), metaModel, null);
	}

	private static class ShallowifyingCloningContext extends StandardCloningContext {

		@Override
		public <T> T getAssociated(GenericEntity entity) {
			T associated = super.getAssociated(entity);
			if (associated != null || !(entity instanceof GmType))
				return associated;

			GmType gmType = (GmType) entity;

			if (gmType.getTypeSignature().contains("partial"))
				return null;

			GmType shallowCopy = (GmType) gmType.entityType().create();
			shallowCopy.setTypeSignature(gmType.getTypeSignature());

			registerAsVisited(entity, shallowCopy);

			return (T) shallowCopy;
		}

	}

	private GmMetaModel generateModel() {
		log("generating meta model");

		NewMetaModelGeneration mmg = new NewMetaModelGeneration();
		GmMetaModel initializerModel = mmg.buildMetaModel("test.gwt:initializer-model", asList(InitializedEntity.T, InitializedSubEntity.T));
		GmMetaModel partialModel = mmg.buildMetaModel("test.gwt:partial-model", asList(PartialEntity.T), asList(initializerModel));

		return partialModel;
	}

}

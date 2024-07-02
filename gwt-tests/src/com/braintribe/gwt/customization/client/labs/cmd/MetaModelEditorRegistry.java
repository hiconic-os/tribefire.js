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
package com.braintribe.gwt.customization.client.labs.cmd;

import static com.braintribe.utils.lcd.CollectionTools2.first;
import static com.braintribe.utils.lcd.CollectionTools2.newMap;
import static com.braintribe.utils.lcd.CollectionTools2.newSet;

import java.util.Map;
import java.util.Set;

import com.braintribe.model.generic.reflection.GenericModelException;
import com.braintribe.model.meta.GmCustomType;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmEnumConstant;
import com.braintribe.model.meta.GmEnumType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.meta.GmType;

/**
 * @author peter.gazdik
 */
public class MetaModelEditorRegistry {

	private final Map<GmMetaModel, MetaModelEditor> editors = newMap();
	private final Map<String, GmMetaModel> models = newMap();
	private final Map<String, GmType> types = newMap();

//	static class Type
	
	public MetaModelEditorRegistry(MetaModelEditor metaModelEditor, GmMetaModel metaModel) {
		editors.put(metaModel, metaModelEditor);
	}

	public MetaModelEditor acquireEditorFor(GmCustomType gmCustomType) {
		return acquireEditorFor(gmCustomType.getDeclaringModel());
	}

	public MetaModelEditor acquireEditorForModel(String modelName) {
		if (models.isEmpty()) {
			indexModelsAndTypes();
		}

		GmMetaModel model = models.get(modelName);
		if (model == null) {
			throw new GenericModelException("No model found with name:" + modelName);
		}

		return acquireEditorFor(model);
	}

	public MetaModelEditor acquireEditorFor(GmMetaModel declaringModel) {
		MetaModelEditor result = editors.get(declaringModel);
		if (result == null) {
			result = new MetaModelEditor(declaringModel, this);
			editors.put(declaringModel, result);
		}

		return result;
	}

	public <T extends GmType> T getType(String typeSignature) {
		if (types.isEmpty()) {
			indexModelsAndTypes();
		}

		T result = (T) types.get(typeSignature);
		if (result == null) {
			throw new GenericModelException("No type found with signature: " + typeSignature);
		}

		return result;
	}

	public GmProperty getProperty(String typeSignature, String propertyName) {
		GmEntityType gmEntityType = getType(typeSignature);
		return MetaModelEditor.getProperty(gmEntityType, propertyName);
	}

	public GmEnumConstant getConstant(String typeSignature, String constantName) {
		GmEnumType gmEnumType = getType(typeSignature);
		return MetaModelEditor.getConstant(gmEnumType, constantName);
	}
	
	private void indexModelsAndTypes() {
		GmMetaModel metaModel = first(editors.keySet());
		index(metaModel, newSet());

		if (types.isEmpty()) {
			throw new GenericModelException("No types in model or it's dependencies found!");
		}
	}

	private void index(GmMetaModel model, Set<GmMetaModel> visited) {
		if (!visited.add(model)) {
			return;
		}

		models.put(model.getName(), model);

		for (GmType gmType: model.getTypes()) {
			types.put(gmType.getTypeSignature(), gmType);
		}

		for (GmMetaModel dependencyModel: model.getDependencies()) {
			index(dependencyModel, visited);
		}
	}

}

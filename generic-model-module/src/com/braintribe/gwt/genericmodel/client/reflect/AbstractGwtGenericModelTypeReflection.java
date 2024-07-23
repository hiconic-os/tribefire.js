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
package com.braintribe.gwt.genericmodel.client.reflect;

import java.lang.reflect.Type;
import java.util.stream.Stream;

import com.braintribe.gwt.genericmodel.client.GwtGmPlatform;
import com.braintribe.gwt.genericmodel.client.itw.GwtScriptTypeSynthesis;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.AbstractGenericModelTypeReflection;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.GenericModelException;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.type.custom.EnumTypeImpl;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.Weavable;
import com.braintribe.model.weaving.ProtoGmEntityType;
import com.braintribe.processing.async.api.AsyncCallback;
import com.google.gwt.core.client.JavaScriptObject;

import jsinterop.context.JsKeywords;

/**
 *
 */
public abstract class AbstractGwtGenericModelTypeReflection extends AbstractGenericModelTypeReflection {

	static {
		ensureSymbols();
	}

	private static native void ensureSymbols() /*-{
		$wnd.$tf = $wnd.$tf || {};
		$wnd.$tf.Symbol = $wnd.$tf.Symbol || {};
		$wnd.$tf.Symbol.enumType = "$enumType";
	}-*/;

	@Override
	public Object getItwClassLoader() {
		return null;
	}

	@Override
	public EnumType<?> deployEnumType(Class<? extends Enum<?>> enumClass) {
		EnumType<?> type = new EnumTypeImpl<>(enumClass);

		registerGenericModelType(enumClass, type);

		TypePackage.register(type, enumObject(type));
		return type;
	}

	private JavaScriptObject enumObject(EnumType<?> type) {
		JavaScriptObject result = JavaScriptObject.createObject();

		defineEnumType(result, type);

		for (Enum<?> value : type.getEnumValues())
			defineConstant(result, JsKeywords.javaIdentifierToJs(value.name()), value);

		return result;
	}

	private static native void defineEnumType(JavaScriptObject enumObject, EnumType<?> type) /*-{
		enumObject[$wnd.$tf.Symbol.enumType] = type;
	}-*/;

	private static native void defineConstant(JavaScriptObject enumObject, String name, Enum<?> value) /*-{
		enumObject[name] = value;
	}-*/;

	@Override
	public void deployEntityType(EntityType<?> entityType) {
		super.deployEntityType(entityType);
		TypePackage.register(entityType, entityType);
	}

	/** Called after instantiated, from {@link GwtGmPlatform}. */
	public void initialize() {
		initialTypes().forEach(this::deployEntityType);
	}

	public abstract Stream<EntityType<?>> initialTypes();

	private static GwtScriptTypeSynthesis gwtScriptTypeSynthesis;

	@Override
	public void deploy(Weavable weavable) {
		getGwtScriptTypeSynthesis().ensureModelTypes((GmMetaModel) weavable);
	}

	@SuppressWarnings("unusable-by-js")
	@Override
	public void deploy(Weavable weavable, final AsyncCallback<Void> asyncCallback) {
		getGwtScriptTypeSynthesis().ensureModelTypesAsync((GmMetaModel) weavable) //
				.andThen(asyncCallback::onSuccess) //
				.onError(asyncCallback::onFailure);
	}

	protected GwtScriptTypeSynthesis createGwtScriptTypeSynthesis() {
		return new GwtScriptTypeSynthesis();
	}

	private GwtScriptTypeSynthesis getGwtScriptTypeSynthesis() {
		if (gwtScriptTypeSynthesis == null)
			gwtScriptTypeSynthesis = createGwtScriptTypeSynthesis();

		return gwtScriptTypeSynthesis;
	}

	@Override
	protected <T extends GenericEntity> EntityType<T> createEntityType(Class<?> entityClass) throws GenericModelException {
		throw new GenericModelException("Unexpected 'createEntityType' invocation."
				+ " EntityTypes for all GenericEntities should have been created at compile-time and registered on bootstrap." + " Class name: "
				+ entityClass.getName());
	}

	@Override
	protected Class<?> getClassForName(String qualifiedEntityTypeName, boolean require) throws GenericModelException {
		if (require)
			throw new UnsupportedOperationException("No class lookup supported in GWT!");

		return null;
	}

	@Override
	public <T extends GenericModelType> T getType(Type type) throws GenericModelException {
		throw new UnsupportedOperationException("Method 'AbstractGwtGenericModelTypeReflection.getGenericModelType' is not supported in GWT.");
	}

	@Override
	protected <T extends GenericModelType> T createCustomType(Class<?> classType) {
		throw new GenericModelException("Unexpected 'createCustomType' invocation."
				+ " EntityTypes for all GenericEntities should have been created at compile-time and registered on bootstrap." + " Class name: "
				+ classType.getName());
	}

	@Override
	public ProtoGmEntityType findProtoGmEntityType(String typeSignature) {
		throw new UnsupportedOperationException("Method 'findProtoGmEntityType' is not supported in GWT!");
	}

}

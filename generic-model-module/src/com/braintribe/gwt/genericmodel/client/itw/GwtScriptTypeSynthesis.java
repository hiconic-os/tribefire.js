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
package com.braintribe.gwt.genericmodel.client.itw;

import static com.braintribe.utils.lcd.CollectionTools2.index;
import static com.braintribe.utils.lcd.CollectionTools2.newLinkedMap;
import static com.braintribe.utils.lcd.CollectionTools2.newList;
import static com.braintribe.utils.lcd.CollectionTools2.newMap;
import static com.braintribe.utils.lcd.CollectionTools2.nullSafe;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.braintribe.common.lcd.Pair;
import com.braintribe.common.lcd.Tuple.Tuple3;
import com.braintribe.gwt.async.client.Future;
import com.braintribe.gwt.genericmodel.client.reflect.GwtEntityType;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.EntityInitializer;
import com.braintribe.model.generic.reflection.EntityInitializerImpl;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.GmtsEnhancedEntityStub;
import com.braintribe.model.generic.reflection.ItwTypeReflection;
import com.braintribe.model.generic.reflection.MapType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.PropertyAccessInterceptor;
import com.braintribe.model.generic.reflection.TypeCode;
import com.braintribe.model.generic.reflection.type.custom.AbstractEntityType;
import com.braintribe.model.generic.value.NullDescriptor;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmEnumConstant;
import com.braintribe.model.meta.GmEnumType;
import com.braintribe.model.meta.GmListType;
import com.braintribe.model.meta.GmMapType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.meta.GmSetType;
import com.braintribe.model.meta.GmSimpleType;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.meta.data.prompt.Confidential;
import com.braintribe.model.meta.info.GmPropertyInfo;
import com.braintribe.model.meta.override.GmPropertyOverride;
import com.braintribe.model.processing.meta.oracle.hierarchy.GraphInliner;
import com.braintribe.model.processing.meta.oracle.hierarchy.InlinedGraph;
import com.google.gwt.core.client.GwtScriptOnly;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.dom.client.Style.Unit;

@GwtScriptOnly
public class GwtScriptTypeSynthesis {

	private static ItwTypeReflection typeReflection = GMF.getTypeReflection();

	public void ensureModelTypes(GmMetaModel gmMetaModel) {
		GwtItwContinuationContext context = new GwtItwContinuationContext(gmMetaModel);
		context.executeSync(new TypeAcquiring(getTypes(gmMetaModel)));
	}

	public Future<Void> ensureModelTypesAsync(GmMetaModel gmMetaModel) {
		GwtItwContinuationContext context = new GwtItwContinuationContext(gmMetaModel);
		return context.execute(new TypeAcquiring(getTypes(gmMetaModel)));
	}

	private List<GmType> getTypes(GmMetaModel gmMetaModel) {
		InlinedGraph<GmMetaModel> models = GraphInliner.inline(gmMetaModel, GmMetaModel::getDependencies);
		return models.list.stream().flatMap(m -> m.getTypes().stream()).collect(Collectors.toList());
	}

	private <T extends GenericModelType> T acquireType(GwtItwContinuationContext context, GmType gmType) throws GmfException {
		GenericModelType type = typeReflection.findType(gmType.getTypeSignature());

		if (type == null)
			type = createType(context, gmType);

		return type.cast();
	}

	private GenericModelType createType(GwtItwContinuationContext context, GmType gmType) throws GmfException {
		try {
			switch (gmType.typeKind()) {
				case ENTITY:
					return createPreliminaryEntityType(context, (GmEntityType) gmType);
				case ENUM:
					return createEnumType((GmEnumType) gmType);
				case LIST:
					return createListType(context, (GmListType) gmType);
				case MAP:
					return createMapType(context, (GmMapType) gmType);
				case SET:
					return createSetType(context, (GmSetType) gmType);
				default:
					throw new GmfException("Cannot create type: " + gmType.getTypeSignature()
							+ ". Only Entity, Enum and Collection types are supported, but not: " + gmType.typeKind());
			}
		} catch (RuntimeException e) {
			throw new RuntimeException("Error while creating GenericModelType for " + gmType.typeKind() + " type: " + gmType.getTypeSignature(), e);
		}
	}

	private EntityType<?> createPreliminaryEntityType(GwtItwContinuationContext context, GmEntityType gmEntityType) throws GmfException {
		GwtScriptEntityType<?> entityType = new GwtRuntimeEntityType<>();

		entityType.setJavaType(RuntimeClassTools.createForInterface(gmEntityType.getTypeSignature()));
		entityType.setIsAbstract(Boolean.TRUE.equals(gmEntityType.getIsAbstract()));

		typeReflection.deployEntityType(entityType);

		/* set supertypes which will push missing supertype weaving fragments (need to be executed so and thus pushed after the weaver itself */
		List<AbstractEntityType<?>> superTypes = newList();
		entityType.setSuperTypes(superTypes);

		// ensure super types and double link them
		for (GmEntityType gmSuperType : gmEntityType.getSuperTypes()) {
			AbstractEntityType<?> superType = acquireType(context, gmSuperType);
			superType.addSubType(entityType);
			superTypes.add(superType);
		}

		/* We must push the fragment first and only then set the evaluatesToType, because this eval type might be our sub-type, which would mean the
		 * sub-type is pushed first. */

		context.pushFragment(new EntityTypeWeaver<>(gmEntityType, entityType));

		entityType.setDeclaredTransientProperties(null);
		entityType.setEvaluatesTo(findEvaluatesToType(context, gmEntityType));

		return entityType;
	}

	private GenericModelType findEvaluatesToType(GwtItwContinuationContext context, GmEntityType gmEntityType) throws GmfException {
		GmType gmEvaluatesTo = gmEntityType.getEvaluatesTo();
		return gmEvaluatesTo != null ? acquireType(context, gmEvaluatesTo) : null;
	}

	private static String extractShortName(String typeSignature) {
		int index = typeSignature.lastIndexOf('.');
		return index != -1 ? typeSignature.substring(index + 1) : typeSignature;
	}

	private EnumType<?> createEnumType(GmEnumType gmEnumType) {
		String valueArray[] = null;
		List<GmEnumConstant> constants = gmEnumType.getConstants();

		if (constants != null) {
			valueArray = new String[constants.size()];
			for (int i = 0; i < constants.size(); i++) {
				GmEnumConstant constant = constants.get(i);
				valueArray[i] = constant.getName();
			}
		} else {
			valueArray = new String[0];
		}

		Class<? extends Enum<?>> enumClass = RuntimeClassTools.newEnumClass(gmEnumType.getTypeSignature(), valueArray);
		return typeReflection.deployEnumType(enumClass);
	}

	private CollectionType createListType(GwtItwContinuationContext context, GmListType gmListType) throws GmfException {
		GenericModelType elementType = acquireType(context, gmListType.getElementType());
		return typeReflection.getCollectionType(List.class, new GenericModelType[] { elementType });
	}

	private CollectionType createSetType(GwtItwContinuationContext context, GmSetType gmSetType) throws GmfException {
		GenericModelType elementType = acquireType(context, gmSetType.getElementType());
		return typeReflection.getCollectionType(Set.class, new GenericModelType[] { elementType });
	}

	private CollectionType createMapType(GwtItwContinuationContext context, GmMapType gmMapType) throws GmfException {
		GenericModelType keyType = acquireType(context, gmMapType.getKeyType());
		GenericModelType valueType = acquireType(context, gmMapType.getValueType());
		return typeReflection.getCollectionType(Map.class, new GenericModelType[] { keyType, valueType });
	}

	private class TypeAcquiring extends GwtItwContinuationFragment {
		private final Collection<? extends GmType> gmTypes;

		public TypeAcquiring(Collection<? extends GmType> gmTypes) {
			this.gmTypes = gmTypes;
		}

		@Override
		public void execute(GwtItwContinuationContext context) throws GmfException {
			if (gmTypes != null)
				for (GmType gmType : gmTypes)
					acquireType(context, gmType);
		}
	}

	private class EntityTypeWeaver<T extends GenericEntity> extends GwtItwContinuationFragment {
		private final GmEntityType gmEntityType;
		private final GwtScriptEntityType<T> entityType;

		private final EntityTypeBinding etb = new EntityTypeBinding();

		private GwtScriptEntityType<?> superTypeWithMostProperties;
		private GenericJavaScriptObject enhancedPrototype;

		private final Map<String, GwtScriptProperty> propertiesByName = newLinkedMap();
		private final Map<String, GmPropertyOverride> propertyOverridesByName = newLinkedMap();
		private final Map<GwtScriptEntityType<?>, Map<String, GwtScriptProperty>> propertiesBySuperType = newLinkedMap();
		private final Map<String, Object> initializerMap = newLinkedMap();

		public EntityTypeWeaver(GmEntityType gmEntityType, GwtScriptEntityType<T> entityType) {
			this.gmEntityType = gmEntityType;
			this.entityType = entityType;

			this.entityType.setEntityTypeBinding(etb);
		}

		@Override
		public void execute(GwtItwContinuationContext context) throws GmfException {
			pickSuperTypeWithMostProperties();
			indexSuperTypes();
			prepareClassesAndConstructors();
			prepareEnhancedTypeMap();
			prepareProperties(context);
			takeEverythingElseFromAllTheSuperTypes();
			prepareToStringToSelectiveInfo();
		}

		private void pickSuperTypeWithMostProperties() {
			int numberOfProperties = -1;
			for (EntityType<?> superType : entityType.getSuperTypes()) {
				if (superType.getProperties().size() > numberOfProperties) {
					superTypeWithMostProperties = (GwtScriptEntityType<?>) superType;
					numberOfProperties = superType.getProperties().size();
				}
			}
		}

		private void prepareClassesAndConstructors() {
			String enhancedSignature = gmEntityType.getTypeSignature() + "__gm";
			EntityTypeBinding superBinding = superTypeWithMostProperties.getEntityTypeBinding();

			etb.enhancedClass = RuntimeClassTools.createForClass(enhancedSignature, superBinding.enhancedClass);

			JsConstructorFunction constrF = JsConstructorFunction.create(etb.enhancedClass, superBinding.enhancedProto);
			etb.enhancedProto = constrF.getPrototype();

			if (!Boolean.TRUE.equals(gmEntityType.getIsAbstract())) {
				if (entityType instanceof GwtRuntimeEntityType)
					((GwtRuntimeEntityType<?>) entityType).setEnhancedConstructorFunction(constrF);
			}

			enhancedPrototype = etb.enhancedProto;
			entityType.setProtoInstance(enhancedPrototype);
			enhancedPrototype.setProperty(RuntimeMethodNames.entityBaseType(), ScriptOnlyItwTools.createProvider(entityType));
		}

		private void prepareEnhancedTypeMap() {
			CastableTypeMap enhancedTypeMap = ScriptOnlyItwTools.getCastableTypeMap(enhancedPrototype);
			enhancedTypeMap.addType(entityType.getTypeId());

			/* loop over ALL super types - no matter if compile or script time to combine all typeIds in the castable type map that are not inherited
			 * from the superTypeWithMostProperties */
			for (EntityType<?> superType : entityType.getSuperTypes()) {
				if (superType != superTypeWithMostProperties) {
					GwtEntityType<?> gwtSuperType = (GwtEntityType<?>) superType;
					enhancedTypeMap.addTypesFrom(gwtSuperType.getCastableTypeMap());
				}
			}
		}

		private void indexSuperTypes() {
			for (GwtScriptEntityType<?> superType : entityType.getGwtScriptSuperTypes()) {
				Map<String, GwtScriptProperty> superPropertiesByName = newLinkedMap();
				propertiesBySuperType.put(superType, superPropertiesByName);

				for (GwtScriptProperty property : superType.getGwtScriptProperties()) {
					String propertyName = property.getName();

					propertiesByName.putIfAbsent(propertyName, property);
					superPropertiesByName.put(propertyName, property);

					if (property.getInitializer() != null)
						initializerMap.putIfAbsent(propertyName, property.getInitializer());
				}

				EntityTypeBinding superEtb = superType.getEntityTypeBinding();

				if (superEtb.toStringMethod != null && etb.toStringMethod == null)
					etb.toStringMethod = superEtb.toStringMethod;

				if (superEtb.getSelectiveInformationForMethod != null && etb.getSelectiveInformationForMethod == null)
					etb.getSelectiveInformationForMethod = superEtb.getSelectiveInformationForMethod;
			}
		}

		private void prepareProperties(GwtItwContinuationContext context) throws GmfException {
			index(gmEntityType.getPropertyOverrides()) //
					.by(po -> po.relatedProperty().getName()) //
					.unique(propertyOverridesByName);

			// properties from supertype
			List<Property> properties = newList();
			for (GwtScriptProperty property : propertiesByName.values()) {
				property = pickRightSuperPropertyOrCreateNewOne(property);

				properties.add(property);

				if (superTypeWithMostProperties.findProperty(property.getName()) != null)
					continue;

				PropertyBinding propertyBinding = property.propertyBinding;

				setDefaultValueOnPrototype(property);

				Pair<JavaScriptObject, JavaScriptObject> functionsPair = buildVirtualPropertyAccessors(propertyBinding, property);
				enhancedPrototype.defineVirtualGmProperty(property.getName(), functionsPair.first(), functionsPair.second());

				if (propertyBinding.runtime)
					/* We don't have to do anything, we already have the property in our list of properties in EntityType and that one already knows
					 * how to set/get the property value on any instance. We don't have to worry about getters/setters on the entity instances,
					 * because there is no code using the getters/setters (otherwise the property would not be runtime) */
					continue;

				// mixin of existing getter/setter
				enhancedPrototype.setProperty(propertyBinding.getterName, propertyBinding.getterFunction);
				enhancedPrototype.setProperty(propertyBinding.setterName, propertyBinding.setterFunction);
			}

			// add runtime PropertyBindings
			for (GmProperty gmProperty : gmEntityType.getProperties()) {
				Object initializer = gmProperty.getInitializer();
				String propertyName = gmProperty.getName();

				if (initializer != null)
					initializerMap.put(propertyName, initializer);

				GwtRuntimeProperty property = createGwtRuntimeProperty(context, gmProperty);
				PropertyBinding pb = new PropertyBinding();
				pb.property = property;
				pb.runtime = true;

				// TODO once it's hopefully clear this never happens
				// old code was setting a primitive flag also checking if type is simple, makes no sense...
				if (!gmProperty.getNullable() && !(gmProperty.getType() instanceof GmSimpleType))
					throw new GmfException("Non simple prop marked as non-nullable!");

				setDefaultValueOnPrototype(property);

				/* We don't have to set getter/setter for runtime properties because nobody calls them. So they are not needed on the enhanced
				 * prototype (see above the code that handles super-properties) */

				Pair<JavaScriptObject, JavaScriptObject> functionsPair;

				functionsPair = buildGetterSetterAccessors(property);
				pb.getterFunction = functionsPair.first;
				pb.setterFunction = functionsPair.second;

				Tuple3<TypeCode, TypeCode, TypeCode> typeCodes = resolveCollectionKeyValueTypeCodes(property);

				functionsPair = buildVirtualPropertyAccessors(pb, property, typeCodes);
				enhancedPrototype.defineVirtualGmProperty(propertyName, functionsPair.first, functionsPair.second);

				functionsPair = buildJsConvertingPropertyAccessors(property, typeCodes);
				setJsConvertingPropertyAccessors(property, functionsPair.first, functionsPair.second);

				property.propertyBinding = pb;

				properties.add(property);

				propertiesByName.put(propertyName, property);
			}

			List<EntityInitializer> initializers = newList();
			for (Entry<String, Object> entry : initializerMap.entrySet()) {
				String propertyName = entry.getKey();
				Object initializer = entry.getValue();

				if (!(initializer instanceof NullDescriptor)) {
					Property property = propertiesByName.get(propertyName);
					initializers.add(EntityInitializerImpl.newInstance(property, initializer));
				}
			}

			entityType.setProperties(properties.toArray(new Property[properties.size()]));
			entityType.setInitializers(toArrayOrNull(initializers));
		}

		// Either null (for non-nullable properties) or the default value for given primitive type (e.g. false for boolean)
		private void setDefaultValueOnPrototype(GwtScriptProperty property) {
			Object defaultValue = property.isNullable() ? null : property.getType().getDefaultValue();
			enhancedPrototype.setProperty(property.getFieldSymbol(), defaultValue);
		}

		private native void setJsConvertingPropertyAccessors(GwtRuntimeProperty property, JavaScriptObject get, JavaScriptObject set) /*-{
			property.get = get;
			property.set = set;
		}-*/;

		/**
		 * If we cannot use a property from super-type, either because we are adding some annotation data, or because we inherit annotation data from
		 * multiple superTypes, we have to create a new {@link GwtRuntimeProperty} instance with new data.
		 * <p>
		 * We are doing the same logic here as in
		 * {@link com.braintribe.gwt.genericmodel.build.GenericModelTypeReflectionGenerator.GmtrGeneratorHelper#getTypeWhereWeDeclareTheProperty}.
		 * 
		 * <pre>
		 *  init | conf | property   // "init" means we are decalring an @Initializer on this level, "conf" means we declare it as @Confidential
		 *  NO   |  NO  | existing
		 *  YES  |  NO  | initP if exists, or new
		 *  NO   |  YES | confP if exists, or new
		 *  YES  |  YES | initP if confidential, or new
		 * </pre>
		 */
		private GwtScriptProperty pickRightSuperPropertyOrCreateNewOne(GwtScriptProperty property) {
			String propertyName = property.getName();
			GwtScriptProperty confidP = findConfidentialSuperProperty(propertyName);
			boolean isConfidential = confidP != null;

			// If PO exists, it affects whether property is confidential. If it also defines initializer, we need a new property
			GmPropertyOverride po = propertyOverridesByName.get(propertyName);
			if (po != null) {
				isConfidential |= isConfidential(po);

				if (po.getInitializer() != null)
					return createOverrideGwtRuntimePropertyAndBinding(property, po.getInitializer(), isConfidential);
			}

			// Here we follow with the logic described in javadoc

			GwtScriptProperty initP = findInitializedSuperProperty(propertyName);
			if (initP != null) {
				if (!isConfidential || initP.isConfidential())
					return initP;
				else
					return createOverrideGwtRuntimePropertyAndBinding(initP, initP.getInitializer(), isConfidential);
			}

			if (isConfidential)
				if (confidP != null)
					return confidP;
				else
					return createOverrideGwtRuntimePropertyAndBinding(property, null, true);

			return property;
		}

		private boolean isConfidential(GmPropertyInfo gmProperty) {
			return nullSafe(gmProperty.getMetaData()).stream() //
					.filter(md -> md instanceof Confidential) //
					.anyMatch(md -> md.getSelector() == null);
		}

		private GwtScriptProperty findInitializedSuperProperty(String propertyName) {
			return findSuperProperty(propertyName, p -> p.getInitializer() != null);
		}

		private GwtScriptProperty findConfidentialSuperProperty(String propertyName) {
			return findSuperProperty(propertyName, GwtScriptProperty::isConfidential);
		}

		private GwtScriptProperty findSuperProperty(String propertyName, Predicate<? super GwtScriptProperty> test) {
			return superPropertiesStream(propertyName) //
					.filter(test) //
					.findFirst() //
					.orElse(null);
		}

		private Stream<GwtScriptProperty> superPropertiesStream(String propertyName) {
			return entityType.getGwtScriptSuperTypes().stream() //
					.map(superType -> propertiesBySuperType.get(superType).get(propertyName)) //
					.filter(p -> p != null);
		}

		private GwtRuntimeProperty createOverrideGwtRuntimePropertyAndBinding(GwtScriptProperty p, Object initializer, boolean confidential) {
			PropertyBinding pb = p.propertyBinding;
			String propertyName = p.getName();

			GwtRuntimeProperty newP = createGwtRuntimeProperty(propertyName, p.getType(), initializer, p.isNullable(), confidential);

			PropertyBinding newPb = new PropertyBinding();
			newPb.property = newP;
			newPb.runtime = pb.runtime;
			newPb.getterName = pb.getterName;
			newPb.setterName = pb.setterName;
			newPb.getterFunction = pb.getterFunction;
			newPb.setterFunction = pb.setterFunction;

			newP.propertyBinding = newPb;

			initializerMap.put(propertyName, initializer);

			return newP;
		}

		private GwtRuntimeProperty createGwtRuntimeProperty(GwtItwContinuationContext ctx, GmProperty p) throws GmfException {
			return createGwtRuntimeProperty(p.getName(), acquireType(ctx, p.getType()), p.getInitializer(), p.getNullable(), isConfidential(p));
		}

		private GwtRuntimeProperty createGwtRuntimeProperty( //
				String name, GenericModelType type, Object initializer, boolean nullable, boolean confidential) {

			GwtRuntimeProperty result = new GwtRuntimeProperty(entityType, name, nullable, confidential);

			result.setPropertyType(type);
			result.setInitializer(initializer);

			return result;
		}

		/** @see GenericAccessorMethods#buildGetterSetterAccessors */
		private Pair<JavaScriptObject, JavaScriptObject> buildGetterSetterAccessors(GwtScriptProperty property) {
			return GenericAccessorMethods.buildGetterSetterAccessors(property);
		}

		/** @see GenericAccessorMethods#buildJsConvertingAccessors */
		private Pair<JavaScriptObject, JavaScriptObject> buildVirtualPropertyAccessors(PropertyBinding propertyBinding, Property property) {
			Tuple3<TypeCode, TypeCode, TypeCode> typeCodes = resolveCollectionKeyValueTypeCodes(property);

			return buildVirtualPropertyAccessors(propertyBinding, property, typeCodes);
		}

		private Tuple3<TypeCode, TypeCode, TypeCode> resolveCollectionKeyValueTypeCodes(Property property) {
			GenericModelType type = property.getType();
			TypeCode collectionType = null;
			TypeCode keyType = null;
			TypeCode valueType = type.getTypeCode();

			if (type.isCollection()) {
				collectionType = valueType;

				if (collectionType == TypeCode.mapType) {
					MapType mapType = (MapType) type;
					keyType = mapType.getKeyType().getTypeCode();
					valueType = mapType.getValueType().getTypeCode();
				} else {
					valueType = ((CollectionType) type).getCollectionElementType().getTypeCode();
				}
			}

			return Tuple3.of(collectionType, keyType, valueType);
		}

		private Pair<JavaScriptObject, JavaScriptObject> buildVirtualPropertyAccessors( //
				PropertyBinding propertyBinding, Property property, Tuple3<TypeCode, TypeCode, TypeCode> typeCodes) {

			return GenericAccessorMethods.buildJsConvertingAccessors( //
					property, propertyBinding.getterFunction, propertyBinding.setterFunction, //
					typeCodes.val0(), typeCodes.val1(), typeCodes.val2());
		}

		private Pair<JavaScriptObject, JavaScriptObject> buildJsConvertingPropertyAccessors( //
				Property property, Tuple3<TypeCode, TypeCode, TypeCode> typeCodes) {

			return GenericAccessorMethods.buildJsConvertingPropertyAccessors(property, typeCodes.val0(), typeCodes.val1(), typeCodes.val2());
		}

		// let's take all other methods we can find on all the other super-types
		private void takeEverythingElseFromAllTheSuperTypes() {
			Map<String, Object> mergedSuperProperties = newMap();
			for (GwtScriptEntityType<?> superType : entityType.getGwtScriptSuperTypes()) {
				Object superPrototype = superType.getProtoInstance();
				ScriptOnlyItwTools.forEachEntry(superPrototype, mergedSuperProperties::put);
			}

			for (Entry<String, Object> entry : mergedSuperProperties.entrySet()) {
				String propertyName = entry.getKey();

				if (!enhancedPrototype.hasProperty(propertyName))
					enhancedPrototype.setProperty(propertyName, entry.getValue());
			}
		}

		private void prepareToStringToSelectiveInfo() {
			if (etb.toStringMethod != null)
				ScriptOnlyItwTools.setProperty(entityType, RuntimeMethodNames.abstractEntityTypeToString(), etb.toStringMethod);

			if (etb.getSelectiveInformationForMethod != null)
				ScriptOnlyItwTools.setProperty(entityType, RuntimeMethodNames.abstractEntityTypeGetSelectiveInformationFor(),
						etb.getSelectiveInformationForMethod);
		}

	}

	/**
	 * import {@link GmtsEnhancedEntityStub} import {@link PropertyAccessInterceptor}
	 */
	// @formatter:off
	private static native JavaScriptObject createObjectGetter(Property property) /*-{
		return function() {
		return this.@GmtsEnhancedEntityStub::pai.
			@PropertyAccessInterceptor::getProperty(Lcom/braintribe/model/generic/reflection/Property;Lcom/braintribe/model/generic/GenericEntity;Z)
				(property, this, false);
		};	
	}-*/;

	private static native JavaScriptObject createObjectSetter(Property property) /*-{
		return function(value) {
		return this.@GmtsEnhancedEntityStub::pai.
			@PropertyAccessInterceptor::setProperty(Lcom/braintribe/model/generic/reflection/Property;Lcom/braintribe/model/generic/GenericEntity;Ljava/lang/Object;Z)
				(property, this, value);
		};	
	}-*/;
	// @formatter:on

	protected static void log(String msg) {
		Document document = Document.get();
		PreElement preElement = document.createPreElement();
		preElement.appendChild(document.createTextNode(msg));
		preElement.getStyle().setMargin(0, Unit.PX);
		document.getBody().appendChild(preElement);
	}

	private EntityInitializer[] toArrayOrNull(List<EntityInitializer> initializers) {
		int size = initializers.size();
		return size == 0 ? null : initializers.toArray(new EntityInitializer[size]);
	}

}

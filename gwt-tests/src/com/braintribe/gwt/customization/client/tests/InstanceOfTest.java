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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.braintribe.gwt.customization.client.tests.model.assignability.AbstractDerived;
import com.braintribe.gwt.customization.client.tests.model.assignability.AbstractDerivedAbstractBase;
import com.braintribe.gwt.customization.client.tests.model.assignability.Derived;
import com.braintribe.gwt.customization.client.tests.model.assignability.DerivedAbstractBase;
import com.braintribe.gwt.customization.client.tests.model.assignability.DiamondAbstractBase;
import com.braintribe.gwt.customization.client.tests.model.assignability.DiamondAbstractIntermediate1;
import com.braintribe.gwt.customization.client.tests.model.assignability.DiamondAbstractIntermediate2;
import com.braintribe.gwt.customization.client.tests.model.assignability.DiamondTop;
import com.braintribe.gwt.customization.client.tests.model.assignability.StandaloneAbstractBase;
import com.braintribe.gwt.customization.client.tests.model.initializer.InitializedEntity;
import com.braintribe.gwt.customization.client.tests.model.initializer.InitializedSubEntity;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.StandardStringIdentifiable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmEnumType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.meta.data.HasMetaData;
import com.braintribe.model.resource.Resource;

/**
 * @author peter.gazdik
 */
public class InstanceOfTest extends AbstractGmGwtTest {

	private static final String SYNTHETIC = "Synthetic";
	private static final String IS_NOT_ASSIGNABLE_TO_TYPE = " is not assignable to ";
	private static final String IS_ASSIGNABLE_TO_TYPE = " is assignable to ";

	@Override
	protected void tryRun() throws GmfException {

		// positives
		logSeparator();
		log("checking positive cases for compile-time types");
		checkAssignability(GenericEntity.T, Resource.T, true);
		checkAssignability(StandardStringIdentifiable.T, Resource.T, true);
		checkAssignability(HasMetaData.T, GmMetaModel.T, true);
		checkAssignability(HasMetaData.T, GmEntityType.T, true);
		checkAssignability(HasMetaData.T, GmEnumType.T, true);

		// negatives
		logSeparator();
		log("checking negative cases for compile-time types");
		checkAssignability(StandardIdentifiable.T, StandardStringIdentifiable.T, false);
		checkAssignability(HasMetaData.T, Resource.T, false);
		checkAssignability(Resource.T, GmMetaModel.T, false);
		checkAssignability(Resource.T, GmEntityType.T, false);
		checkAssignability(Resource.T, GmEnumType.T, false);

		logSeparator();
		log("checking runtime-time types");
		log("creating models");
		List<EntityType<?>> toBeVirtualized = Arrays.asList(InitializedSubEntity.T, InitializedEntity.T);
		Set<String> typeNames = toBeVirtualized.stream().map(t -> t.getTypeSignature()).collect(Collectors.toSet());
		GmMetaModel testModel = generateModel("test-model", toBeVirtualized);

		for (GmType type : testModel.getTypes()) {
			if (type.isEntity() && typeNames.contains(type.getTypeSignature())) {
				GmEntityType entityType = (GmEntityType) type;
				entityType.setTypeSignature(entityType.getTypeSignature() + SYNTHETIC);
			}
		}

		List<EntityType<?>> toBeVirtualized2 = Arrays.asList( //
				Derived.T, //
				DerivedAbstractBase.T, //
				StandaloneAbstractBase.T, //
				AbstractDerivedAbstractBase.T, //
				AbstractDerived.T, //
				DiamondAbstractBase.T, //
				DiamondAbstractIntermediate1.T, //
				DiamondAbstractIntermediate2.T, //
				DiamondTop.T //
		);

		GmMetaModel testModel2 = generateModel("test-model2", toBeVirtualized2);

		GmEntityType runtimeDerivedAbstractBase = createDerivate("com.braintribe.model.virtual.RuntimeDerivedAbstractBase", testModel2,
				DerivedAbstractBase.T);
		GmEntityType runtimeStandaloneAbstractBase = createDerivate("com.braintribe.model.virtual.RuntimeStandaloneAbstractBase", testModel2,
				StandaloneAbstractBase.T);
		GmEntityType runtimeFromAbstractDerivedAbstractBase = createDerivate("com.braintribe.model.virtual.RuntimeFromAbstractDerivedAbstractBase",
				testModel2, AbstractDerivedAbstractBase.T);
		GmEntityType runtimeFromDerived = createDerivate("com.braintribe.model.virtual.RuntimeFromDerived", testModel2, Derived.T);
		GmEntityType runtimeFromDiamondAbstractBase = createDerivate("com.braintribe.model.virtual.RuntimeFromDiamondAbstractBase", testModel2,
				DiamondAbstractBase.T);
		GmEntityType runtimeFromDiamoneIntermediate1 = createDerivate("com.braintribe.model.virtual.RuntimeFromDiamoneIntermediate1", testModel2,
				DiamondAbstractIntermediate1.T);

		log("weaving models");
		testModel.deploy();
		testModel2.deploy();

		EntityType<?> initializedSubEntitySyntheticType = GMF.getTypeReflection().getType(InitializedSubEntity.T.getTypeSignature() + SYNTHETIC);
		EntityType<?> initializedEntitySyntheticType = GMF.getTypeReflection().getType(InitializedEntity.T.getTypeSignature() + SYNTHETIC);

		EntityType<?> runtimeDerivedAbstractBaseType = GMF.getTypeReflection().getType(runtimeDerivedAbstractBase.getTypeSignature());
		EntityType<?> runtimeStandaloneAbstractBaseType = GMF.getTypeReflection().getType(runtimeStandaloneAbstractBase.getTypeSignature());
		EntityType<?> runtimeFromAbstractDerivedAbstractBaseType = GMF.getTypeReflection()
				.getType(runtimeFromAbstractDerivedAbstractBase.getTypeSignature());
		EntityType<?> runtimeFromDerivedType = GMF.getTypeReflection().getType(runtimeFromDerived.getTypeSignature());
		EntityType<?> runtimeFromDiamondAbstractBaseType = GMF.getTypeReflection().getType(runtimeFromDiamondAbstractBase.getTypeSignature());
		EntityType<?> runtimeFromDiamoneIntermediate1Type = GMF.getTypeReflection().getType(runtimeFromDiamoneIntermediate1.getTypeSignature());

		log("checking positive cases for runtime types");
		checkAssignability(GenericEntity.T, initializedEntitySyntheticType, true);
		checkAssignability(GenericEntity.T, initializedSubEntitySyntheticType, true);
		checkAssignability(initializedEntitySyntheticType, initializedSubEntitySyntheticType, true);
		checkAssignability(StandaloneAbstractBase.T, runtimeStandaloneAbstractBaseType, true);
		checkAssignability(DerivedAbstractBase.T, runtimeDerivedAbstractBaseType, true);
		checkAssignability(AbstractDerivedAbstractBase.T, runtimeFromAbstractDerivedAbstractBaseType, true);
		checkAssignability(AbstractDerivedAbstractBase.T, AbstractDerived.T, true);
		checkAssignability(Derived.T, runtimeFromDerivedType, true);
		checkAssignability(DiamondAbstractBase.T, runtimeFromDiamondAbstractBaseType, true);
		checkAssignability(DiamondAbstractBase.T, runtimeFromDiamoneIntermediate1Type, true);

		log("checking negative cases for runtime types");
		checkAssignability(StandardIdentifiable.T, initializedEntitySyntheticType, false);
		checkAssignability(Resource.T, initializedSubEntitySyntheticType, false);
		checkAssignability(initializedSubEntitySyntheticType, initializedEntitySyntheticType, false);
	}

	private GmEntityType createDerivate(String typeSignature, GmMetaModel model, EntityType<?>... superTypes) {

		GmEntityType derivate = GmEntityType.T.create();
		derivate.setTypeSignature(typeSignature);
		derivate.setGlobalId("type:" + typeSignature);
		derivate.setIsAbstract(false);

		for (EntityType<?> superType : superTypes) {
			GmEntityType gmSuperType = findType(model, superType);
			derivate.getSuperTypes().add(gmSuperType);
		}

		model.getTypes().add(derivate);

		return derivate;
	}

	private GmEntityType findType(GmMetaModel model, EntityType<?> type) {
		return (GmEntityType) model.getTypes() //
				.stream() //
				.filter(t -> t.getTypeSignature().equals(type.getTypeSignature())) //
				.findFirst() //
				.get();
	}

	private void checkAssignability(EntityType<?> type, EntityType<?> otherType, boolean expectedAssignability) {
		// type assignability
		boolean assignable = type.isAssignableFrom(otherType);
		String issue = expectedAssignability ? IS_ASSIGNABLE_TO_TYPE : IS_NOT_ASSIGNABLE_TO_TYPE;

		if (assignable != expectedAssignability) {
			logError("  fail: type " + otherType.getShortName() + issue + type.getShortName());
		} else {
			log("  match: type " + otherType.getShortName() + issue + type.getShortName());
		}

		// instanceof
		if (!otherType.isAbstract()) {
			GenericEntity instance = otherType.create();
			boolean instanceOf = type.isInstance(instance);

			if (instanceOf != expectedAssignability) {
				logError("  fail: instance of " + instance.entityType().getShortName() + issue + type.getShortName());
			} else {
				log("  match: instance of " + instance.entityType().getShortName() + issue + type.getShortName());
			}
		}

	}

}

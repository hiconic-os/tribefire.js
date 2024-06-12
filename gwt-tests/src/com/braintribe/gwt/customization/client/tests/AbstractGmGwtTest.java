package com.braintribe.gwt.customization.client.tests;

import static com.braintribe.utils.lcd.CollectionTools2.asList;
import static com.braintribe.utils.lcd.CollectionTools2.newMap;
import static com.braintribe.utils.lcd.CollectionTools2.nullSafe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.builder.meta.MetaModelBuilder;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.GenericModelTypeReflection;
import com.braintribe.model.generic.value.EnumReference;
import com.braintribe.model.meta.GmEntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.meta.GmProperty;
import com.braintribe.model.meta.GmType;
import com.braintribe.model.meta.info.GmPropertyInfo;
import com.braintribe.model.util.meta.NewMetaModelGeneration;

/**
 * @author peter.gazdik
 */
public abstract class AbstractGmGwtTest extends AbstractGwtTest {

	public static final GenericModelTypeReflection typeReflection = GMF.getTypeReflection();

	// ##########################################
	// ## . . . . . . Util methods . . . . . . ##
	// ##########################################

	protected GmMetaModel generateModel(String name, EntityType<?>... types) {
		return generateModel(name, asList(types));
	}

	protected GmMetaModel generateModel(String name, Collection<EntityType<?>> types) {
		return new NewMetaModelGeneration().buildMetaModel(name, types);
	}

	protected EntityType<?> getDynamicCounterpart(EntityType<?> type) {
		return GMF.getTypeReflection().getEntityType(makeSignatureDynamic(type.getTypeSignature()));
	}

	protected EnumType getDynamicCounterpart(EnumType type) {
		return GMF.getTypeReflection().getEnumType(makeSignatureDynamic(type.getTypeSignature()));
	}

	protected void makeSignaturesDynamic(GmMetaModel metaModel) {
		makeSignaturesDynamic(metaModel, true);
	}

	protected void makeSignaturesDynamic(GmMetaModel metaModel, boolean alsoEnums) {
		for (GmType gmType : nullSafe(metaModel.getTypes())) {
			if (gmType.isGmEntity() || (alsoEnums && gmType.isGmEnum())) {
				String ts = gmType.getTypeSignature();
				if (ts.startsWith("com.braintribe.gwt.customization.client.tests.model") && !ts.contains("non_dynamic"))
					gmType.setTypeSignature(makeSignatureDynamic(ts));

				if (alsoEnums)
					if (gmType.isGmEntity())
						makeInitializersSignaturesDynamic((GmEntityType) gmType);
			}
		}
	}

	private void makeInitializersSignaturesDynamic(GmEntityType t) {
		makeInitializersSignaturesDynamic(t.getProperties());
		makeInitializersSignaturesDynamic(t.getPropertyOverrides());
	}

	private void makeInitializersSignaturesDynamic(List<? extends GmPropertyInfo> ps) {
		for (GmPropertyInfo p : ps) {
			Object i = p.getInitializer();
			if (i instanceof EnumReference) {
				EnumReference oldEr = (EnumReference) i;
				EnumReference newEr = EnumReference.T.create();
				newEr.setTypeSignature(makeSignatureDynamic(oldEr.getTypeSignature()));
				newEr.setConstant(oldEr.getConstant());
				p.setInitializer(newEr);
			}
		}
	}

	protected String makeSignatureDynamic(String ts) {
		int lastDot = ts.lastIndexOf(".");
		return ts.substring(0, lastDot) + ".dynamic" + ts.substring(lastDot);
	}

	protected void ensureModelTypes(GmMetaModel metaModel) throws GmfException {
		typeReflection.deploy(metaModel);
	}

	protected GmEntityType createDynamicSubType(GmEntityType gmEntityType) {
		GmEntityType subType = copy(gmEntityType);
		subType.setTypeSignature(makeSignatureDynamic(gmEntityType.getTypeSignature()));
		subType.setSuperTypes(asList(gmEntityType));

		return subType;
	}

	protected Map<String, GmEntityType> indexEntityTypes(GmMetaModel metaModel) {
		Map<String, GmEntityType> result = newMap();

		for (GmType gmType : metaModel.getTypes()) {
			if (gmType.isGmEntity()) {
				result.put(gmType.getTypeSignature(), (GmEntityType) gmType);
			}
		}

		return result;
	}

	protected GmEntityType copy(GmEntityType gmEntityType) {
		GmEntityType result = MetaModelBuilder.entityType(gmEntityType.getTypeSignature());
		result.setTypeSignature(gmEntityType.getTypeSignature());
		result.setSuperTypes(gmEntityType.getSuperTypes());
		result.setDeclaringModel(gmEntityType.getDeclaringModel());
		result.setIsAbstract(gmEntityType.getIsAbstract());
		result.setProperties(new ArrayList<GmProperty>());

		return result;
	}

	@SafeVarargs
	protected static GmMetaModel modelForTypes(EntityType<? extends GenericEntity>... types) {
		return new NewMetaModelGeneration().buildMetaModel("gm:Gwt27Model", asList(types));
	}

}

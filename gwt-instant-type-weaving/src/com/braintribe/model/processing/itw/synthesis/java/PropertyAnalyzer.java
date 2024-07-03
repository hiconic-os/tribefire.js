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
package com.braintribe.model.processing.itw.synthesis.java;

import static com.braintribe.utils.lcd.CollectionTools2.newList;
import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.braintribe.model.generic.annotation.meta.api.synthesis.AnnotationDescriptor;
import com.braintribe.model.generic.annotation.meta.synthesis.MdaSynthesis;
import com.braintribe.model.processing.itw.InitializerTools;
import com.braintribe.model.processing.itw.asm.AsmAnnotationInstance;
import com.braintribe.model.processing.itw.asm.AsmClass;
import com.braintribe.model.processing.itw.asm.AsmClassPool;
import com.braintribe.model.processing.itw.asm.AsmMethod;
import com.braintribe.model.processing.itw.asm.AsmType;
import com.braintribe.model.processing.itw.asm.TypeBuilder;
import com.braintribe.model.processing.itw.synthesis.java.PropertyAnalysis.PropertyDescription;
import com.braintribe.model.processing.itw.synthesis.java.PropertyAnalysis.SetterGetterAchievement;
import com.braintribe.model.processing.itw.tools.ItwTools;
import com.braintribe.model.weaving.ProtoGmCollectionType;
import com.braintribe.model.weaving.ProtoGmProperty;
import com.braintribe.model.weaving.ProtoGmSimpleType;
import com.braintribe.model.weaving.ProtoGmType;
import com.braintribe.model.weaving.info.ProtoGmPropertyInfo;
import com.braintribe.model.weaving.override.ProtoGmPropertyOverride;

/**
 * 
 */
public class PropertyAnalyzer {

	private final JavaTypeSynthesis jts;

	public PropertyAnalyzer(JavaTypeSynthesis jts) {
		this.jts = jts;
	}

	public PropertyAnalysis analyzeProperties(TypeBuilder typeBuilder, Map<String, ProtoGmPropertyInfo[]> properties)
			throws JavaTypeSynthesisException {

		return analyzeProperties(typeBuilder.getPreliminaryClass(), properties);
	}

	public PropertyAnalysis analyzeProperties(AsmClass asmClass, Map<String, ProtoGmPropertyInfo[]> properties) throws JavaTypeSynthesisException {
		PropertyAnalysis result = new PropertyAnalysis();

		int propertyIndex = 0;
		for (ProtoGmPropertyInfo[] propertyLineage : properties.values()) {
			PropertyDescription description = getPropertyDescription(asmClass, propertyLineage);
			result.propertyDescriptions.add(description);
			result.propertyNames.add(description.property.getName());
		}

		result.numberOfProperties = propertyIndex;

		return result;
	}

	private PropertyDescription getPropertyDescription(AsmClass asmClass, ProtoGmPropertyInfo[] propertyLineage) throws JavaTypeSynthesisException {
		ProtoGmPropertyInfo propertyInfo = propertyLineage[0];
		ProtoGmProperty property = propertyInfo.relatedProperty();
		ProtoGmType propertyType = property.getType();
		AsmClass actualPropertyClass = jts.ensureClass(propertyType);
		AsmClass accessPropertyClass = actualPropertyClass;

		String getterName = ItwTools.getGetterName(property);
		String setterName = ItwTools.getSetterName(property);

		AsmMethod getterMethod;

		// It should be enough to check nullable, as non-simple types must be nullable, but just in case we treat it leniently
		if (property.getNullable() || (!(propertyType instanceof ProtoGmSimpleType))) {
			getterMethod = asmClass.getMethod(getterName, accessPropertyClass);

		} else {
			AsmClass primitiveClass = getPrimitiveClass((ProtoGmSimpleType) propertyType);
			if (primitiveClass == null) {
				// again, if someone marked a simple type as nullable, even if it's one that doesn't allow it, we treat it leniently
				getterMethod = asmClass.getMethod(getterName, accessPropertyClass);

			} else {
				getterMethod = asmClass.getMethod(getterName, primitiveClass);
				accessPropertyClass = primitiveClass;
			}
		}

		PropertyDescription description = new PropertyDescription();
		description.ownerType = asmClass;
		if (propertyType instanceof ProtoGmCollectionType) {
			description.actualPropertyClass = jts.ensureClassAsGenericIfNeeded(propertyType);
			description.accessPropertyClass = description.actualPropertyClass;

		} else {
			description.actualPropertyClass = actualPropertyClass;
			description.accessPropertyClass = accessPropertyClass;
		}
		description.propertyInfo = propertyInfo;
		description.property = property;
		description.declaredTypeOverride = resolveTypeOverride(propertyInfo);
		description.allTypeOverrides = allTypeOverrides(propertyLineage);
		description.fieldName = ItwTools.getFieldName(property.getName());
		description.getterName = getterName;
		description.setterName = setterName;
		description.achievement = SetterGetterAchievement.missing;

		if (getterMethod != null) {
			AsmMethod setterMethod = asmClass.getMethod(setterName, AsmClassPool.voidType, accessPropertyClass);
			if (setterMethod == null)
				throw new JavaTypeSynthesisException("getter/setter inconsistency: No suitable setter for existing getter of property "
						+ asmClass.getName() + "." + property.getName());

			description.achievement = SetterGetterAchievement.declared;
		}

		return description;
	}

	private List<AsmType> allTypeOverrides(ProtoGmPropertyInfo[] propertyLineage) {
		List<AsmType> result = newList();
		for (ProtoGmPropertyInfo propertyInfo : propertyLineage) {
			AsmType typeOverride = resolveTypeOverride(propertyInfo);
			if (typeOverride != null)
				result.add(typeOverride);
		}

		return result.isEmpty() ? emptyList() : result;
	}

	private AsmType resolveTypeOverride(ProtoGmPropertyInfo propertyInfo) {
		if (propertyInfo instanceof ProtoGmPropertyOverride) {
			ProtoGmType typeOverride = ((ProtoGmPropertyOverride) propertyInfo).getTypeOverride();
			if (typeOverride == null)
				return null;

			return jts.ensureClassAsGenericIfNeeded(typeOverride);
		}

		return null;
	}

	public List<AsmAnnotationInstance> getAnnotations(PropertyDescription pd) {
		// if getter inherited (i.e. super type exists, but our type doesn't)
		ProtoGmPropertyInfo p = pd.propertyInfo;
		Object initializer = p.getInitializer();

		if (!pd.ownerType.getName().equals(p.declaringTypeInfo().addressedType().getTypeSignature()) && initializer == null)
			return emptyList();

		List<AsmAnnotationInstance> result = newList();

		if (initializer != null) {
			String initializerString = InitializerTools.stringifyInitializer(initializer);
			result.add(new AsmAnnotationInstance(AsmClassPool.initializerType, "value", initializerString));
		}

		// TODO what if initializer is null but we still have some additional annotations, despite the property being an override?

		// Annotations resulting from configured meta-data
		for (AnnotationDescriptor annotationDescriptor : MdaSynthesis.synthesizeMetaDataAnnotations(p.getGlobalId(), p.getMetaData()))
			result.add(jts.toAsmAnnotationInstance(annotationDescriptor));

		return result.isEmpty() ? Collections.<AsmAnnotationInstance> emptyList() : result;
	}

	public AsmClass getPrimitiveClass(ProtoGmSimpleType gmSimpleType) {
		switch (gmSimpleType.getTypeSignature()) {
			case "integer":
				return AsmClassPool.intType;
			case "boolean":
				return AsmClassPool.booleanType;
			case "long":
				return AsmClassPool.longType;
			case "float":
				return AsmClassPool.floatType;
			case "double":
				return AsmClassPool.doubleType;
			default:
				return null;
		}
	}

}

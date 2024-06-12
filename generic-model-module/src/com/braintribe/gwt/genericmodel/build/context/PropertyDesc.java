// ============================================================================
// BRAINTRIBE TECHNOLOGY GMBH - www.braintribe.com
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2018 - All Rights Reserved
// It is strictly forbidden to copy, modify, distribute or use this code without written permission
// To this file the Braintribe License Agreement applies.
// ============================================================================

package com.braintribe.gwt.genericmodel.build.context;

import com.braintribe.gwt.genericmodel.client.itw.GenericAccessorMethods;
import com.braintribe.gwt.genericmodel.client.itw.GwtScriptProperty;
import com.braintribe.model.generic.reflection.TypeCode;
import com.braintribe.model.processing.itw.InitializerTools;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JType;

import jsinterop.context.JsKeywords;

/**
 * Uses {@link GwtScriptProperty}
 */
public class PropertyDesc {

	public String typeRef;
	public String returnType;
	public String originalType;
	public String name;
	public String Name;
	public JType jType;
	public EntityDesc ownerTypeDesc;
	public JClassType declaringType; // Type where we want to declare our property, i.e. might be an overlay
	public EntityDesc declaringTypeDesc;
	public boolean isOverlay; // true iff declaringTypeDesc != ownerTypeDesc
	public String defaultLiteral;
	public boolean isPrimitive;
	public boolean isConfidential;
	public String initializerString;
	public String initializerLiteralOrSupplier;

	private TypeCode collectionTypeCode;
	private TypeCode keyTypeCode;
	private TypeCode valueTypeCode;

	public String getSingletonInstanceRef() {
		return getPropertyWrapperClassName() + "." + getLiteralName();
	}

	public String getSingletonInstanceJsniRef() {
		return "@" + getPropertyWrapperClassName() + "::" + getLiteralName();
	}

	private String getPropertyWrapperClassName() {
		return declaringTypeDesc.getEnhancedClassFullName() + "._Properties";
	}

	/** @see #getValueTypeCodeJsniRefIfSimpleOrObject() */
	public String getCollectionTypeCodeJsniRefOrNull() {
		return toTypeCodeJsniRef(collectionTypeCode);
	}

	/** @see #getValueTypeCodeJsniRefIfSimpleOrObject() */
	public String getKeyTypeCodeJsniRefIfSimpleOrObject() {
		return toTypeCodeJsniRef(keyTypeCode);
	}

	/** Type code to pass to {@link GenericAccessorMethods#buildJsConvertingAccessors} */
	public String getValueTypeCodeJsniRefIfSimpleOrObject() {
		return toTypeCodeJsniRef(valueTypeCode);
	}

	private String toTypeCodeJsniRef(TypeCode tc) {
		return tc == null ? "null" : "@com.braintribe.model.generic.reflection.TypeCode::" + tc.name();
	}

	public String virtualPropertyName() {
		return JsKeywords.javaIdentifierToJs(name);
	}

	public boolean isPrimitive() {
		return isPrimitive;
	}

	public String getNullableFlag() {
		return boolToString(!isPrimitive);
	}

	public String getConfidentialFlag() {
		return boolToString(isConfidential);
	}

	private String boolToString(boolean b) {
		return b ? "true" : "false";
	}

	public String getDefaultLiteral() {
		return defaultLiteral;
	}

	public JType getDeclaringType() {
		return declaringType;
	}

	public EntityDesc getDeclaringTypeDesc() {
		return declaringTypeDesc;
	}

	public boolean getIsInheritedFromSuperclass() {
		return ownerTypeDesc.isInheritedFromSuperclass(name);
	}

	public boolean getIsOverlay() {
		return isOverlay;
	}

	public JType getJType() {
		return jType;
	}

	public String getTypeRef() {
		return typeRef;
	}

	public void setOriginalType(String originalType) {
		this.originalType = originalType;
		String valueType = originalType;
		String[] s = this.originalType.split("<");
		if (s.length > 1) {
			valueType = s[1];
			String collectionType = s[0];
			if (collectionType.startsWith("java.util.List")) {
				collectionTypeCode = TypeCode.listType;
			} else if (collectionType.startsWith("java.util.Set")) {
				collectionTypeCode = TypeCode.setType;
			} else if (collectionType.startsWith("java.util.Map")) {
				collectionTypeCode = TypeCode.mapType;
				s = valueType.split(",");
				valueType = s[1].trim();
				keyTypeCode = resolveSimpleOrObjectTypeCode(s[0]);
			}
		}

		valueTypeCode = resolveSimpleOrObjectTypeCode(valueType);		
	}

	private TypeCode resolveSimpleOrObjectTypeCode(String elementType) {
		if (elementType.startsWith("java.lang.Boolean") || elementType.startsWith("boolean"))
			return TypeCode.booleanType;

		if (elementType.startsWith("java.lang.String"))
			return TypeCode.stringType;

		if (elementType.startsWith("java.lang.Integer") || elementType.startsWith("int"))
			return TypeCode.integerType;

		if (elementType.startsWith("java.lang.Float") || elementType.startsWith("float"))
			return TypeCode.floatType;

		if (elementType.startsWith("java.lang.Double") || elementType.startsWith("double"))
			return TypeCode.doubleType;

		if (elementType.startsWith("java.lang.Long") || elementType.startsWith("long"))
			return TypeCode.longType;

		if (elementType.startsWith("java.math.BigDecimal"))
			return TypeCode.decimalType;

		if (elementType.startsWith("java.util.Date"))
			return TypeCode.dateType;

		if (elementType.startsWith("java.lang.Object"))
			return TypeCode.objectType;

		return null;
	}

	public String getOriginalType() {
		return originalType;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getName() {
		return name;
	}

	public String getLiteralName() {
		return Name;
	}

	public String getSetterName() {
		return "set" + Name;
	}

	public String getGetterName() {
		return "get" + Name;
	}

	/** Might also be "null", i.e. this can be used always in the code. */
	public String getInitializerLiteralOrSupplier() {
		return initializerLiteralOrSupplier;
	}

	public boolean getHasNonNullInitializer() {
		return initializerString != null && !InitializerTools.NULL_STRING.equals(initializerString);
	}

	public static String firstLetterToUpperCase(String name) {
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

}

package com.braintribe.gwt.tribefirejs.client.tools;

import static com.braintribe.gwt.genericmodel.client.itw.GenericAccessorMethods.jToJsListConverter;
import static com.braintribe.gwt.genericmodel.client.itw.GenericAccessorMethods.jToJsMapConverter;
import static com.braintribe.gwt.genericmodel.client.itw.GenericAccessorMethods.jToJsSetConverter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.ArrayIface;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Arrayish;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.MapIface;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Mapish;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.SetIface;
import com.braintribe.gwt.genericmodel.client.jsinterop.collectionish.Setish;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.processing.session.impl.session.collection.EnhancedList;
import com.braintribe.model.processing.session.impl.session.collection.EnhancedMap;
import com.braintribe.model.processing.session.impl.session.collection.EnhancedSet;

import jsinterop.annotations.JsMethod;
import jsinterop.utils.Lambdas.JsUnaryFunction;

public class JsCollectionTools {

	@JsMethod(namespace = GmCoreApiInteropNamespaces.util)
	public static <E> ArrayIface<E> createArrayish(GenericModelType elementType) {
		JsUnaryFunction<List<?>, ?> converter = (JsUnaryFunction<List<?>, ?>) jToJsListConverter(elementType.getTypeCode());

		EnhancedList<E> jList = new EnhancedList<>(GMF.getTypeReflection().getListType(elementType));

		return (Arrayish<E>) converter.call(jList);
	}

	@JsMethod(namespace = GmCoreApiInteropNamespaces.util)
	public static <E> SetIface<E> createSetish(GenericModelType elementType) {
		JsUnaryFunction<Set<?>, ?> converter = (JsUnaryFunction<Set<?>, ?>) jToJsSetConverter(elementType.getTypeCode());

		EnhancedSet<E> jSet = new EnhancedSet<>(GMF.getTypeReflection().getSetType(elementType));

		return (Setish<E>) converter.call(jSet);
	}

	@JsMethod(namespace = GmCoreApiInteropNamespaces.util)
	public static <K, V> MapIface<K, V> createMapish(GenericModelType keyType, GenericModelType valueType) {
		JsUnaryFunction<Map<?, ?>, ?> converter = (JsUnaryFunction<Map<?, ?>, ?>) jToJsMapConverter(keyType.getTypeCode(), valueType.getTypeCode());

		EnhancedMap<K, V> jMap = new EnhancedMap<>(GMF.getTypeReflection().getMapType(keyType, valueType));

		return (Mapish<K, V>) converter.call(jMap);
	}

}

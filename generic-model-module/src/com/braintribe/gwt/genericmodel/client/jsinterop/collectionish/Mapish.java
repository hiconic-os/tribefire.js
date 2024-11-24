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
package com.braintribe.gwt.genericmodel.client.jsinterop.collectionish;

import static com.braintribe.model.generic.reflection.EssentialCollectionTypes.TYPE_MAP;

import java.util.Iterator;
import java.util.Map;

import com.braintribe.gwt.browserfeatures.client.interop.JsIterableIterator;
import com.braintribe.gwt.browserfeatures.client.interop.JsMap;
import com.braintribe.gwt.genericmodel.client.itw.GenericAccessorMethods;
import com.braintribe.gwt.genericmodel.client.reflect.TypePackage;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.Messages.Optional;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.annotations.custom.TsIgnore;
import jsinterop.utils.Lambdas.JsUnaryFunction;

@TsIgnore
@JsType(name = "Map", namespace = TypePackage.GM_TYPE_NAMESPACE)
public class Mapish<K, V> extends AbstractCollectionish<V> implements MapIface<K, V> {

	private final Map<Object, Object> map;

	private final JsUnaryFunction<Object, K> keyJavaToJs;
	private final JsUnaryFunction<K, Object> keyJsToJava;

	public Mapish( //
			Map<Object, Object> map, //
			JsUnaryFunction<Object, V> javaToJs, JsUnaryFunction<V, Object> jsToJava, //
			JsUnaryFunction<Object, K> keyJavaToJs, JsUnaryFunction<K, Object> keyJsToJava) {

		super(map, javaToJs, jsToJava);
		this.keyJavaToJs = map != null ? keyJavaToJs : GenericAccessorMethods.jToJsNonCollectionConverter();
		this.keyJsToJava = map != null ? keyJsToJava : GenericAccessorMethods.jsToJNonCollectionConverter();
		this.map = map != null ? map : TYPE_MAP.createPlain();
	}

	@Override
	public Object wrappedCollection() {
		return map;
	}

	// ############################################
	// ## . . . . . es2015.collections . . . . . ##
	// ############################################

	@JsProperty(name = "size")
	public final int size() {
		return map.size();
	}

	public void clear() {
		map.clear();
	}

	public boolean delete(K jsKey) {
		Object jKey = keyToJavaIfNeeded(jsKey);
		boolean result = map.containsKey(jKey);
		if (result)
			map.remove(jKey);
		return result;
	}

	public native void forEach(JavaScriptObject callbackfn, @Optional JavaScriptObject thisArg) /*-{
		return this.@Mapish::toJsMap()().forEach(callbackfn, thisArg);
	}-*/;

	public V get(K jsKey) {
		Object jKey = keyToJavaIfNeeded(jsKey);
		if (!map.containsKey(jKey))
			return undefined();

		Object jValue = map.get(jKey);
		return toJsIfNeeded(jValue);
	}

	public boolean has(K jsKey) {
		Object jKey = keyToJavaIfNeeded(jsKey);
		return map.containsKey(jKey);
	}

	public Mapish<K, V> set(K jsKey, V jsValue) {
		Object jKey = keyToJavaIfNeeded(jsKey);
		Object jValue = toJavaIfNeeded(jsValue);
		map.put(jKey, jValue);
		return this;
	}

	// ############################################
	// ## . . . . . . es2015.iterable . . . . . .##
	// ############################################

	// Real signature: entries(): IterableIterator<[K, V]>;;
	public native JsIterableIterator<JavaScriptObject> entries() /*-{
		return this.@Mapish::toJsMap()().entries();
	}-*/;

	public native JsIterableIterator<K> keys() /*-{
		return this.@Mapish::toJsMap()().keys();
	}-*/;

	public native JsIterableIterator<V> values() /*-{
		return this.@Mapish::toJsMap()().values();
	}-*/;

	// ############################################
	// ## . . . . . . . . OTHER . . . . . . . . .##
	// ############################################

	private JsMap<K, V> toJsMap() {
		return toJsMapHelper(map.entrySet().iterator());
	}

	private native JsMap<K, V> toJsMapHelper(Iterator<Map.Entry<Object, Object>> it) /*-{
		var result = new $wnd.Map();
		while (it.hasNext()) {
			var e = it.next();
			var k =  this.@Mapish::keyToJsIfNeeded(*)(e.getKey());;
			var v = this.@Mapish::toJsIfNeeded(*)(e.getValue());
			result.set(k, v);
		}
	
		return result;
	}-*/;

	private K keyToJsIfNeeded(Object o) {
		return keyJavaToJs == null || o == null ? (K) o : keyJavaToJs.call(o);
	}

	private Object keyToJavaIfNeeded(K k) {
		return keyJsToJava == null || k == null ? k : keyJsToJava.call(k);
	}

}

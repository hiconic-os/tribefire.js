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

import static com.braintribe.model.generic.reflection.EssentialCollectionTypes.TYPE_SET;

import java.util.Set;

import com.braintribe.gwt.browserfeatures.client.interop.JsIterableIterator;
import com.braintribe.gwt.genericmodel.client.reflect.TypePackage;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.Messages.Optional;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.annotations.custom.TsIgnore;
import jsinterop.utils.Lambdas.JsUnaryFunction;

@TsIgnore
@JsType(name = "Set", namespace = TypePackage.GM_TYPE_NAMESPACE)
public class Setish<T> extends AbstractCollectionish<T> {

	private final Set<Object> set;

	public Setish(Set<Object> set, JsUnaryFunction<Object, T> javaToJs, JsUnaryFunction<T, Object> jsToJava) {
		super(set, javaToJs, jsToJava);
		this.set = set != null ? set : TYPE_SET.createPlain();
	}

	@Override
	public Object wrappedCollection() {
		return set;
	}

	// ############################################
	// ## . . . . . es2015.collections . . . . . ##
	// ############################################

	@JsProperty(name = "size")
	public final int size() {
		return set.size();
	}

	public Setish<T> add(T jsElement) {
		set.add(toJavaIfNeeded(jsElement));
		return this;
	}

	public void clear() {
		set.clear();
	}

	public boolean delete(T jsElement) {
		return set.remove(toJavaIfNeeded(jsElement));
	}

	public native void forEach(JavaScriptObject callbackfn, @Optional JavaScriptObject thisArg) /*-{
		return this.@Setish::toJsArray()().forEach(callbackfn, thisArg);
	}-*/;

	public boolean has(T jsElement) {
		return set.contains(toJavaIfNeeded(jsElement));
	}

	// ############################################
	// ## . . . . . . es2015.iterable . . . . . .##
	// ############################################

	// Real signature: entries(): IterableIterator<[T, T]>;
	public native JsIterableIterator<JavaScriptObject> entries() /*-{
		return new $wnd.Set(this.@Setish::toJsArray()()).entries();
	}-*/;

	public JsIterableIterator<T> keys() {
		return values();
	}

	public native JsIterableIterator<T> values() /*-{
		return this.@Setish::toJsArray()().values();
	}-*/;

	// ############################################
	// ## . . . . . . . Helpers . . . . . . . . .##
	// ############################################

	private T[] toJsArray() {
		return toJsArrayIfNeeded(set.toArray());
	}

}

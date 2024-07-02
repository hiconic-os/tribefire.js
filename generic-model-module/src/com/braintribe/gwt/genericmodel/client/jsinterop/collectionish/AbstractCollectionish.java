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

import com.braintribe.gwt.genericmodel.client.itw.GenericAccessorMethods;
import com.braintribe.model.generic.collection.Collectionish;
import com.braintribe.model.generic.collection.JsWrappableCollection;

import jsinterop.utils.Lambdas.JsUnaryFunction;

/**
 * Base for JS collection wrappers, which are implementations of the native JS Array/Set/Map interfaces.
 * <p>
 * They are called "-ish", because arrays don't support the square bracket index access (a[123]), and because they might not support all the methods,
 * as these are being added to JS potentially every year.
 * 
 * @see JsWrappableCollection
 */
public abstract class AbstractCollectionish<T> implements Collectionish {

	protected final JsUnaryFunction<Object, T> javaToJs;
	protected final JsUnaryFunction<T, Object> jsToJava;

	/**
	 * @param jCollection
	 *            may be <code>null</code> when created in JS. Then we want to treat it as containing Objects.
	 */
	public AbstractCollectionish(Object jCollection, JsUnaryFunction<Object, T> javaToJs, JsUnaryFunction<T, Object> jsToJava) {
		this.javaToJs = jCollection != null ? javaToJs : GenericAccessorMethods.jToJsNonCollectionConverter();
		this.jsToJava = jCollection != null ? jsToJava : GenericAccessorMethods.jsToJNonCollectionConverter();
	}

	protected T[] toJsArrayIfNeeded(Object[] jArray) {
		return jsToJava == null || jArray.length == 0 ? (T[]) jArray : convertToJsArray(jArray);
	}

	protected native T[] convertToJsArray(Object[] jArray) /*-{
		return jArray.map(this.@AbstractCollectionish::javaToJs); 
	}-*/;

	protected Object[] toJArrayIfNeeded(T[] jsArray) {
		return jsToJava == null || jsArray.length == 0 ? (Object[]) jsArray : convertToJArray(jsArray);
	}

	protected native Object[] convertToJArray(T[] jsArray) /*-{
		return jsArray.map(this.@AbstractCollectionish::jsToJava); 
	}-*/;

	protected T toJsIfNeeded(Object o) {
		return javaToJs == null || o == null ? (T) o : javaToJs.call(o);
	}

	protected Object toJavaIfNeeded(T t) {
		return jsToJava == null || t == null ? t : jsToJava.call(t);
	}

	protected native <E> E undefined() /*-{
		return undefined;
	}-*/;

}

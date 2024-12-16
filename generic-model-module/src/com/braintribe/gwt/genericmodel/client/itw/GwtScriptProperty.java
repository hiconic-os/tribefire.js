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

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.AbstractProperty;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author peter.gazdik
 */
public abstract class GwtScriptProperty extends AbstractProperty {

	public GwtScriptProperty(String propertyName, boolean nullable, boolean confidential) {
		super(propertyName, nullable, confidential);

		setAccessors(getFieldSymbol(), RuntimeMethodNames.propertyGetDirectUnsafe(), RuntimeMethodNames.propertySetDirectUnsafe());
	}

	public PropertyBinding propertyBinding;

	private static JavaScriptObject symbols = JavaScriptObject.createObject();

	public native JavaScriptObject getFieldSymbol() /*-{
		var symbols = @GwtScriptProperty::symbols;
		var name = this.@AbstractProperty::propertyName;
		var result = symbols[name];
		if (!result) {
			result = Symbol(name);
			symbols[name] = result;
		}
		return result;
	}-*/;

	@Override
	public native <T> T getJs(GenericEntity entity) /*-{
		return entity[this.@AbstractProperty::propertyName];
	}-*/;

	@Override
	public native void setJs(GenericEntity entity, Object value) /*-{
		entity[this.@AbstractProperty::propertyName] = value;
	}-*/;

	@SuppressWarnings("unusable-by-js")
	@Override
	public <T> T getDirectUnsafe(GenericEntity entity) {
		throw new UnsupportedOperationException("Seems method 'getDirectUnsafe' was not implemented in runtime! Property: "
				+ getDeclaringType().getTypeSignature() + "." + getName());
	}

	@SuppressWarnings("unusable-by-js")
	@Override
	public void setDirectUnsafe(GenericEntity entity, Object value) {
		throw new UnsupportedOperationException("Seems method 'setDirectUnsafe' was not implemented in runtime! Property: "
				+ getDeclaringType().getTypeSignature() + "." + getName());
	}

	private native void setAccessors(JavaScriptObject symbol, String getDirectName, String setDirectName) /*-{
		this[getDirectName] = (function(e){return e[symbol]});
		this[setDirectName] = (function(e,v){e[symbol]=v}); 
	}-*/;

}

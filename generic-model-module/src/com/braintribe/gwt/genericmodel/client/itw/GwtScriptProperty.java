// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.gwt.genericmodel.client.itw;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.AbstractProperty;

/**
 * @author peter.gazdik
 */
public abstract class GwtScriptProperty extends AbstractProperty {

	public GwtScriptProperty(String propertyName, boolean nullable, boolean confidential) {
		super(propertyName, nullable, confidential);
	}

	private PropertyBinding propertyBinding;

	public PropertyBinding getPropertyBinding() {
		return propertyBinding;
	}

	public void setPropertyBinding(PropertyBinding propertyBinding) {
		this.propertyBinding = propertyBinding;
	}

	public String getFieldName() {
		return ObfuscatedIdentifierSequence.specialChar + getName();
	}

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

}

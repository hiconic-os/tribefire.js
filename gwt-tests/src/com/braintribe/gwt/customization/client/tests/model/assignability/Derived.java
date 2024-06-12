package com.braintribe.gwt.customization.client.tests.model.assignability;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface Derived extends DerivedAbstractBase {
	
	final EntityType<Derived> T = EntityTypes.T(Derived.class);
}

package com.braintribe.gwt.customization.client.tests.model.assignability;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
public interface AbstractDerived extends AbstractDerivedAbstractBase {
	
	final EntityType<AbstractDerived> T = EntityTypes.T(AbstractDerived.class);
}

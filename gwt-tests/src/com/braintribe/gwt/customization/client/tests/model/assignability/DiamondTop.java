package com.braintribe.gwt.customization.client.tests.model.assignability;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface DiamondTop extends DiamondAbstractIntermediate1, DiamondAbstractIntermediate2 {

	final EntityType<DiamondTop> T = EntityTypes.T(DiamondTop.class);
}

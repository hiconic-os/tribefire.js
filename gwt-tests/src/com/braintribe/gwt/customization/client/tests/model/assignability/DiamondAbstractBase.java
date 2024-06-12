package com.braintribe.gwt.customization.client.tests.model.assignability;

import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
public interface DiamondAbstractBase extends StandardIdentifiable {

	final EntityType<DiamondAbstractBase> T = EntityTypes.T(DiamondAbstractBase.class);
}

package com.braintribe.gwt.customization.client.tests.model.assignability;

import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
public interface StandaloneAbstractBase extends StandardIdentifiable {

	final EntityType<StandaloneAbstractBase> T = EntityTypes.T(StandaloneAbstractBase.class);
}

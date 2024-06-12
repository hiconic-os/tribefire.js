package com.braintribe.gwt.customization.client.tests.model.basic.proto.misc;

import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface IdEntity extends StandardIdentifiable {

	EntityType<IdEntity> T = EntityTypes.T(IdEntity.class);

}

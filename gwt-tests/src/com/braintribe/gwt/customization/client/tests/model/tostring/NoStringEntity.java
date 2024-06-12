package com.braintribe.gwt.customization.client.tests.model.tostring;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface NoStringEntity extends GenericEntity {

	EntityType<NoStringEntity> T = EntityTypes.T(NoStringEntity.class);
}

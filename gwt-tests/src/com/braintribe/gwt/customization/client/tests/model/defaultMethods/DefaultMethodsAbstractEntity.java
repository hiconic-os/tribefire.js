package com.braintribe.gwt.customization.client.tests.model.defaultMethods;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface DefaultMethodsAbstractEntity extends GenericEntity {

	EntityType<DefaultMethodsAbstractEntity> T = EntityTypes.T(DefaultMethodsAbstractEntity.class);

	String ABSTRACT_DESCRIPTION = "DMAE";

	default String abstractDescription() {
		return ABSTRACT_DESCRIPTION;
	}

}

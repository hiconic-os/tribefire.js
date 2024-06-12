package com.braintribe.gwt.customization.client.tests.model.defaultMethods;

import java.util.Set;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface DefaultMethodsSubEntity extends DefaultMethodsEntity {

	EntityType<DefaultMethodsSubEntity> T = EntityTypes.T(DefaultMethodsSubEntity.class);

	String DESCRIPTION = "SUB-DME";

	@Override
	default String description() {
		return DESCRIPTION;
	}

	default void addTo(Set<GenericEntity> set) {
		set.add(this);
	}

}

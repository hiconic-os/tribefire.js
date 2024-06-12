package com.braintribe.gwt.customization.client.tests.model.defaultMethods;

import java.util.LinkedList;
import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface DefaultMethodsEntity extends DefaultMethodsAbstractEntity {

	EntityType<DefaultMethodsEntity> T = EntityTypes.T(DefaultMethodsEntity.class);
	
	String DESCRIPTION = "DME";

	default String description() throws Exception {
		return DESCRIPTION;
	}

	default int identityHash() {
		return System.identityHashCode(this);
	}

	default void addToList(List<GenericEntity> list) {
		list.add(this);
	}

	default void addToList(LinkedList<GenericEntity> list) {
		list.add(this);
		list.add(this);
	}

}

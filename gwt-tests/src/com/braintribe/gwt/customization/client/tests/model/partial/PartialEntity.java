package com.braintribe.gwt.customization.client.tests.model.partial;

import com.braintribe.gwt.customization.client.tests.model.initializer.Color;
import com.braintribe.gwt.customization.client.tests.model.initializer.InitializedEntity;
import com.braintribe.model.generic.StandardIdentifiable;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface PartialEntity extends StandardIdentifiable {

	EntityType<PartialEntity> T = EntityTypes.T(PartialEntity.class);

	InitializedEntity getInitializedEntity();
	void setInitializedEntity(InitializedEntity initializedEntity);

	Color getColor();
	void setColor(Color value);

}

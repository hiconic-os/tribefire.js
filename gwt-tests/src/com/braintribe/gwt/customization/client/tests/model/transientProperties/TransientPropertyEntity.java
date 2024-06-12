package com.braintribe.gwt.customization.client.tests.model.transientProperties;

import java.sql.Timestamp;

import com.braintribe.model.generic.annotation.Transient;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface TransientPropertyEntity extends TransientPropertySuper1, TransientPropertySuper2 {

	EntityType<TransientPropertyEntity> T = EntityTypes.T(TransientPropertyEntity.class);

	int NUMBER_OF_PROPS = TransientPropertySuper1.NUMBER_OF_PROPS + TransientPropertySuper2.NUMBER_OF_PROPS + 4;

	@Transient
	String getName();
	void setName(String name);

	@Transient
	Object getObject();
	void setObject(Object object);

	@Transient
	Timestamp getTimestamp();
	void setTimestamp(Timestamp timestamp);

	@Transient
	TransientPropertyEntity getSelf();
	void setSelf(TransientPropertyEntity self);

}

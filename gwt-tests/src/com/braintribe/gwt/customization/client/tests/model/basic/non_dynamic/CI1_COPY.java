package com.braintribe.gwt.customization.client.tests.model.basic.non_dynamic;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface CI1_COPY extends GenericEntity {

	EntityType<CI1_COPY> T = EntityTypes.T(CI1_COPY.class);

	public void setLong(Long l);

	public Long getLong();

	public void setPrimitiveLong(long l);

	public long getPrimitiveLong();
}

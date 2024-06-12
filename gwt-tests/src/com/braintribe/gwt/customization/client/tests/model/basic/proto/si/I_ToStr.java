package com.braintribe.gwt.customization.client.tests.model.basic.proto.si;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.ToStringInformation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@ToStringInformation("Prefix_${primitiveLong}_Suffix")
public interface I_ToStr extends GenericEntity {

	EntityType<I_ToStr> T = EntityTypes.T(I_ToStr.class);

	void setLong(Long l);
	Long getLong();

	void setPrimitiveLong(long l);
	long getPrimitiveLong();

}

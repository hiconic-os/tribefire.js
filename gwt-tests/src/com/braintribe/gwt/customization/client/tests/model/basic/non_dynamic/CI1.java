package com.braintribe.gwt.customization.client.tests.model.basic.non_dynamic;

import com.braintribe.gwt.customization.client.tests.model.basic.proto.misc.ItwTestColor;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface CI1 extends GenericEntity {
	
	EntityType<CI1> T = EntityTypes.T(CI1.class);
	
	void setLong(Long l);
	Long getLong();
	
	void setPrimitiveLong(long l);
	long getPrimitiveLong();
	
	ItwTestColor getColor();
	void setColor(ItwTestColor value);
}

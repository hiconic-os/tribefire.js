package com.braintribe.gwt.customization.client.tests.model.basic.non_dynamic;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface CI2 extends GenericEntity {

	EntityType<CI2> T = EntityTypes.T(CI2.class);

	String getString();
	void setString(String s);

}

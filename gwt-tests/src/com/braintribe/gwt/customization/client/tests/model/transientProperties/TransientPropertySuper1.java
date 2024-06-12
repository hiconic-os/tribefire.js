package com.braintribe.gwt.customization.client.tests.model.transientProperties;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Transient;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface TransientPropertySuper1 extends GenericEntity {

	EntityType<TransientPropertySuper1> T = EntityTypes.T(TransientPropertySuper1.class);

	int NUMBER_OF_PROPS = 1;
	
	@Transient
	String getSuper1();
	void setSuper1(String transientSuper1);

}

package com.braintribe.gwt.customization.client.tests.model.transientProperties;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Transient;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface TransientPropertySuper2 extends GenericEntity {

	EntityType<TransientPropertySuper2> T = EntityTypes.T(TransientPropertySuper2.class);

	int NUMBER_OF_PROPS = 1;
	
	@Transient
	String getSuper2();
	void setSuper2(String transientSuper2);

}

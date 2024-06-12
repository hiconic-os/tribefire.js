package com.braintribe.gwt.customization.client.tests.model.basic.proto.mi;

import com.braintribe.gwt.customization.client.tests.model.basic.non_dynamic.CI1;
import com.braintribe.gwt.customization.client.tests.model.basic.non_dynamic.CI2;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;


public interface RI_CI_CI extends CI1, CI2 {

	EntityType<RI_CI_CI> T = EntityTypes.T(RI_CI_CI.class);

}

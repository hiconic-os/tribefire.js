package com.braintribe.gwt.customization.client.tests.model.basic.proto.mi;

import com.braintribe.gwt.customization.client.tests.model.basic.non_dynamic.CI1;
import com.braintribe.gwt.customization.client.tests.model.basic.proto.si.RI_CI;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@SuppressWarnings("unused")

public interface RI_CI_RI extends CI1, RI_CI {

	EntityType<RI_CI_RI> T = EntityTypes.T(RI_CI_RI.class);
}

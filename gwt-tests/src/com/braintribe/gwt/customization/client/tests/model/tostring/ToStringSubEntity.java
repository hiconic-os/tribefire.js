package com.braintribe.gwt.customization.client.tests.model.tostring;

import com.braintribe.model.generic.annotation.SelectiveInformation;
import com.braintribe.model.generic.annotation.ToStringInformation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@SelectiveInformation("${#type} ${#type_short} ${#id} ${#runtimeId} ${N/A} SelectiveSub")
@ToStringInformation("${#type} ${#type_short} ${#id} ${#runtimeId} ${N/A} ToStringSub")

public interface ToStringSubEntity extends ToStringEntity {

	EntityType<ToStringSubEntity> T = EntityTypes.T(ToStringSubEntity.class);

}

package com.braintribe.gwt.customization.client.tests.model.tostring;

import com.braintribe.model.generic.StandardStringIdentifiable;
import com.braintribe.model.generic.annotation.SelectiveInformation;
import com.braintribe.model.generic.annotation.ToStringInformation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@SelectiveInformation("${#type} ${#type_short} ${#id} ${#runtimeId} ${N/A} Selective")
@ToStringInformation("${#type} ${#type_short} ${#id} ${#runtimeId} ${N/A} ToString")
public interface ToStringEntity extends StandardStringIdentifiable {

	EntityType<ToStringEntity> T = EntityTypes.T(ToStringEntity.class);

}

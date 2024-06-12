package com.braintribe.gwt.customization.client.tests.model.transientProperties;

import com.braintribe.model.generic.annotation.Transient;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface TransientPropertySubEntity extends TransientPropertyEntity {

	EntityType<TransientPropertySubEntity> T = EntityTypes.T(TransientPropertySubEntity.class);

	int NUMBER_OF_PROPS = TransientPropertyEntity.NUMBER_OF_PROPS + 1;

	
	@Transient
	String getSubName();
	void setSubName(String subName);

}

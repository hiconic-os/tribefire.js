package com.braintribe.gwt.customization.client.tests.model.singleCharEnum;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface SingleCharEnumEntity extends GenericEntity {

	EntityType<SingleCharEnumEntity> T = EntityTypes.T(SingleCharEnumEntity.class);

	SingleCharEnum getSingleCharEnum();
	void setSingleCharEnum(SingleCharEnum singleCharEnum);

}

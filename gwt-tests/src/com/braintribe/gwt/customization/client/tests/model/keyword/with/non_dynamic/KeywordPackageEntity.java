package com.braintribe.gwt.customization.client.tests.model.keyword.with.non_dynamic;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface KeywordPackageEntity extends GenericEntity {

	EntityType<KeywordPackageEntity> T = EntityTypes.T(KeywordPackageEntity.class);

}

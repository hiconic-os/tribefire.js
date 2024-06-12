package com.braintribe.gwt.customization.client.tests.model.essentialmd;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.meta.Confidential;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface EssentialMdSiblingEntity extends GenericEntity {

	EntityType<EssentialMdSiblingEntity> T = EntityTypes.T(EssentialMdSiblingEntity.class);

	String siblingConfidential = "siblingConfidential";

	@Confidential
	String getSiblingConfidential();
	void setSiblingConfidential(String siblingConfidential);

}

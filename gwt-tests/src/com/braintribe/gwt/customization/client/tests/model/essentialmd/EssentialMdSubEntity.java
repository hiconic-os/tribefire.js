package com.braintribe.gwt.customization.client.tests.model.essentialmd;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface EssentialMdSubEntity extends EssentialMdEntity, EssentialMdSiblingEntity {

	EntityType<EssentialMdSubEntity> T = EntityTypes.T(EssentialMdSubEntity.class);

	@Override
	@Initializer("'sub-secret'")
	String getConfidential();

}

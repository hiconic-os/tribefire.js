package com.braintribe.gwt.customization.client.tests.model.essentialmd;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.meta.Confidential;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface EssentialMdEntity extends GenericEntity {

	EntityType<EssentialMdEntity> T = EntityTypes.T(EssentialMdEntity.class);

	String regularProperty = "regularProperty";
	String confidential = "confidential";

	String getRegularProperty();
	void setRegularProperty(String regularProperty);

	@Confidential
	String getConfidential();
	void setConfidential(String confidential);

	/**
	 * This property is declared confidential in the {@link EssentialMdSiblingEntity} to test the multiple inheritance case. The sub-type
	 * {@link EssentialMdSubEntity} must have both "confidential" and "siblingConfidential" marked as confidential.
	 */
	String getSiblingConfidential();
	void setSiblingConfidential(String siblingConfidential);

}

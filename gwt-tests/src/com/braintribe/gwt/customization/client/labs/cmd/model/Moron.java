package com.braintribe.gwt.customization.client.labs.cmd.model;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface Moron extends GenericEntity {

	EntityType<Moron> T = EntityTypes.T(Moron.class);

	// @formatter:off
	String getStupidName();
	void setStupidName(String stupidName);
	// @formatter:on

}

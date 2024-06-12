package com.braintribe.gwt.customization.client.tests.model.methodsMultiInheritance;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface BaseWithProps extends GenericEntity {

	EntityType<BaseWithProps> T = EntityTypes.T(BaseWithProps.class);

	String getNameX();
	void setNameX(String nameX);

	String getNameY();
	void setNameY(String nameY);

	String getNameZ();
	void setNameZ(String nameZ);

}

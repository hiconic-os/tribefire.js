package com.braintribe.gwt.customization.client.tests.model.methodsMultiInheritance;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface SubTypeWithPropsAndMethods extends BaseWithMethods, BaseWithProps {

	EntityType<SubTypeWithPropsAndMethods> T = EntityTypes.T(SubTypeWithPropsAndMethods.class);

}

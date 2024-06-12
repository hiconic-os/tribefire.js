package com.braintribe.gwt.customization.client.tests.model.evaluatesTo;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface SubTypeEvalResult extends SubTypeEvalRequest {
	
	final EntityType<SubTypeEvalResult> T = EntityTypes.T(SubTypeEvalResult.class);


}


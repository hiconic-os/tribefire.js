package com.braintribe.gwt.customization.client.tests.model.evaluatesTo;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * In runtime GWT ITW we had an issue this leads to sub-type being woven before super-type is ready, and leading to an
 * NPE when accessing sub-type's super properties.
 * 
 * @author peter.gazdik
 */
public interface SubTypeEvalRequest extends GenericEntity {

	final EntityType<SubTypeEvalRequest> T = EntityTypes.T(SubTypeEvalRequest.class);

	EvalContext<SubTypeEvalResult> eval(Evaluator<SubTypeEvalRequest> evaluator);

}

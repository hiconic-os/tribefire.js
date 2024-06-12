package com.braintribe.gwt.customization.client.tests.model.evaluatesTo;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface BoundWildcardEvalEntity extends GenericEntity {
	
	final EntityType<BoundWildcardEvalEntity> T = EntityTypes.T(BoundWildcardEvalEntity.class);

	EvalContext<? extends GenericEntity> eval(Evaluator<BoundWildcardEvalEntity> evaluator);

}


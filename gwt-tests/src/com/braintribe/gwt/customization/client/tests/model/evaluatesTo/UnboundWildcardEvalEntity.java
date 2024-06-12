package com.braintribe.gwt.customization.client.tests.model.evaluatesTo;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface UnboundWildcardEvalEntity extends GenericEntity {
	
	final EntityType<UnboundWildcardEvalEntity> T = EntityTypes.T(UnboundWildcardEvalEntity.class);

	EvalContext<?> eval(Evaluator<UnboundWildcardEvalEntity> evaluator);

}


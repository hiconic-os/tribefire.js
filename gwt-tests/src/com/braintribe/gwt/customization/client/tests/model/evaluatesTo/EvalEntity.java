package com.braintribe.gwt.customization.client.tests.model.evaluatesTo;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface EvalEntity extends GenericEntity {
	
	final EntityType<EvalEntity> T = EntityTypes.T(EvalEntity.class);

	EvalContext<String> eval(Evaluator<EvalEntity> evaluator);

}


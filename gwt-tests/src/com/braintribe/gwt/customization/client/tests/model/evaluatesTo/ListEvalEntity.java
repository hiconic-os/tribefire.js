package com.braintribe.gwt.customization.client.tests.model.evaluatesTo;

import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface ListEvalEntity extends GenericEntity {
	
	final EntityType<ListEvalEntity> T = EntityTypes.T(ListEvalEntity.class);

	EvalContext<? extends List<String>> eval(Evaluator<ListEvalEntity> evaluator);

}


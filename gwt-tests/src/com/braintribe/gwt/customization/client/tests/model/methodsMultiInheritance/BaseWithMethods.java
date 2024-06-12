package com.braintribe.gwt.customization.client.tests.model.methodsMultiInheritance;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface BaseWithMethods extends GenericEntity {

	EntityType<BaseWithMethods> T = EntityTypes.T(BaseWithMethods.class);

	EvalContext<String> eval(Evaluator<BaseWithMethods> evaluator);

	default String print() {
		return "I am " + entityType().getTypeSignature();
	}

}

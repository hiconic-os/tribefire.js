package com.braintribe.gwt.customization.client.tests;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.util.Date;

import com.braintribe.gwt.customization.client.tests.model.methodsMultiInheritance.BaseWithMethods;
import com.braintribe.gwt.customization.client.tests.model.methodsMultiInheritance.BaseWithProps;
import com.braintribe.gwt.customization.client.tests.model.methodsMultiInheritance.SubTypeWithPropsAndMethods;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.meta.GmMetaModel;
import com.braintribe.model.util.meta.NewMetaModelGeneration;

/**
 * To really test this DO NOT USE DRAFT COMPILE OPTION!!!
 * 
 * @author peter.gazdik
 */
public class MethodsMultiInheritanceTest extends AbstractGmGwtTest {

	@Override
	protected void tryRun() throws GmfException {
		GmMetaModel metaModel = generateModel();

		makeSignaturesDynamic(metaModel);
		ensureModelTypes(metaModel);

		runEvals();
	}

	private GmMetaModel generateModel() {
		log("generating meta model");

		NewMetaModelGeneration mmg = new NewMetaModelGeneration();
		GmMetaModel baseModel = mmg.buildMetaModel("test.gwt.eval-base-model", asList(BaseWithMethods.T, BaseWithProps.T));
		GmMetaModel bothModel = mmg.buildMetaModel("test.gwt.eval-both-model", asList(SubTypeWithPropsAndMethods.T), asList(baseModel));
		return bothModel;
	}

	private void runEvals() {
		log("Running eval tests");

		runEval(SubTypeWithPropsAndMethods.T);
	}

	private void runEval(EntityType<? extends BaseWithMethods> et) {
		runEvalHelper(et.getTypeSignature());
		runEvalHelper(makeSignatureDynamic(et.getTypeSignature()));
	}

	private void runEvalHelper(String typeSignature) {
		EntityType<? extends BaseWithMethods> et = typeReflection.getEntityType(typeSignature);
		BaseWithMethods instance = et.create();
		Evaluator<BaseWithMethods> evaluator = getEvaluator();

		log("Calling eval on: " + typeSignature);
		instance.eval(evaluator);

		log("Calling default method on: " + typeSignature);
		log(instance.print());
	}

	// private native void evalJs(GenericEntity instance, Evaluator<?> evaluator)
	// /*-{
	// instance.@Evaluable::eval(Lcom/braintribe/model/generic/eval/Evaluator;)(evaluator);
	// }-*/;

	private Evaluator<BaseWithMethods> getEvaluator() {
		if (new Date().getTime() < 10)
			return this::evaluatorImpl1;
		else
			return this::evaluatorImpl2;
	}

	<T> EvalContext<T> evaluatorImpl1(BaseWithMethods evaluable) {
		log("Evaluator called for: " + evaluable.entityType().getTypeSignature());
		return null;
	}

	<T> EvalContext<T> evaluatorImpl2(BaseWithMethods evaluable) {
		log("Evaluator called for: " + evaluable.entityType().getTypeSignature());
		return null;
	}

}

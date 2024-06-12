package com.braintribe.gwt.customization.client.tests;

import com.braintribe.model.processing.query.eval.context.ConditionEvaluationTools;

/**
 * @author peter.gazdik
 */
public class RegexTest extends AbstractGmGwtTest {

	@Override
	public void tryRun() {
		String plain = "com.bt.model.Clazz";
		String pattern = "*.Clazz";

		String convertedPattern = ConditionEvaluationTools.convertToRegexPattern(pattern);

		log("Pattern: " + pattern);
		log("ConvertedPattern: " + convertedPattern);
		log("Matches: " + plain.matches(convertedPattern));
	}

}

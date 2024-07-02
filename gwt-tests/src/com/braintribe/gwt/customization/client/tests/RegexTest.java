// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
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

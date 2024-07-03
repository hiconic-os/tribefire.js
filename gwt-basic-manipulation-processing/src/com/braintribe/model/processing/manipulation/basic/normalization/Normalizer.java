// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
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
package com.braintribe.model.processing.manipulation.basic.normalization;

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.util.List;

import com.braintribe.model.accessapi.ManipulationRequest;
import com.braintribe.model.generic.manipulation.AtomicManipulation;
import com.braintribe.model.generic.manipulation.Manipulation;
import com.braintribe.model.generic.manipulation.NormalizedCompoundManipulation;
import com.braintribe.model.generic.manipulation.util.ManipulationBuilder;

/**
 * This class is responsible for providing a normal/canonical/compact form for manipulations. What it means is, that it
 * takes a list of {@link AtomicManipulation}s and returns a modified list which has an equivalent effect when applied
 * (via the right "applyManipulation" invocation), but is normalized in a certain sense.
 * <p>
 * Note that all "normalize" methods check whether a given manipulation is already a normalized one (by checking whether
 * the type is {@link NormalizedCompoundManipulation}) and if so, this original manipulation is returned without any
 * normalization process being executed.
 * <p>
 * This process consists of two parts.
 * 
 * <h3>Removing redundant manipulations</h3>
 * 
 * This part takes a list of manipulations and returns a minimal equivalent sub-list of the original list. For example
 * if a list contains an Instantiation and Deletion of the same entity, these (along with any changes made to that
 * entity) may be removed from the list without having any effect on the resulting compound manipulation. This is
 * implemented in {@link SimpleManipulationNormalizer}.
 * 
 * <h3>Summarizing collection manipulations</h3>
 * 
 * Manipulation applied on same collection/map property may be grouped together. Multiple inserts may be merged into one
 * bulk-insert, insert and remove may be both left out, <code>insert(a)</code> and <code>replace(a, b)</code> is
 * equivalent to <code>insert(b)</code>.
 * 
 * This is implemented in {@link CollectionManipulationNormalizer}.
 * 
 * @see IdManipulationNormalizer
 * @see SimpleManipulationNormalizer
 * @see CollectionManipulationNormalizer
 */
public class Normalizer {

	public static void normalize(ManipulationRequest manipulationRequest) {
		manipulationRequest.setManipulation(normalize(manipulationRequest.getManipulation()));
	}

	public static NormalizedCompoundManipulation normalize(Manipulation manipulation) {
		if (isNormal(manipulation))
			return (NormalizedCompoundManipulation) manipulation;
		else
			return normalize(manipulation, new NormalizationContext());
	}

	private static NormalizedCompoundManipulation normalize(Manipulation manipulation, NormalizationContext context) {
		List<AtomicManipulation> manipulations = manipulation.inline();

		List<AtomicManipulation> result = newList(manipulations);
		result = new IdManipulationNormalizer(result, context).normalize();
		result = new SimpleManipulationNormalizer(result, context).normalize();
		result = new CollectionManipulationNormalizer(result, context).normalize();

		return wrapToNormalizedCompoundManipulation(result, manipulation);
	}

	private static boolean isNormal(Manipulation manipulation) {
		return manipulation instanceof NormalizedCompoundManipulation;
	}

	private static NormalizedCompoundManipulation wrapToNormalizedCompoundManipulation(List<AtomicManipulation> manipulations,
			Manipulation originalManipulation) {

		NormalizedCompoundManipulation result = ManipulationBuilder.normalizedCompound((List<Manipulation>) (List<?>) manipulations);
		result.setInverseManipulation(originalManipulation.getInverseManipulation());

		return result;
	}

}

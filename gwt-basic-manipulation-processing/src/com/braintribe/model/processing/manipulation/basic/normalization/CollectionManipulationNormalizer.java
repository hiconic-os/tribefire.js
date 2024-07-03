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

import static com.braintribe.model.processing.manipulation.basic.tools.ManipulationTools.createUpdatedReference;
import static com.braintribe.utils.lcd.CollectionTools2.acquireMap;
import static com.braintribe.utils.lcd.CollectionTools2.newMap;
import static com.braintribe.utils.lcd.CollectionTools2.newSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.model.generic.manipulation.AddManipulation;
import com.braintribe.model.generic.manipulation.AtomicManipulation;
import com.braintribe.model.generic.manipulation.ChangeValueManipulation;
import com.braintribe.model.generic.manipulation.ManipulationType;
import com.braintribe.model.generic.manipulation.Owner;
import com.braintribe.model.generic.manipulation.PropertyManipulation;
import com.braintribe.model.generic.manipulation.RemoveManipulation;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.value.EntityReference;
import com.braintribe.model.processing.manipulation.basic.BasicMutableManipulationContext;

/**
 * This class is responsible for keeping track of {@link AtomicManipulation}s performed on property of type map/collection. The process how
 * the aggregation (merging) of various manipulations is computed is different for different collection types. See specific trackers for
 * more information.
 * 
 * @see ListTracker
 * @see SetTracker
 * @see MapTracker
 */
class CollectionManipulationNormalizer {

	private final List<AtomicManipulation> manipulations;
	private final NormalizationContext normalizationContext;

	private final BasicMutableManipulationContext context;

	private final Map<EntityReference, Map<String, CollectionTracker>> collectionTrackers = newMap();
	private final Set<Integer> manipulationsToDelete = newSet();

	/**
	 * We keep track of instantiated entities so that whenever we encounter a collection manipulation on such, we notify the tracker that we
	 * are starting with empty collection, thus the result will always be a {@link ChangeValueManipulation}.
	 */
	private final Set<EntityReference> createdEntities = newSet();

	private CollectionType propertyType;

	public CollectionManipulationNormalizer(List<AtomicManipulation> manipulations, NormalizationContext normalizationContext) {
		this.manipulations = manipulations;
		this.normalizationContext = normalizationContext;
		this.context = normalizationContext.manipulationContext;
}

	public List<AtomicManipulation> normalize() {
		/* Examine manipulations */
		Integer index = 0;
		for (AtomicManipulation manipulation: manipulations) {
			context.setCurrentManipulationSafe(manipulation);

			if (examineManipulation())
				manipulationsToDelete.add(index);

			index++;
		}

		/* Execute the actual removing of manipulations */
		for (int positionToDelete: positionsToDeleteDescending()) {
			manipulations.remove(positionToDelete);
		}

		/* Insert merged manipulations instead of the removed ones */
		for (Map<String, CollectionTracker> propertyTrackers: collectionTrackers.values())
			for (CollectionTracker tracker: propertyTrackers.values())
				tracker.appendAggregateManipulations(manipulations, normalizationContext.entitiesToDelete);

		return manipulations;
	}

	private List<Integer> positionsToDeleteDescending() {
		List<Integer> positionsToDelete = new ArrayList<Integer>(manipulationsToDelete);
		Collections.sort(positionsToDelete, Collections.reverseOrder());

		return positionsToDelete;
	}

	private boolean examineManipulation() {
		if (context.getTargetPropertyName() == null) {
			noteInstantiationIfPossible();

			return false;
		}

		if (!initializeCollectionProperty()) {
			updateInstantiationIfEligible();

			return false;
		}

		CollectionTracker tracker = acquireCollectionTracker();

		switch (context.getCurrentManipulationType()) {
			case CHANGE_VALUE:
				tracker.onChangeValue((ChangeValueManipulation) context.getCurrentManipulation());
				return true;

			case CLEAR_COLLECTION:
				tracker.onClearCollection();
				return true;

			case ADD:
				tracker.onBulkInsert((AddManipulation) context.getCurrentManipulation());
				return true;

			case REMOVE:
				tracker.onBulkRemove((RemoveManipulation) context.getCurrentManipulation());
				return true;

			default:
				return false;
		}
	}

	private void noteInstantiationIfPossible() {
		if (context.getCurrentManipulationType() == ManipulationType.INSTANTIATION)
			createdEntities.add(context.getNormalizedTargetReference());
	}

	/**
	 * We update the reference in {@link #createdEntities} if this manipulation is a {@link ChangeValueManipulation} on an identifying
	 * property of such.
	 */
	private void updateInstantiationIfEligible() {
		if (context.getCurrentManipulationType() != ManipulationType.CHANGE_VALUE)
			return;

		if (!context.getTargetProperty().isIdentifying())
			return;

		EntityReference targetRef = context.getNormalizedTargetReference();

		if (createdEntities.contains(targetRef)) {
			createdEntities.remove(targetRef);

			ChangeValueManipulation cvm = context.getCurrentManipulation();
			EntityReference newRef = createUpdatedReference(targetRef, cvm.getOwner().getPropertyName(), cvm.getNewValue());
			createdEntities.add(context.getNormalizeReference(newRef));
		}
	}

	private CollectionTracker acquireCollectionTracker() {
		Map<String, CollectionTracker> trackersForProperties = acquireMap(collectionTrackers, context.getNormalizedTargetReference());

		CollectionTracker tracker = trackersForProperties.get(context.getTargetPropertyName());
		if (tracker == null) {
			tracker = newTracker();
			trackersForProperties.put(context.getTargetPropertyName(), tracker);

			if (createdEntities.contains(context.getNormalizedTargetReference()))
				tracker.onClearCollection();
		}

		return tracker;
	}

	private CollectionTracker newTracker() {
		Owner owner = ((PropertyManipulation) context.getCurrentManipulation()).getOwner();
		String propertySignature = context.getTargetProperty().getType().getTypeSignature();

		switch (propertyType.getCollectionKind()) {
			case list:
				return new ListTracker(owner, propertySignature);

			case map:
				return new MapTracker(owner, propertySignature);

			case set:
				return new SetTracker(owner, propertySignature);
		}

		throw new RuntimeException("Unsupported collection kind: " + propertyType.getCollectionKind());
	}

	private boolean initializeCollectionProperty() {
		GenericModelType propertyType = context.getTargetProperty().getType();

		if (!(propertyType instanceof CollectionType))
			return false;

		this.propertyType = (CollectionType) propertyType;

		return true;
	}

}

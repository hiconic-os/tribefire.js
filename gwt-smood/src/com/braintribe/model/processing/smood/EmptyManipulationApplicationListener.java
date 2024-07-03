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
package com.braintribe.model.processing.smood;

/**
 * @author peter.gazdik
 */
public class EmptyManipulationApplicationListener implements ManipulationApplicationListener {

	public static final EmptyManipulationApplicationListener INSTANCE = new EmptyManipulationApplicationListener();

	@Override
	public void onBeforeRequestApplication() {
		// noop
	}

	@Override
	public void onAfterRequestApplication() {
		// noop
	}

	@Override
	public void onBeforePersistenceIdAssignment() {
		// noop
	}

	@Override
	public void onAfterPersistenceIdAssignment() {
		// noop
	}

	@Override
	public void onBeforeGlobalIdAssignment() {
		// noop
	}

	@Override
	public void onAfterGlobalIdAssignment() {
		// noop
	}

}

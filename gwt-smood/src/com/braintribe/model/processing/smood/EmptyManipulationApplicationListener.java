// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
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

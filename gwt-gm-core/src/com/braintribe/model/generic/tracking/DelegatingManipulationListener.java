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
package com.braintribe.model.generic.tracking;

import static com.braintribe.utils.lcd.CollectionTools2.newList;

import java.util.List;

import com.braintribe.logging.Logger;
import com.braintribe.model.generic.manipulation.Manipulation;

public class DelegatingManipulationListener implements ManipulationListener {
	private static final Logger logger = Logger.getLogger(DelegatingManipulationListener.class);
	private final List<ManipulationListener> delegates = newList();

	@Override
	public void noticeManipulation(Manipulation manipulation) {
		List<ManipulationListener> delegatesClone = newList(delegates);
		for (ManipulationListener listenerDelegate : delegatesClone) {
			try {
				listenerDelegate.noticeManipulation(manipulation);
			} catch (Exception e) {
				logger.error("error while delegating manipulation", e);
				e.printStackTrace();
			}
		}
	}

	public void addDelegate(ManipulationListener listener) {
		delegates.add(listener);
	}

	public void addDelegate(ManipulationListener listener, boolean addFirst) {
		if (addFirst)
			delegates.add(0, listener);
		else
			delegates.add(listener);
	}

	public boolean removeDelegate(ManipulationListener listener) {
		return delegates.remove(listener);
	}

	public List<ManipulationListener> getDelegates() {
		return delegates;
	}

	public void clearDelegates() {
		delegates.clear();
	}

}

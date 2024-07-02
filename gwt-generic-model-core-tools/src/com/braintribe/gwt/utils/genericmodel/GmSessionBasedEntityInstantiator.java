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
package com.braintribe.gwt.utils.genericmodel;

import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.session.GmSession;
import com.braintribe.utils.genericmodel.entity.lcd.EntityInstantiator;

/**
 * A {@link GmSession session} based {@link EntityInstantiator}.
 *
 * @author michael.lafite
 */
public class GmSessionBasedEntityInstantiator extends EntityInstantiator {

	private GmSession session;

	public GmSessionBasedEntityInstantiator() {
		// nothing to do
	}

	public GmSessionBasedEntityInstantiator(GmSession session) {
		this.session = session;
	}

	public GmSession getSession() {
		return this.session;
	}

	public void setSession(GmSession session) {
		this.session = session;
	}

	@Override
	protected <U extends GenericEntity> U instantiateWithoutExceptionHandling(Class<U> entityClass) {
		if (this.session == null) {
			throw new IllegalStateException("Session not set!");
		}
		return this.session.create(GMF.getTypeReflection().getEntityType(entityClass));
	}

}

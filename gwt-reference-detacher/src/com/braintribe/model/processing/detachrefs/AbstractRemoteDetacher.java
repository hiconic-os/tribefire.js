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
package com.braintribe.model.processing.detachrefs;

import com.braintribe.model.access.IncrementalAccess;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.meta.data.QualifiedProperty;
import com.braintribe.model.processing.manipulator.api.ReferenceDetacherException;
import com.braintribe.model.processing.meta.cmd.CmdResolver;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.query.EntityQueryResult;

public abstract class AbstractRemoteDetacher extends AbstractAccessBasedReferenceDetacher<IncrementalAccess> {

	public AbstractRemoteDetacher(IncrementalAccess access, CmdResolver cmdResolver) {
		super(access, cmdResolver);
	}

	@Override
	protected void executeDetach(EntityQuery query, QualifiedProperty qualifiedProperty, Property property, GenericEntity entityToDetach,
			String detachNotAllowedReason) {

		EntityQueryResult queryResult;
		do {
			PersistenceGmSession session = session();

			queryResult = executeQuery(session, query);
			checkDetachAllowed(detachNotAllowedReason, queryResult, qualifiedProperty, entityToDetach);
			removeReferences(queryResult, qualifiedProperty, property, entityToDetach);

			commit(session);

		} while (queryResult.getHasMore());
	}

	@Override
	protected EntityQueryResult executeQuery(EntityQuery query) throws ReferenceDetacherException {
		return executeQuery(session(), query);
	}

	protected abstract PersistenceGmSession session();

	private EntityQueryResult executeQuery(PersistenceGmSession session, EntityQuery query) throws ReferenceDetacherException {
		try {
			return session.query().entities(query).result();

		} catch (Exception e) {
			throw new ReferenceDetacherException("Error while querying entities.", e);
		}
	}

	private void commit(PersistenceGmSession session) throws ReferenceDetacherException {
		try {
			session.commit();

		} catch (GmSessionException e) {
			throw new ReferenceDetacherException("Session commit failed.", e);
		}
	}
}

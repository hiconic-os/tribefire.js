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
package com.braintribe.model.processing.query.stringifier.experts;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.processing.query.api.stringifier.QueryStringifierRuntimeException;
import com.braintribe.model.processing.query.api.stringifier.experts.Stringifier;
import com.braintribe.model.processing.query.api.stringifier.experts.StringifierContext;

public class EntityStringifier implements Stringifier<GenericEntity,StringifierContext> {
	@Override
	public String stringify(GenericEntity entity, StringifierContext context) throws QueryStringifierRuntimeException {
		// TODO why are we creating a new instance of EntityReference just to write entity as String?
		// TODO Use something like following instead (or whatever the representation for an entity reference is):
		// TODO return entity.type().getTypeSignature() + "[" + entity.persistenceId() + ", " + entity.get$partition() + "]";

		return context.stringify(entity.reference());
	}
}

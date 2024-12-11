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
package com.braintribe.model.processing.session.api.managed;

import static java.util.Objects.requireNonNull;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.braintribe.model.generic.manipulation.DeleteMode;
import com.braintribe.model.generic.manipulation.Manipulation;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.session.exception.GmSessionException;
import com.braintribe.model.processing.session.api.notifying.NotifyingGmSession;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;
import com.braintribe.model.processing.session.api.resource.ResourceAccess;
import com.braintribe.model.resource.api.HasResourceReadAccess;

import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsType;

/**
 * An extension of {@link NotifyingGmSession} which provides some advanced functionality regarding the entities belonging to this session. The main
 * difference is, that this session is aware of the entities attached to it.
 * <p>
 * It is possible to query these entities and also to apply manipulations on them. An important aspect of a managed session (and sub-types) is the so
 * called identity management, which is a guarantee that queries for the same entity (same means having same id) always return the same instance.
 * <p>
 * Another important change compared to {@link NotifyingGmSession} is the semantics of the {@link #deleteEntity(GenericEntity)}.
 * 
 * <h3>Querying attached entities</h3>
 * 
 * This session keeps track of the entities attached to it and is able to evaluate queries on these entities and also resolve entity references. If an
 * entity for given reference is not attached to the session, <tt>null</tt> is returned (unlike in case of {@link PersistenceGmSession}).
 * 
 * @see NotifyingGmSession
 * @see PersistenceGmSession
 * @see SessionQueryBuilder
 * @see ManipulationApplicationContextBuilder
 */
@JsType(namespace=GmCoreApiInteropNamespaces.session)
@SuppressWarnings("unusable-by-js")
public interface ManagedGmSession extends NotifyingGmSession, HasResourceReadAccess, EntityManager {

	<T extends GenericEntity> T acquire(EntityType<T> entityType, String globalId); 
	
	/**
	 * Deletes given entity from the session and clears all the references from other entities pointing to given entity
	 * (other entities bound to this same session of course).
	 */
	@Override
	void deleteEntity(GenericEntity entity);

	@Override
	@JsMethod(name = "deleteEntityWithMode")
	void deleteEntity(GenericEntity entity, DeleteMode deleteMode);

	@Override
	<T extends GenericEntity> T findEntityByGlobalId(String globalId);

	@Override
	default <T extends GenericEntity> T getEntityByGlobalId(String globalId) {
		// GWT compiler can't handle EntityManager.super.getEntityByGlobalId(...)
		return requireNonNull(findEntityByGlobalId(globalId), () -> "No entity found with globalId: " + globalId);
	}

	/** Creates a {@link SessionQueryBuilder} that can be used to expressively build and execute all kinds of queries. */
	SessionQueryBuilder query();

	/** Returns the {@link EntityView} on the entities managed by this session. */
	EntityView getEntityView();

	/**
	 * Returns the {@link ModelAccessory} that can be used to access meta information for the model.
	 */
	ModelAccessory getModelAccessory();

	/** Creates a {@link ManipulationApplicationContextBuilder} to execute a {@link Manipulation} stack. */
	ManipulationApplicationContextBuilder manipulate();

	/**
	 * Creates a {@link MergeBuilder} that allows to merge entities into the current session.
	 */
	MergeBuilder merge() throws GmSessionException;

	/**
	 * Provides the {@link ResourceAccess} that allows dealing with streams.
	 */
	@Override
	ResourceAccess resources();

}

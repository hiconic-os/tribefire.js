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
package com.braintribe.gwt.tribefirejs.client.remote.impl;

import java.util.function.Supplier;

import com.braintribe.gwt.gmresource.session.GwtSessionResourceSupport;
import com.braintribe.gwt.gmsession.client.AccessServiceGwtPersistenceGmSession;
import com.braintribe.gwt.tribefirejs.client.remote.api.RpcSession;
import com.braintribe.model.usersession.UserSession;
import com.braintribe.provider.PrototypeBeanProvider;
import com.braintribe.provider.SingletonBeanProvider;

public class RawPersistenceSessionFactory {
	
	private final RpcSession rpcSession;
	private final String tribefireServicesUrl;
	private final UserSession userSession;
	
	public RawPersistenceSessionFactory(RpcSession rpcSession, String tribefireServicesUrl, UserSession userSession) {
		super();
		this.rpcSession = rpcSession;
		this.tribefireServicesUrl = tribefireServicesUrl;
		this.userSession = userSession;
	}
	
	public AccessServiceGwtPersistenceGmSession createRawSession() {
		return persistenceSession.get();
	}

	private final Supplier<AccessServiceGwtPersistenceGmSession> persistenceSession = new PrototypeBeanProvider<AccessServiceGwtPersistenceGmSession>() {
		@Override
		public AccessServiceGwtPersistenceGmSession create() throws Exception {
			AccessServiceGwtPersistenceGmSession bean = (new AccessServiceGwtPersistenceGmSession());
			bean.setResourcesAccessFactory(resourceAccess.get());
			bean.setModelAccessoryResourcesAccessFactory(accessoryResourceAccess.get());
			bean.setRequestEvaluator(rpcSession.evaluator());
			bean.setSessionIdSupplier(userSession::getSessionId);
			bean.setUserNameSupplier(userSession.getUser()::getName);
			bean.setUserRolesSupplier(userSession::getEffectiveRoles);
			return bean;
		}
	};
	
	private final Supplier<GwtSessionResourceSupport> restBasedAbstractResourceAccess = new PrototypeBeanProvider<GwtSessionResourceSupport>() {
		@Override
		public GwtSessionResourceSupport create() throws Exception {
			GwtSessionResourceSupport bean = new GwtSessionResourceSupport();
			bean.setSessionIdProvider(rpcSession.sessionIdHolder());
			bean.setStreamBaseUrl(tribefireServicesUrl + "/api/v1/");
			return bean;
		}
	};
	
	private final Supplier<GwtSessionResourceSupport> resourceAccess = new SingletonBeanProvider<GwtSessionResourceSupport>() {
		@Override
		public GwtSessionResourceSupport create() throws Exception {
			GwtSessionResourceSupport bean = publish(restBasedAbstractResourceAccess.get());
			return bean;
		}
	};
	
	private final Supplier<GwtSessionResourceSupport> accessoryResourceAccess = new SingletonBeanProvider<GwtSessionResourceSupport>() {
		@Override
		public GwtSessionResourceSupport create() throws Exception {
			GwtSessionResourceSupport bean = publish(restBasedAbstractResourceAccess.get());
			bean.setAccessoryAxis(true);
			return bean;
		}
	};
}

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
package com.braintribe.gwt.tribefirejs.remote.client;

import java.util.function.Supplier;

import com.braintribe.gwt.gmrpc.web.client.GwtGmWebRpcEvaluator;
import com.braintribe.gwt.gmsession.client.CortexTypeEnsurer;
import com.braintribe.gwt.tribefirejs.client.remote.api.RpcSession;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.service.api.ServiceRequest;
import com.braintribe.provider.Holder;
import com.braintribe.provider.SingletonBeanProvider;

public class TfJsRpc {
	private String servicesUrl;
	
	public TfJsRpc(String servicesUrl) {
		super();
		this.servicesUrl = servicesUrl;
	}

	public Supplier<GwtGmWebRpcEvaluator> serviceRequestEvaluator = new SingletonBeanProvider<GwtGmWebRpcEvaluator>() {
		@Override
		public GwtGmWebRpcEvaluator create() throws Exception {
			GwtGmWebRpcEvaluator bean = publish(new GwtGmWebRpcEvaluator());
			bean.setServerUrl(servicesUrl);
			bean.setSessionIdProvider(sessionIdProvider.get());
			bean.setTypeEnsurer(typeEnsurer.get());
			bean.setSendSessionIdExpressive(false);
			return bean;
		}
	};
	
	public Supplier<Holder<String>> sessionIdProvider = new SingletonBeanProvider<Holder<String>>() {
		@Override
		public Holder<String> create() throws Exception {
			return new Holder<String>();
		}
	};
	
	public Supplier<CortexTypeEnsurer> typeEnsurer = new SingletonBeanProvider<CortexTypeEnsurer>() {
		@Override
		public CortexTypeEnsurer create() throws Exception {
			CortexTypeEnsurer bean = publish(new CortexTypeEnsurer());
			bean.setEvaluator(serviceRequestEvaluator.get());
			return bean;
		}
	};
	
	public static RpcSession createRpcSession(String sessionId) {
		TfJsRpc rpc = new TfJsRpc(sessionId);
		
		return new RpcSession() {
			@Override
			public Evaluator<ServiceRequest> evaluator() {
				return rpc.serviceRequestEvaluator.get();
			}
			
			@Override
			public Holder<String> sessionIdHolder() {
				return rpc.sessionIdProvider.get();
			}
		};
	}
}

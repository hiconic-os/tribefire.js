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
package com.braintribe.gwt.gmrpc.base.client;

import java.util.function.Function;

import com.braintribe.exception.AuthorizationException;
import com.braintribe.exception.CommunicationException;
import com.braintribe.exception.GenericServiceException;
import com.braintribe.model.service.api.result.Failure;
import com.braintribe.model.service.api.result.ServiceResult;

public final class RpcExceptionDetection {

	private RpcExceptionDetection() {
	}

	public static Throwable detect(ServiceResult response, Function<Failure, Throwable> failureDecoder) {
		
		Failure failure = response.asFailure();

		if (failure == null) {
			return null;
		}
		else {
			Throwable exception = null;
			
			String type = failure.getType();
			
			if (type == null || type.isEmpty()) {
				
				exception = new CommunicationException("RPC server returned a failed response, but no failure type was included.");
				
			} else if (AuthorizationException.class.getName().equals(type)) {
				
				exception = new AuthorizationException(failure.getMessage());
				
			} else {
				
				exception = failureDecoder.apply(failure);
				
				if (exception == null) {
					exception = new GenericServiceException(failure.getMessage()+": "+failure.getDetails());
				}
				
			} 
			
			return exception;
		}
	}

}

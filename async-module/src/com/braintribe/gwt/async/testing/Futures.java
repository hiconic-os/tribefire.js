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
package com.braintribe.gwt.async.testing;

import java.util.concurrent.Executors;

import com.braintribe.gwt.async.client.DeferredExecutor;
import com.braintribe.gwt.async.client.Future;
import com.braintribe.gwt.async.client.Future.State;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author peter.gazdik
 */
public class Futures {

	public static final DeferredExecutor syncExecutor = Runnable::run;
	public static final DeferredExecutor singleThreadedDeferredExecutor = Executors.newSingleThreadExecutor()::execute;

	public static <T> T waitForValue(Future<T> future) {
		waitFor(future);

		return future.getResult();
	}

	public static void waitFor(Future<?> f) {
		if (f.getState() == State.outstanding) {
			MonitorCallback monitor = new MonitorCallback();

			f.get(monitor);

			synchronized (monitor) {
				if (f.getState() == State.outstanding) {
					try {
						monitor.wait(0);
					} catch (InterruptedException e) {
						// noop
					}
				}
			}
		}
	}

	// @formatter:off
	private static class MonitorCallback implements AsyncCallback<Object> {
		@Override public synchronized void onSuccess(Object value) { 
			notify(); 
			}
		@Override public synchronized void onFailure(Throwable t) { notify(); }
	}
	// @formatter:on

}

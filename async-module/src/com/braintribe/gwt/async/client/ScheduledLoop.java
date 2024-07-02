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
package com.braintribe.gwt.async.client;

import com.braintribe.gwt.async.client.WorkingAndPausingTimeMeasure.LoopBreakerCheck;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

public abstract class ScheduledLoop {
	private LoopBreakerCheck check;
	private boolean bodyRunning = false;
	private Boolean eagerAsyncResult = null;
	
	public static enum BodyResult {
		continueLoop, breakLoop, asyncContinueLoop
	}
	
	public ScheduledLoop(long maxRunningTime, long maxLoopCyclesBeforeCheck) {
		super();
		check = WorkingAndPausingTimeMeasure.getInstance().createLoopBreakerCheck(maxRunningTime, maxLoopCyclesBeforeCheck);
	}
	
	public ScheduledLoop() {
		this(250, 1);
	}

	protected abstract BodyResult body();
	
	protected void onBeforeLoopFragment() {
		//
	}
	
	protected void onAfterLoopFragment() {
		//
	}
	
	protected void onLoopFinished() {
		//
	}
	
	public final void loop() {
		Scheduler.get().scheduleIncremental(new RepeatingCommand() {
			@Override
			public boolean execute() {
				onBeforeLoopFragment();
				try {
					mainloop: while (true) {
						if (check.shouldBreak()) {
							return true;
						}
						else {
							BodyResult result = null;
							try {
								bodyRunning = true;
								eagerAsyncResult = null;
								result = body();
							}
							finally {
								bodyRunning = false;
							}
							
							switch (result) {
							case asyncContinueLoop:
								if (eagerAsyncResult != null) {
									if (eagerAsyncResult)
										break;
									else {
										onLoopFinished();
										break mainloop;
									}
								}
								else
									break mainloop;
								
							case continueLoop:
								break;
								
							case breakLoop:
								onLoopFinished();
								break mainloop;
							}
						}
					}
					
					return false;
				}
				finally {
					onAfterLoopFragment();
				}
			}
		});
	}
	
	protected void asyncReturn(boolean asyncResult) {
		if (bodyRunning) {
			// don't loop here because the return was really synced
			eagerAsyncResult = asyncResult;
		}
		else {
			// run loop here because the return was really async
			if (asyncResult)
				loop();
			else {
				onLoopFinished();
			}
		}
	}
}

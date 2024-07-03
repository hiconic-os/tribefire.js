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
package com.braintribe.common.lcd;

import com.braintribe.common.lcd.GenericTask.GenericTaskException;

/**
 * Generic interface that may be used for classes that perform tasks using a non-<code>null</code> context provided by
 * the calling classes. One purpose of this interface is to avoid creating undesirable dependencies.
 *
 * @author michael.lafite
 *
 * @param <T>
 *            the type of the task context.
 *
 * @see GenericTaskWithNullableContext
 * @see GenericTask
 */
public interface GenericTaskWithContext<T> {

	void perform(T taskContext) throws GenericTaskException;

}

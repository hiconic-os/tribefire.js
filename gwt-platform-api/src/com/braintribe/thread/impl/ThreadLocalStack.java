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
package com.braintribe.thread.impl;

import java.util.Deque;
import java.util.LinkedList;

/**
 * An alternative to {@code ThreadLocalStackedHolder} providing more direct push/peek/pop methods.
 * 
 *
 * @param <T>
 *            The type of elements held in this queue
 */
public class ThreadLocalStack<T> {

	private ThreadLocal<Deque<T>> threadLocal = new ThreadLocal<Deque<T>>();

	public void push(T object) {

		if (object == null) {
			throw new IllegalArgumentException("Cannot push null");
		}

		if (threadLocal.get() == null) {
			threadLocal.set(new LinkedList<T>());
		}

		threadLocal.get().push(object);

	}

	public T peek() {
		return (threadLocal.get() != null) ? threadLocal.get().peek() : null;
	}

	public T pop() {

		T popped = null;

		if (threadLocal.get() != null) {

			popped = threadLocal.get().pop();

			if (threadLocal.get().isEmpty()) {
				threadLocal.remove();
			}
		}

		return popped;

	}

}

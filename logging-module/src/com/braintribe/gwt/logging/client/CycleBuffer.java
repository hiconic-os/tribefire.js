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
package com.braintribe.gwt.logging.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CycleBuffer<E> implements Collection<E> {
	private ArrayList<E> elements;
	private int offset;
	private int capacity;
	
	public CycleBuffer(int capacity) {
		elements = new ArrayList<E>(capacity);
		this.capacity = capacity;
	}
	
	@Override
	public boolean add(E element) {
		if (elements.size() < capacity) {
			elements.add(element);
		}
		else {
			offset = (offset + 1) % capacity;
			elements.set(offset - 1, element);
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> elements) {
		for (E element: elements) 
			add(element);
		return true;
	}

	@Override
	public void clear() {
		elements.clear();
		offset = 0;
	}

	@Override
	public boolean contains(Object element) {
		return elements.contains(element);
	}

	@Override
	public boolean containsAll(Collection<?> elements) {
		return this.elements.containsAll(elements);
	}

	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return elements.size();
	}

	@Override
	public Object[] toArray() {
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return null;
	}

}

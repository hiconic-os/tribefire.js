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
package com.braintribe.model.generic.manipulation.accessor;

import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated Please use the GmExpertRegistry of GmCoreCommons
 */
@Deprecated
public class ExpertRegistry {
	private ExpertRegistry parent;
	
	private Map<ExpertKey, Object> expertMap;
	
	public void setParent(ExpertRegistry parent) {
		this.parent = parent;
	}

	public void setExpertMap(Map<ExpertKey, Object> expertMap) {
		this.expertMap = expertMap;
	}

	public Map<ExpertKey, Object> getExpertMap() {
		return expertMap;
	}
	
	public <T> T getExpert(Class<T> expertClass, Class<?> handledClass) {
		return getExpert(new ExpertKey(expertClass, handledClass));
	}
	
	public <T> T getExpert(Class<T> expertClass, Object handledObject) {
		return getExpert(expertClass, handledObject != null? handledObject.getClass(): Void.class);
	}
	
	public <T> T getExpert(ExpertKey expertKey) {
		T expert = (T)getDirectExpert(expertKey);
		
		if (expert != null || expertKey.getHandledClass().getSuperclass() == null) {
			return expert;
		} else {
			return (T) getExpert(prepareKeyWithSuperClass(expertKey));
		}
	}
	
	protected <T> T getDirectExpert(ExpertKey expertKey) {
		T expert = (T) expertMap.get(expertKey);
		
		if (expert == null && parent != null)
			expert = parent.getDirectExpert(expertKey);
		
		return expert;
	}
	
	private ExpertKey prepareKeyWithSuperClass(ExpertKey expertKey) {
		return new ExpertKey(expertKey.getExpertClass(), expertKey.getHandledClass().getSuperclass());
	}
	
	public <T, T1 extends T> void registerExpert(Class<T> expertClass,
			Class<?> handledClass, T1 expert) {
		Map<ExpertKey, Object> expertMap = getExpertMap();
		if (expertMap == null) {
			expertMap = new HashMap<ExpertKey, Object>();
			setExpertMap(expertMap);
		}

		expertMap.put(new ExpertKey(expertClass, handledClass), expert);
	}
}

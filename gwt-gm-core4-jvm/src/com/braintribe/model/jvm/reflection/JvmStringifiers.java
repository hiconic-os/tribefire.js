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
package com.braintribe.model.jvm.reflection;

import java.util.function.Function;

import com.braintribe.logging.Logger;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.processing.core.expert.api.MutableDenotationMap;
import com.braintribe.model.processing.core.expert.impl.PolymorphicDenotationMap;

/**
 * @author peter.gazdik
 */
public class JvmStringifiers {

	private static final Logger log = Logger.getLogger(JvmStringifiers.class);

	// This is thread-safe
	private static volatile MutableDenotationMap<GenericEntity, Function<? extends GenericEntity, String>> stringifiers = new PolymorphicDenotationMap<>();

	static {
		// initialize the configurators, including ones for stringifiers
		JvmGmfConfigurators.triggerClassLoading();
	}

	public static <T extends GenericEntity> void register(EntityType<T> baseType, Function<T, String> stringifier) {
		stringifiers.put(baseType, stringifier);
	}

	public static String stringify(GenericEntity entity) {
		Function<GenericEntity, String> stringifier = findStringifier(entity);

		return stringifier == null ? entity.toString() : applyStringifierSafely(entity, stringifier);
	}

	private static String applyStringifierSafely(GenericEntity entity, Function<GenericEntity, String> stringifier) {
		try {
			return stringifier.apply(entity);

		} catch (Exception e) {
			log.warn("[APPLICATION]: " + e.getMessage());
			return "<stringifier failed, see logs> " + entity.toString();
		}
	}

	private static Function<GenericEntity, String> findStringifier(GenericEntity entity) {
		try {
			return (Function<GenericEntity, String>) stringifiers.find(entity);

		} catch (Exception e) {
			log.warn("[STRINGIFIER CONFIGURATION]: " + e.getMessage());
			return null;
		}
	}

}

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
package com.braintribe.gwt.genericmodel.client.itw;

import com.braintribe.model.generic.reflection.TypeCode;

public class DefaultLiterals {

	private static Integer default_int = 0;
	private static Long default_long = 0L;
	private static Float default_float = 0F;
	private static Double default_double = 0D;
	@Deprecated
	public static Boolean default_boolean = Boolean.FALSE;

	public static Object forType(TypeCode typeCode) {
		switch (typeCode) {
			case booleanType:
				return Boolean.FALSE;
			case doubleType:
				return default_double;
			case floatType:
				return default_float;
			case integerType:
				return default_int;
			case longType:
				return default_long;
			default:
				// hopefully unreachable
				return null;
		}
	}

}

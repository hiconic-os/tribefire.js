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
package com.braintribe.model.generic.reflection.type.simple;

import com.braintribe.model.generic.reflection.FloatType;
import com.braintribe.model.generic.reflection.GenericModelException;
import com.braintribe.model.generic.reflection.TypeCode;
import com.braintribe.model.generic.reflection.type.JsTypeUtils;
import com.braintribe.model.generic.tools.GmValueCodec;

@SuppressWarnings("unusable-by-js")
public class FloatTypeImpl extends AbstractSimpleType implements FloatType {
	public static final FloatTypeImpl INSTANCE = new FloatTypeImpl();

	private FloatTypeImpl() {
		super(Float.class);
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	@Override
	public String getTypeName() {
		return "float";
	}

	@Override
	public TypeCode getTypeCode() {
		return TypeCode.floatType;
	}

	@Override
	public Object getDefaultValue() {
		return 0F;
	}

	@Override
	public Class<?> getPrimitiveJavaType() {
		return float.class;
	}

	@Override
	public <T> T instanceFromString(String s) throws GenericModelException {
		return (T) Float.valueOf(s);
	}

	@Override
	public String instanceToGmString(Object value) throws GenericModelException {
		return GmValueCodec.floatToGmString((Float) value);
	}

	@Override
	public Object instanceFromGmString(String encodedValue) {
		return GmValueCodec.floatFromGmString(encodedValue);
	}

	@Override
	public boolean isInstanceJs(Object value) {
		return JsTypeUtils.isJsFloat(value);
	}
}

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
package com.braintribe.gwt.codec.string.client;

import com.braintribe.codec.Codec;
import com.braintribe.codec.CodecException;

public class ShortCodec implements Codec<Short, String> {
	
	@Override
	public Short decode(String strValue) throws CodecException  {
		return strValue == null || strValue.trim().length() == 0 ? null
				: new Short(strValue.trim());
	}

	@Override
	public String encode(Short obj) throws CodecException {
		return obj == null ? "" : obj.toString();
	}

	@Override
	public Class<Short> getValueClass() {
		return Short.class;
	}
}

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
package com.braintribe.common.lcd.uncheckedcounterpartexceptions;

import java.io.IOException;

/**
 * Unchecked counterpart of {@link IOException}.
 *
 * @author michael.lafite
 * @deprecated Since Java 1.8, there is a native {@link java.io.UncheckedIOException} class.
 */
@Deprecated
public class UncheckedIOException extends UncheckedCounterpartException {

	private static final long serialVersionUID = 5535375051187793834L;

	public UncheckedIOException(final String message) {
		super(message);
	}

	public UncheckedIOException(final String message, final IOException cause) {
		super(message, cause);
	}
}

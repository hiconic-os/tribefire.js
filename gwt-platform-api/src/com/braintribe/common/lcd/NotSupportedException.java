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

/**
 * Indicates that a method (or service, feature, etc.) is intentionally not supported. If it's just not implemented yet,
 * {@link NotImplementedException} should be used.
 *
 * @author michael.lafite
 */
public class NotSupportedException extends AbstractUncheckedBtException {

	private static final long serialVersionUID = -1731584496205854607L;

	public NotSupportedException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public NotSupportedException(final String message) {
		super(message);
	}

	public NotSupportedException() {
		// no message
		this(null);
	}
}

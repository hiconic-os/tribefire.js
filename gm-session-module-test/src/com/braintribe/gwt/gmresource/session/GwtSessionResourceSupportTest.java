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
package com.braintribe.gwt.gmresource.session;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.braintribe.model.processing.session.api.persistence.AccessDescriptor;

public class GwtSessionResourceSupportTest {

	@Test
	public void accessoryResourcesHonorDescriptorAccessId() {
		GwtSessionResourceSupport support = support();
		support.setAccessoryAxis(true);

		RestBasedResourceAccessBuilder access = (RestBasedResourceAccessBuilder) support
				.newInstance(new AccessDescriptor("model-resources", null, null));

		assertThat(access.accessIdProvider.get()).isEqualTo("model-resources");
	}

	@Test
	public void explicitDomainIdIsNeverReplaced() {
		GwtSessionResourceSupport support = support();
		support.setAccessoryAxis(true);

		RestBasedResourceAccessBuilder access = (RestBasedResourceAccessBuilder) support.newInstanceForDomainId("model-resources");

		assertThat(access.accessIdProvider.get()).isEqualTo("model-resources");
	}

	private static GwtSessionResourceSupport support() {
		GwtSessionResourceSupport support = new GwtSessionResourceSupport();
		support.setStreamBaseUrl("/services/api/v1");
		support.setSessionIdProvider(() -> "session");
		return support;
	}
}

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
package com.braintribe.model.processing.notification.api.builder;

import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.processing.notification.api.NotificationAwareEvalContext;
import com.braintribe.model.processing.notification.api.builder.impl.BasicNotificationsBuilder;
import com.braintribe.model.processing.session.api.persistence.PersistenceGmSession;

public class Notifications {

	
	public static NotificationsBuilder build() {
		return new BasicNotificationsBuilder();
	}

	public static NotificationsBuilder buildWithSession(PersistenceGmSession session) {
		return new BasicNotificationsBuilder(session);
	}
	
	public static <T> NotificationAwareEvalContext<T> makeNotificationAware(EvalContext<T> other) {
		return new FailureNotificationAwareEvalContext<>(other);
	}
	
}

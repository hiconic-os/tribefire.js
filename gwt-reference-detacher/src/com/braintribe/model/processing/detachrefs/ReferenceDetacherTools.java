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
package com.braintribe.model.processing.detachrefs;

import com.braintribe.logging.Logger;
import com.braintribe.model.meta.data.QualifiedProperty;
import com.braintribe.model.meta.data.constraint.Mandatory;
import com.braintribe.model.processing.meta.cmd.CmdResolver;

/**
 * 
 */
public class ReferenceDetacherTools {

	private static final Logger log = Logger.getLogger(ReferenceDetacherTools.class);

	public static String canDetachValueFrom(QualifiedProperty qualifiedProperty, CmdResolver cmdResolver) {
		try {
			if (cmdResolver.getMetaData().entityType(qualifiedProperty.getEntityType()).property(qualifiedProperty.getProperty()).is(Mandatory.T))
				return "property is mandatory";

		} catch (Exception e) {
			log.error("Failed to resolve 'MandatoryProperty' meta data", e);
		}

		return null;
	}

}

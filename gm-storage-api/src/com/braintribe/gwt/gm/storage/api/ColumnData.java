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
package com.braintribe.gwt.gm.storage.api;

import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.i18n.LocalizedString;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface ColumnData extends GenericEntity {
	
	final EntityType<ColumnData> T = EntityTypes.T(ColumnData.class);

	// @formatter:off
	boolean getDisplayNode();
	void setDisplayNode(boolean displayNode);
	
	boolean getPreventSingleEntryExpand();
	void setPreventSingleEntryExpand(boolean prevent);
	
	Integer getNodeWidth();
	void setNodeWidth(Integer nodeWidth);
	
	LocalizedString getNodeTitle();
	void setNodeTitle(LocalizedString nodeTitle);

	List<StorageColumnInfo> getDisplayPaths();
	void setDisplayPaths(List<StorageColumnInfo> displayPaths);
	
	void setDisableExpansion(boolean disable);
	boolean getDisableExpansion();
	// @formatter:on
	
}

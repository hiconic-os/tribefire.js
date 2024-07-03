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
package com.braintribe.model.processing.meta.editor.empty;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.braintribe.model.meta.GmEnumType;
import com.braintribe.model.meta.data.MetaData;
import com.braintribe.model.meta.info.GmEnumConstantInfo;
import com.braintribe.model.meta.info.GmEnumTypeInfo;
import com.braintribe.model.processing.meta.editor.EnumTypeMetaDataEditor;

/**
 * @author peter.gazdik
 */
@SuppressWarnings("unusable-by-js")
public class EmptyEnumTypeMetaDataEditor implements EnumTypeMetaDataEditor {

	public static final EmptyEnumTypeMetaDataEditor INSTANCE = new EmptyEnumTypeMetaDataEditor();

	private EmptyEnumTypeMetaDataEditor() {
	}

	@Override
	public GmEnumType getEnumType() {
		throw new UnsupportedOperationException("Method 'EmptyEnumTypeMetaDataEditor.getEnumType' is not supported!");
	}

	@Override
	public EnumTypeMetaDataEditor configure(Consumer<GmEnumTypeInfo> consumer) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor configure(String constant, Consumer<GmEnumConstantInfo> consumer) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor configure(Enum<?> constant, Consumer<GmEnumConstantInfo> consumer) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor configure(GmEnumConstantInfo constant, Consumer<GmEnumConstantInfo> consumer) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor addMetaData(MetaData... mds) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor removeMetaData(Predicate<? super MetaData> filter) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor addConstantMetaData(MetaData... mds) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor removeConstantMetaData(Predicate<? super MetaData> filter) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor addConstantMetaData(String constant, MetaData... mds) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor removeConstantMetaData(String constant, Predicate<? super MetaData> filter) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor addConstantMetaData(Enum<?> constant, MetaData... mds) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor removeConstantMetaData(Enum<?> constant, Predicate<? super MetaData> filter) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor addConstantMetaData(GmEnumConstantInfo gmConstantInfo, MetaData... mds) {
		return this;
	}

	@Override
	public EnumTypeMetaDataEditor removeConstantMetaData(GmEnumConstantInfo gmConstantInfo, Predicate<? super MetaData> filter) {
		return this;
	}

}

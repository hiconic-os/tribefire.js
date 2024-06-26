// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.model.processing.query.expander;

import com.braintribe.model.processing.query.api.expander.QuerySignatureExpander;
import com.braintribe.model.processing.query.api.shortening.SignatureExpert;
import com.braintribe.model.query.Query;

public class QueryTypeSignatureExpanderBuilder {
	private final QuerySignatureExpander signatureExpander;

	/***************** Static Methods ******************/

	public static QueryTypeSignatureExpanderBuilder create() {
		return new QueryTypeSignatureExpanderBuilder();
	}

	public static QueryTypeSignatureExpanderBuilder create(final Query query, final SignatureExpert expandMode) {
		return new QueryTypeSignatureExpanderBuilder().query(query).mode(expandMode);
	}

	/**************** Instanced Methods ****************/

	private QueryTypeSignatureExpanderBuilder() {
		this.signatureExpander = new QueryTypeSignatureExpanderImpl();
	}

	public QueryTypeSignatureExpanderBuilder query(final Query shortenedQuery) {
		this.signatureExpander.setQuery(shortenedQuery);
		return this;
	}

	public QueryTypeSignatureExpanderBuilder mode(final SignatureExpert expandMode) {
		this.signatureExpander.setMode(expandMode);
		return this;
	}

	public Query done() {
		return this.signatureExpander.expandTypeSignature();
	}
}

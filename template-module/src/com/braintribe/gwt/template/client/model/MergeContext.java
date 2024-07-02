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
package com.braintribe.gwt.template.client.model;

import java.util.function.Function;

import com.braintribe.gwt.template.client.TemplateException;


public class MergeContext {
	private boolean sourceMode;
	private Function<String, ?> variableProvider;
	private Function<String, String> literalEscaper;
	
	public void setLiteralEscaper(Function<String, String> literalEscaper) {
		this.literalEscaper = literalEscaper;
	}
	
	public void setVariableProvider(Function<String, ?> variableProvider) {
		this.variableProvider = variableProvider;
	}
	
	public void setSourceMode(boolean sourceMode) {
		this.sourceMode = sourceMode;
	}
	
	public boolean isSourceMode() {
		return sourceMode;
	}
	
	public Function<String, ?> getVariableProvider() {
		return variableProvider;
	}
	
	public Object getVariableValue(String variableName) throws TemplateException {
		try {
			return variableProvider.apply(variableName);
		} catch (RuntimeException e) {
			throw new TemplateException("error while accessing variable " + variableName , e);
		}
	}
	
	public String createSourceLiteral(String text) throws TemplateException {
		if (literalEscaper != null) {
			try {
				text = literalEscaper.apply(text);
			} catch (RuntimeException e) {
				throw new TemplateException("error while escaping literal string", e);
			}
		}
			
		return '"' + text + '"';
	}
}

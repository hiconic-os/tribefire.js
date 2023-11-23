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
package com.braintribe.model.processing.itw.synthesis.gm.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.braintribe.asm.MethodVisitor;

public class Sequence implements TemplateNode {
	private final List<TemplateNode> nodes = new ArrayList<TemplateNode>();

	public void add(TemplateNode templateNode) {
		nodes.add(templateNode);
	}

	@Override
	public void merge(MethodVisitor mv, VariableResolver variableResolver) {
		for (TemplateNode node: nodes)
			node.merge(mv, variableResolver);
	}

	@Override
	public void collectVariables(Set<Variable> variables) {
		for (TemplateNode node: nodes)
			node.collectVariables(variables);
	}

}

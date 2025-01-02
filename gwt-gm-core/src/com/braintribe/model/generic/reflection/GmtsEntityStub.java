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
package com.braintribe.model.generic.reflection;

import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.GmSystemInterface;
import com.braintribe.model.generic.eval.EvalContext;
import com.braintribe.model.generic.eval.Evaluator;
import com.braintribe.model.generic.eval.JsEvalContext;
import com.braintribe.model.generic.reflection.type.custom.AbstractEntityType;
import com.braintribe.model.generic.value.EntityReference;
import com.braintribe.model.generic.value.ValueDescriptor;
import com.braintribe.processing.async.api.JsPromise;

import jsinterop.annotations.JsMethod;

/**
 * @author peter.gazdik
 */
@GmSystemInterface
@SuppressWarnings("unusable-by-js")
public abstract class GmtsEntityStub extends GmtsBaseEntityStub implements EvaluableEntity {

	private volatile long runtimeId;
	private Property[] propertyNames;
	private String[] properties;

	public GmtsEntityStub() {
		//
	}

	@Override
	public void read(Property p, PropertyValueReceiver pvr) {
		Object fieldValue = p.getDirectUnsafe(this);

		ValueDescriptor vd = VdHolder.getValueDescriptorIfPossible(fieldValue);
		if (vd != null)
			pvr.receiveVd(vd);
		else
			pvr.receive(fieldValue);
	}

	public Object idOrRid() {
		Object id = getId();
		return id != null ? id : "~" + runtimeId;
	}

	@Override
	public long runtimeId() {
		return runtimeId != 0 ? runtimeId : ensureRuntimeId();
	}

	private synchronized long ensureRuntimeId() {
		return runtimeId != 0 ? runtimeId : (runtimeId = RuntimeIdGenerator.nextId());
	}

	// TODO change that entityType is actually implemented by ITW and type delegates to it...
	@Override
	public abstract GenericModelType type();

	public <T extends GenericEntity> AbstractEntityType<T> abstractEntityType() {
		return (AbstractEntityType<T>) type();
	}

	@Override
	public <T extends GenericEntity> EntityType<T> entityType() {
		return (EntityType<T>) type();
	}

	@Override
	public <T extends EntityReference> T reference() {
		return (T) this.entityType().createReference(this, this.getId());
	}

	@Override
	public <T extends EntityReference> T globalReference() {
		return (T) this.entityType().createGlobalReference(this, this.getGlobalId());
	}

	@Override
	public <T> T clone(CloningContext cloningContext) {
		return entityType().clone(cloningContext, this, null);
	}

	@Override
	public void traverse(TraversingContext traversingContext) {
		entityType().traverse(traversingContext, this);
	}

	@Override
	public boolean isVd() {
		return type().isVd();
	}

	@Override
	public String toString() {
		return abstractEntityType().toString(this);
	}

	@Override
	public final String toSelectiveInformation() {
		return type().getSelectiveInformation(this);
	}

	/** Using rawTypes because it doesn't matter, this method is only used from javascript and the TypeScript has correct generics. */
	@SuppressWarnings("rawtypes")
	@JsMethod(name = "EvalAndGet")
	public JsPromise evalAndGet(Evaluator evaluator) {
		return eval(evaluator).andGet();
	}

	/** Using rawTypes because it doesn't matter, this method is only used from javascript and the TypeScript has correct generics. */
	@SuppressWarnings("rawtypes")
	@JsMethod(name = "EvalAndGetReasoned")
	public JsPromise evalAndGetReasoned(Evaluator evaluator) {
		return eval(evaluator).andGetReasoned();
	}

	/** Using rawTypes so it is compatible with the sources generated for the GWT compiler. */
	@Override
	@SuppressWarnings("rawtypes")
	@JsMethod(name = "Eval")
	public JsEvalContext eval(Evaluator evaluator) {
		return new JsEvalContextImpl(evaluator.eval(this));
	}

	@Override
	public GenericEntity deproxy() {
		return this;
	}

	// <GWT ONLY>

	// we call the public methods starting with Uppercase so they do not conflict with potential default methods on entities

	@JsMethod(name = "Properties")
	public native Property[] PropertiesJs() /*-{
		if (this.@GmtsEntityStub::properties == null)
			this.@GmtsEntityStub::properties = Object.freeze(this.@GmtsEntityStub::getProperties()());
		return this.@GmtsEntityStub::properties
	}-*/;

	private Property[] getProperties() {
		List<Property> ps = entityType().getProperties();
		return ps.toArray(new Property[ps.size()]);
	}

	@JsMethod(name = "PropertyNames")
	public native Property[] PropertyNamesJs() /*-{
		if (this.@GmtsEntityStub::propertyNames == null)
			this.@GmtsEntityStub::propertyNames = Object.freeze(this.@GmtsEntityStub::getPropertyNames()());
		return this.@GmtsEntityStub::propertyNames;
	}-*/;

	private native String[] getPropertyNames() /*-{
		var props = this.@GmtsEntityStub::PropertiesJs()();
		return props.map(function(p){return p.getName()});
	}-*/;

	@JsMethod(name = "TypeSignature")
	public String TypeSignature() {
		return entityType().getTypeSignature();
	}

}

/** This exists just to have bytecode for this method signature (alongside one returning JsEvalContext). Relevant when debugging in JVM. */
interface EvaluableEntity {
	@SuppressWarnings("rawtypes")
	EvalContext eval(Evaluator evaluator);
}
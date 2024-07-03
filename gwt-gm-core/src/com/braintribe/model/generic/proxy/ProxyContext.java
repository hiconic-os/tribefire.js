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
package com.braintribe.model.generic.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.ScalarType;
import com.braintribe.processing.async.api.AsyncCallback;

public class ProxyContext {
	protected List<DeferredApplier> appliers = new ArrayList<>();
	//private Map<String, EntityType<?>> resolvedProxyTypes = new HashMap<>();
	protected Map<String, ProxyEntityType> proxyEntityTypes = new HashMap<>();
	protected Map<String, ProxyEnumType> proxyEnumTypes = new HashMap<>();

	public void defer(DeferredApplier applier) {
		appliers.add(applier);
	}
	
	public void deferPropertyAssignment(GenericEntity target, Property property, ProxyValue value) {
		if (property.getClass() == ProxyProperty.class) {
			/* casting the entity but do not register as registering is 
			 * only needed for application onto another target and in that
			 * case it would be registered in that very situation
			 */
			ProxyEntity proxyEntity = (ProxyEntity)target;
			
			
			ProxyProperty proxyProperty = (ProxyProperty)property;
			appliers.add(new DeferredProxyPropertyAssigner(proxyEntity, proxyProperty, value));
		}
		else {
			appliers.add(new DeferredPropertyAssigner(target, property, value));
		}

	}
	
	public void deferListInsert(java.util.List<Object> target, ProxyValue value, int position) {
		appliers.add(new DeferredListInserter(target, position, value));
	}

	public void deferCollectionAdd(java.util.Collection<Object> target, ProxyValue value) {
		appliers.add(new DeferredCollectionAdder(target, value));
	}

	public void deferMapPut(java.util.Map<Object,Object> target, ProxyValue key, Object value) {
		appliers.add(new DeferredKeyMapPutter(target, key, value));
	}

	
	public void deferMapPut(java.util.Map<Object,Object> target, Object key, ProxyValue value) {
		appliers.add(new DeferredValueMapPutter(target, key, value));
	}

	
	public void deferMapPut(java.util.Map<Object,Object> target, ProxyValue key, ProxyValue value) {
		appliers.add(new DeferredEntryMapPutter(target, key, value));
	}

	
	public void resolveProxiesAndApply(AsyncCallback<Void> callback) {
		try {
			resolveProxiesAndApply();
		} catch (Exception e) {
			callback.onFailure(e);
			return;
		}
		
		callback.onSuccess(null);
	}
	
	public void resolveProxiesAndApply() {
		for (DeferredApplier applier: appliers) {
			applier.apply();
		}
	}

	public ProxyEntityType getProxyEntityType(String typeSignature) {
		ProxyEntityType proxyEntityType = proxyEntityTypes.get(typeSignature);
		if (proxyEntityType == null) {
			proxyEntityType = new ProxyEntityType(this, typeSignature);
			proxyEntityTypes.put(typeSignature, proxyEntityType);
		}
		return proxyEntityType;
	}
	
	
	public ScalarType getProxyEnumType(String typeSignature) {
		ProxyEnumType proxyEnumType = proxyEnumTypes.get(typeSignature);
		if (proxyEnumType == null) {
			proxyEnumType = new ProxyEnumType(this, typeSignature);
			proxyEnumTypes.put(typeSignature, proxyEnumType);
		}
		return proxyEnumType;
	}
	
	public void onCreateEntity(ProxyEntity proxyEntity) {
		appliers.add(new DeferredEntityInstantiator(proxyEntity));
	}
	
	public void onCreateEnum(ProxyEnum proxyEnum) {
		appliers.add(new DeferredEnumInstantiator(proxyEnum));
	}
	
}

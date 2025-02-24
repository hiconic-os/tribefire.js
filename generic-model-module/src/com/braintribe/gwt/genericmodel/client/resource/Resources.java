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
package com.braintribe.gwt.genericmodel.client.resource;

import java.util.Date;

import com.braintribe.gwt.fileapi.client.Blob;
import com.braintribe.gwt.fileapi.client.File;
import com.braintribe.model.generic.GMF;
import com.braintribe.model.generic.GmCoreApiInteropNamespaces;
import com.braintribe.model.generic.session.InputStreamProvider;
import com.braintribe.model.resource.Resource;
import com.braintribe.model.resource.source.TransientSource;

import jsinterop.annotations.JsMethod;

@SuppressWarnings("unusable-by-js")
public interface Resources {

	@JsMethod(namespace = GmCoreApiInteropNamespaces.resources)
	public static Resource fromBlob(Blob blob) {
		TransientSource source = sourceFromBlob(blob);

		Resource resource = Resource.T.create();

		resource.setResourceSource(source);
		source.setOwner(resource);

		resource.setFileSize(blob.size());
		resource.setMimeType(blob.type());
		resource.setCreated(new Date());

		return resource;
	}
	
	@JsMethod(namespace = GmCoreApiInteropNamespaces.resources)
	public static TransientSource sourceFromBlob(Blob blob) {
		String uuid = GMF.platform().newUuid();

		GwtInputStreamProvider streamProvider = new GwtInputStreamProvider(blob);

		TransientSource source = TransientSource.T.create();
		source.setGlobalId(uuid);
		source.setInputStreamProvider(streamProvider);

		return source;
	}

	@JsMethod(namespace = GmCoreApiInteropNamespaces.resources)
	public static Resource fromFile(File file) {
		Resource resource = fromBlob(file);
		resource.setName(file.getName());

		return resource;
	}
	
	@JsMethod(namespace = GmCoreApiInteropNamespaces.resources)
	/**
	 * Returns the associated {@link Blob} if existing otherwise null
	 */
	public static Blob hasBlob(TransientSource resource) {
		InputStreamProvider provider = resource.getInputStreamProvider();
		
		if (provider instanceof GwtInputStreamProvider) {
			return ((GwtInputStreamProvider) provider).blob();
		}
		
		return null; 
	}
}

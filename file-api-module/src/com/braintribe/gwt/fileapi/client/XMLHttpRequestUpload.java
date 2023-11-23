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
package com.braintribe.gwt.fileapi.client;

import com.google.gwt.core.client.JavaScriptObject;

public class XMLHttpRequestUpload extends JavaScriptObject {
	protected XMLHttpRequestUpload() {
		
	}

//	public final native void setProgressHandler(ProgressHandler handler) /*-{
//		this.onprogress = $entry(function(event) {
//			handler.@com.braintribe.gwt.fileapi.client.ProgressHandler::onProgress(Lcom/braintribe/gwt/fileapi/client/ProgressEvent;)(event);
//		});
//	}-*/;
	
	public final native void setProgressHandler(ProgressHandler handler) /*-{
		this.onprogress = $entry(function(event) {
			handler(event);
		});
	}-*/;
}

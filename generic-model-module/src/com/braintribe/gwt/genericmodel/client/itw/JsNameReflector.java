package com.braintribe.gwt.genericmodel.client.itw;

import com.google.gwt.core.client.JavaScriptObject;

public class JsNameReflector {

	public static JavaScriptObject JsNR = createNameReflector();

	private static native JavaScriptObject createNameReflector() /*-{
		var handler = {
		    get: function(target, prop, receiver) {
		        return prop;
		    }
		};

		return new Proxy({}, handler);
	}-*/;

}

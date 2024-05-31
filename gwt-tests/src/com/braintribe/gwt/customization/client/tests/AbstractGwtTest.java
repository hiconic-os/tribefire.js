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
package com.braintribe.gwt.customization.client.tests;

import com.braintribe.gwt.logging.client.ExceptionUtil;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.dom.client.Style.Unit;

/**
 * @author peter.gazdik
 */
public abstract class AbstractGwtTest {

	public void run() {
		log("Running: " + this.getClass().getSimpleName());

		try {
			tryRun();

		} catch (Exception e) {
			logError("error: " + e.getMessage(), e);
		}

		logSeparator();
	}

	protected abstract void tryRun() throws Exception;

	// ##########################################
	// ## . . . . . . Util methods . . . . . . ##
	// ##########################################

	protected static <T> T cast(Object o) {
		return (T) o;
	}

	// ##########################################
	// ## . . . . . Assertions . . . . . . . . ##
	// ##########################################

	protected void assertNotNull(Object o, String descriptor) {
		if (o == null)
			throw new RuntimeException("Object is null: " + descriptor);
	}

	// ##########################################
	// ## . . . . . . . tf.js . . . . . . . . .##
	// ##########################################

	protected final native JavaScriptObject getJsProperty(JavaScriptObject jso, String name) /*-{
		return jso[name];
	}-*/;

	protected final native <T> T getJsPropertyCasted(JavaScriptObject jso, String name) /*-{
		return jso[name];
	}-*/;

	// ##########################################
	// ## . . . . . . . Printing . . . . . . . ##
	// ##########################################

	protected void logSeparator() {
		Document document = Document.get();
		document.getBody().appendChild(document.createHRElement());
	}

	protected void log(String msg) {
		Document document = Document.get();
		PreElement preElement = document.createPreElement();
		preElement.appendChild(document.createTextNode(msg));
		preElement.getStyle().setMargin(0, Unit.PX);
		document.getBody().appendChild(preElement);
	}

	protected void logError(String msg, Throwable t) {
		Document document = Document.get();
		PreElement preElement = document.createPreElement();
		preElement.getStyle().setColor("red");
		preElement.getStyle().setMargin(0, Unit.PX);
		preElement.appendChild(document.createTextNode(msg + "\n" + ExceptionUtil.format(t)));
		document.getBody().appendChild(preElement);
	}

	protected void logError(String msg) {
		Document document = Document.get();
		PreElement preElement = document.createPreElement();
		preElement.getStyle().setColor("red");
		preElement.getStyle().setMargin(0, Unit.PX);
		preElement.appendChild(document.createTextNode(msg));
		document.getBody().appendChild(preElement);
	}

}

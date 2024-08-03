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
package com.braintribe.gwt.customization.client;

import com.braintribe.gwt.customization.client.tests.BasicItwTest;
import com.braintribe.gwt.customization.client.tests.DefaultMethodsTest;
import com.braintribe.gwt.customization.client.tests.EssentialMdTest;
import com.braintribe.gwt.customization.client.tests.EvaluatesToTest;
import com.braintribe.gwt.customization.client.tests.InitializerTest;
import com.braintribe.gwt.customization.client.tests.InstanceOfTest;
import com.braintribe.gwt.customization.client.tests.KeywordTest;
import com.braintribe.gwt.customization.client.tests.MethodsMultiInheritanceTest;
import com.braintribe.gwt.customization.client.tests.PartialModelTest;
import com.braintribe.gwt.customization.client.tests.Props_CompileTimeEntity_Test;
import com.braintribe.gwt.customization.client.tests.Props_RuntimeEntity_Test;
import com.braintribe.gwt.customization.client.tests.SingleCharEnumTest;
import com.braintribe.gwt.customization.client.tests.ToStringTest;
import com.braintribe.gwt.customization.client.tests.TransientPropertyTest;
import com.braintribe.gwt.customization.client.tests.collectionish.ArrayishTest;
import com.braintribe.gwt.customization.client.tests.collectionish.MapishTest;
import com.braintribe.gwt.customization.client.tests.collectionish.SetishTest;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.dom.client.Style.Unit;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtTestsEntryPoint implements EntryPoint {

	/** Entry point method. */
	@Override
	public void onModuleLoad() {
		new Props_CompileTimeEntity_Test().run();
		new Props_RuntimeEntity_Test().run();
		new BasicItwTest().run();
		new MapishTest().run();
		new SetishTest().run();
		new ArrayishTest().run();
		new SingleCharEnumTest().run();
		new EssentialMdTest().run();
		new InitializerTest().run();
		new MethodsMultiInheritanceTest().run();
		new DefaultMethodsTest().run();
		new ToStringTest().run();
		new EvaluatesToTest().run();
		new InstanceOfTest().run();
		new TransientPropertyTest().run();
		new PartialModelTest().run();
		new KeywordTest().run();

		// NOT a test
		// new SimpleTypesOverview().run();

		// new JseReadTest().run();
	}

	// @Override
	// public void onModuleLoad() {
	// ColorPicker colorPicker = new ColorPicker();
	// Picker[] pickers = new Picker[5]; // if this is IPicker, GWT does not work
	// pickers[0] = new Picker(123);
	// log("Hello World" + colorPicker.pickColor("peter") + pickers.length + " " + colorPicker);
	//
	// testPicker();
	//
	// JavascriptMemberReflection<IPicker> ipickerReflection = GWT.create(IPickerJsMemberReflection.class);
	// log("JsMethodNames: " + ipickerReflection.getJsMethodNames());
	//
	// }

	// @SuppressWarnings("cast")
	// private void testPicker() {
	// JavaScriptObject entityType = foo();
	//
	// IPicker ipicker = getDynamicPicker(entityType);
	// log("dynamic picker type: " + ipicker.pickerType());
	//
	// log("is dynamic instance of iface: " + (ipicker instanceof IPicker));
	//
	// Class<?> clazz = ipicker.getClass();
	//
	// log("class name: " + clazz.getName());
	// log("super class name: " + clazz.getSuperclass().getName());
	// }
	//
	// private IPicker getColorPicker() {
	// return new ColorPicker();
	// }

	public static void logSeparator() {
		Document document = Document.get();
		document.getBody().appendChild(document.createHRElement());
	}

	public static void log(String msg) {
		Document document = Document.get();
		PreElement preElement = document.createPreElement();
		preElement.appendChild(document.createTextNode(msg));
		preElement.getStyle().setMargin(0, Unit.PX);
		document.getBody().appendChild(preElement);
	}

	public static void logError(String msg) {
		Document document = Document.get();
		PreElement preElement = document.createPreElement();
		preElement.getStyle().setColor("red");
		preElement.getStyle().setMargin(0, Unit.PX);
		preElement.appendChild(document.createTextNode(msg));
		document.getBody().appendChild(preElement);
	}

// @formatter:off
//	private static native JavaScriptObject foo()
//	/*-{
//	    var empty_Constructor = @com.braintribe.gwt.customization.client.api.IPicker_Empty::new(); // just to make sure the class exists with all it's methods
//	    
//	    var clazz = @com.braintribe.gwt.customization.client.api.IPicker_Empty::class;
//	    var proto = @Class::getPrototypeForClass(Ljava/lang/Class;)(clazz);
//	    
//		var colorPickerConstructor = @ColorPicker::new();
//
//	 	var method = proto.@com.braintribe.gwt.customization.client.api.IPicker::pickerType();
//		console.log("method: " + method);
//		
//		var methodName;
//		for (var name in proto) {
//			if (method === proto[name]) {
//				methodName = name;
//				break;
//			}
//		}
//
//
//		var objClazz = @Object::class;
//	    var objProto = @Class::getPrototypeForClass(Ljava/lang/Class;)(objClazz);
//	    
//		//(String packageName, String compoundClassName,  JavaScriptObject typeId, Class<? super T> superclass)
//		var subClazz = @Class::createForClass(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/Class;)("com.braintribe.foo", "DynamicPicker", null, objClazz);
//		
//		var subProto = @com.google.gwt.lang.JavaClassHierarchySetupUtil::portableObjCreate(Lcom/google/gwt/core/client/JavaScriptObject;)(objProto);
//		subProto[methodName] = function() {
//			return "overridden picker type";
//		};
//		subProto.@Object::castableTypeMap = proto.@Object::castableTypeMap;
//		
//		subProto.@Object::___clazz = subClazz;  
//		
//		// TODO override toString 
//
//		var superConstructor = @StartupEntryPoint::extractConstructor(Lcom/google/gwt/core/client/JavaScriptObject;)(@Object::new());
//
//		var result = {};
//		result.clazz = subClazz;
//		
//		result.factory = function() {
//			superConstructor.call(this);
//		}
//		result.factory.prototype = subProto;
//		 
//		
//		//function() {
//			// return @com.google.gwt.lang.JavaClassHierarchySetupUtil::portableObjCreate(Lcom/google/gwt/core/client/JavaScriptObject;)(subProto);
//		//}
//		
//		return result; 
//	}-*/;
//
//	private static native IPicker getDynamicPicker(JavaScriptObject entityType)
//	/*-{
//	 	return new entityType.factory();
//	}-*/;
//
//	private static final String RETURN_NEW = "return new";
//
//	private static JavaScriptObject extractConstructor(JavaScriptObject superConstructorFunction) {
//		String source = superConstructorFunction.toString();
//
//		/* assert source like "function() { return new ${package}.${SuperclassName};}" (potentially with round brackets
//		 * following the constructor function) */
//
//		int s = source.indexOf(RETURN_NEW);
//		int e = source.lastIndexOf(';');
//
//		String superConstructor = source.substring(s + RETURN_NEW.length(), e).trim();
//
//		return resolveObjectPath(superConstructor);
//	}
//
//	private static native JavaScriptObject resolveObjectPath(String objectPath)
//	/*-{
//	 	return new Function("return " + objectPath)();
//	}-*/;
// @formatter:on

}

package com.braintribe.gwt.customization.client.labs.types;

import com.braintribe.gwt.customization.client.tests.AbstractGwtTest;

/**
 * This is just to analyze the generated JS code, to see how Java types are mapped to JS.
 */
public class SimpleTypesOverview extends AbstractGwtTest {

	@Override
	public void tryRun() {
		PojoJoat pojo = new PojoJoat();
		pojo.primitiveBoolean = true;
		pojo.objectBoolean = true;
		pojo.primitiveInt = 1;
		pojo.objectInt = 1;
		pojo.primitiveLong = 1L;
		pojo.objectLong = 1L;
		pojo.primitiveFloat = 1.0f;
		pojo.objectFloat = 1.0f;
		pojo.primitiveDouble = 1.0;
		pojo.objectDouble = 1.0;
		pojo.string = "hello";
		
		log("Simple types set.");
	}


}

class PojoJoat {
	public boolean primitiveBoolean;
	public Boolean objectBoolean;
	public int primitiveInt;
	public Integer objectInt;
	public long primitiveLong;
	public Long objectLong;
	public float primitiveFloat;
	public Float objectFloat;
	public double primitiveDouble;
	public Double objectDouble;
	public String string;
}
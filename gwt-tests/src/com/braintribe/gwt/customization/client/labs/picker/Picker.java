package com.braintribe.gwt.customization.client.labs.picker;

import com.braintribe.gwt.customization.client.labs.picker.api.IPicker;
import com.google.gwt.user.client.Random;

/**
 * 
 * @author peter.gazdik
 */
public class Picker implements IPicker {

	protected int defaultValue; 
	protected int constructorStaticValue;
	protected int directStaticValue = 111;
	protected int blockStaticValue;

	protected int constructorDynamicValue;
	protected int directDynamicValue = Random.nextInt();
	protected int blockDynamicValue;

	protected static int staticDirectValue = 5555;
	protected static int staticBlockValue;

	protected static int staticDynamicValue = Random.nextInt();
	protected static int staticBlockDynamicValue;

	public Picker() {
		constructorStaticValue = 222;
		constructorDynamicValue = Random.nextInt();
	}

	public Picker(int constructorStaticValue) {
		this.constructorStaticValue = constructorStaticValue;
		this.constructorDynamicValue = Random.nextInt();
	}
	
	{
		blockStaticValue = 333;
		blockDynamicValue = Random.nextInt();
	}

	static {
		staticBlockValue = 6666;
		staticBlockDynamicValue = Random.nextInt();
	}

	@Override
	public String pickerType() {
		return "Picker";
	}

}

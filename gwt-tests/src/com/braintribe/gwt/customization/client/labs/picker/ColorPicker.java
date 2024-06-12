package com.braintribe.gwt.customization.client.labs.picker;

import com.google.gwt.user.client.Random;

/**
 * 
 * @author peter.gazdik
 */
public class ColorPicker extends Picker {

	protected int sub_defaultValue; 
	protected int sub_constructorStaticValue;
	protected int sub_directStaticValue = 111;
//	protected int sub_blockStaticValue;

	protected int sub_constructorDynamicValue;
	protected int sub_directDynamicValue = Random.nextInt();
//	protected int sub_blockDynamicValue;

	protected static int sub_staticDirectValue = 5555;
//	protected static int sub_staticBlockValue;

	protected static int sub_staticDynamicValue = Random.nextInt();
//	protected static int sub_staticBlockDynamicValue;

	public ColorPicker() {
		sub_constructorStaticValue = 222;
		sub_constructorDynamicValue = Random.nextInt();
	}
//
//	{
//		sub_directStaticValue = 333;
//		sub_directDynamicValue = Random.nextInt();
//	}

//	static {
//		sub_staticBlockValue = 6666;
//		sub_staticBlockDynamicValue = Random.nextInt();
//	}

	public int pickColor(String userName) {
		return userName == null ? 0 : userName.hashCode();
	}

}

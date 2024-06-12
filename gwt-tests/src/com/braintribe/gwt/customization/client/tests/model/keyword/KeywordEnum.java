package com.braintribe.gwt.customization.client.tests.model.keyword;

import com.braintribe.model.generic.base.EnumBase;
import com.braintribe.model.generic.reflection.EnumType;
import com.braintribe.model.generic.reflection.EnumTypes;

/**
 * @author peter.gazdik
 */
public enum KeywordEnum implements EnumBase {

	arguments,
	await,
	debugger,
	delete,
	eval,
	export,
	function,
	in,
	let,
	prototype,
	typeof,
	var,
	with,
	yield;

	@Override
	public EnumType type() {
		return EnumTypes.T(KeywordEnum.class);
	}

}

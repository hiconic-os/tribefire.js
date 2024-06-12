package com.braintribe.gwt.customization.client.labs.cmd;

import java.util.function.Function;

import com.braintribe.model.meta.GmType;
import com.braintribe.model.meta.override.GmCustomTypeOverride;

/**
 * @author peter.gazdik
 */
public interface GmMappers {

	Function<GmType, String> typeToSignature = gmType -> gmType.getTypeSignature();
	Function<GmCustomTypeOverride, String> typeOverrideToSignature = typeOverride -> typeOverride.addressedType().getTypeSignature();

}

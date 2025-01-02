package dev.hiconic.js.resource.model;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;
import com.braintribe.model.resource.source.ResourceSource;

/**
 * Base type for all JS-specific resource sources.
 * <p>
 * All these type hava a {@code toUrl()} method injected by hiconic.js.
 */
@Abstract
public interface JsResourceSource extends ResourceSource {

	EntityType<JsResourceSource> T = EntityTypes.T(JsResourceSource.class);

	/* hiconic.js adds the following method here, and on Resource: */
	/* String toUrl(); */

}

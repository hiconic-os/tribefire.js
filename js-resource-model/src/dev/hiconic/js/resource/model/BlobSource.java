package dev.hiconic.js.resource.model;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface BlobSource extends JsResourceSource {

	EntityType<BlobSource> T = EntityTypes.T(BlobSource.class);

	/* hiconic.js declares (gm-core-api.d.ts) the following property: */
	/* Blob blob; */

}

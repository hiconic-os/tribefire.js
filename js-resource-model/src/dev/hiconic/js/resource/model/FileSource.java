package dev.hiconic.js.resource.model;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface FileSource extends JsResourceSource {

	EntityType<FileSource> T = EntityTypes.T(FileSource.class);

	/* hiconic.js declares (gm-core-api.d.ts) the following property: */
	/* File file; */

}

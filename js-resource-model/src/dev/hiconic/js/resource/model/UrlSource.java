package dev.hiconic.js.resource.model;

import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface UrlSource extends JsResourceSource {

	EntityType<UrlSource> T = EntityTypes.T(UrlSource.class);

	String getUrl();
	void setUrl(String url);

}

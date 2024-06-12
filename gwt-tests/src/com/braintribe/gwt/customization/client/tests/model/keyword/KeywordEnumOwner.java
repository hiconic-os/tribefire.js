package com.braintribe.gwt.customization.client.tests.model.keyword;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * This exists just so that the enum can be woven.
 * 
 * @author peter.gazdik
 */
public interface KeywordEnumOwner extends GenericEntity {

	EntityType<KeywordEnumOwner> T = EntityTypes.T(KeywordEnumOwner.class);

	String keywordEnum = "keywordEnum";

	KeywordEnum getKeywordEnum();
	void setKeywordEnum(KeywordEnum keywordEnum);
}

package com.braintribe.gwt.customization.client.tests.model.initializer;

import java.util.Date;

import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface InitializedSubEntity extends InitializedEntity {

	EntityType<InitializedSubEntity> T = EntityTypes.T(InitializedSubEntity.class);

	// override value with explicit one
	@Override
	@Initializer("88")
	int getIntValue();
	@Override
	void setIntValue(int intValue);

	// override with default (0)
	@Override
	@Initializer("0L")
	long getLongValue();
	@Override
	void setLongValue(long longValue);

	// re-declared does not change the default
	@Override
	boolean getBooleanValue();
	@Override
	void setBooleanValue(boolean doubleValue);

	@Override
	@Initializer("null")
	Date getDateValue();
	@Override
	void setDateValue(Date value);

	long getNewLongValue();
	void setNewLongValue(long value);

}

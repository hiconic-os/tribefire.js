package com.braintribe.gwt.customization.client.tests.model.initializer;

import java.math.BigDecimal;
import java.util.Date;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.annotation.Initializer;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface InitializedEntity extends GenericEntity {

	EntityType<InitializedEntity> T = EntityTypes.T(InitializedEntity.class);

	// @formatter:off
	@Initializer("99")
	int getIntValue();
	void setIntValue(int value);

	@Initializer("11L")
	long getLongValue();
	void setLongValue(long value);

	@Initializer("+123f")
	float getFloatValue();
	void setFloatValue(float value);

	@Initializer("-123D")
	double getDoubleValue();
	void setDoubleValue(double value);

	@Initializer("+1.0e30f")
	float getBigFloatValue();
	void setBigFloatValue(float value);

	@Initializer("-1.0e30d")
	double getBigDoubleValue();
	void setBigDoubleValue(double value);

	@Initializer("true")
	boolean getBooleanValue();
	void setBooleanValue(boolean value);

	@Initializer("99889988.00b")
	BigDecimal getDecimalValue();
	void setDecimalValue(BigDecimal value);

	@Initializer("now()")
	Date getDateValue();
	void setDateValue(Date value);

	@Initializer("enum(com.braintribe.gwt.customization.client.tests.model.initializer.Color,green)")
	Color getEnumValue();
	void setEnumValue(Color value);

	@Initializer("green")
	Color getEnumShort();
	void setEnumShort(Color value);
	
	Date getUninitializedDateValue();
	void setUninitializedDateValue(Date value);
	
	long getUninitializedLongValue();
	void setUninitializedLongValue(long value);
	// @formatter:on

	boolean getUninitializedBooleanValue();
	void setUninitializedBooleanValue(boolean value);

}

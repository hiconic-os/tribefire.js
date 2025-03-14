// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.gwt.customization.client.tests.model.simple;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.gwt.customization.client.tests.model.initializer.Color;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

public interface PropsEntity extends GenericEntity {

	final EntityType<PropsEntity> T = EntityTypes.T(PropsEntity.class);

	boolean getPrimitiveBoolean();
	void setPrimitiveBoolean(boolean value);

	Boolean getBooleanWrapper();
	void setBooleanWrapper(Boolean value);

	int getPrimitiveInteger();
	void setPrimitiveInteger(int value);

	Integer getIntegerWrapper();
	void setIntegerWrapper(Integer value);

	long getPrimitiveLong();
	void setPrimitiveLong(long value);

	Long getLongWrapper();
	void setLongWrapper(Long value);

	double getPrimitiveDouble();
	void setPrimitiveDouble(double value);

	Double getDoubleWrapper();
	void setDoubleWrapper(Double value);

	float getPrimitiveFloat();
	void setPrimitiveFloat(float value);

	Float getFloatWrapper();
	void setFloatWrapper(Float value);

	String getString();
	void setString(String value);

	Date getDate();
	void setDate(Date date);

	BigDecimal getBigDecimal();
	void setBigDecimal(BigDecimal bigDecimal);

	// List

	List<Object> getListObject();
	void setListObject(List<Object> ListObject);

	List<String> getListString();
	void setListString(List<String> listString);

	List<Float> getListFloat();
	void setListFloat(List<Float> listFloat);

	List<Double> getListDouble();
	void setListDouble(List<Double> listDouble);

	List<Long> getListLong();
	void setListLong(List<Long> listLong);

	List<Date> getListDate();
	void setListDate(List<Date> listDate);

	// Set

	Set<String> getSetString();
	void setSetString(Set<String> setString);

	Set<Date> getSetDate();
	void setSetDate(Set<Date> setDate);

	// Map

	Map<Integer, String> getMapIntegerString();
	void setMapIntegerString(Map<Integer, String> mapIntegerString);

	Map<Float, Date> getMapFloatDate();
	void setMapFloatDate(Map<Float, Date> mapFloatDate);

	// custom

	Color getColor();
	void setColor(Color color);

	PropsEntity getOtherEntity();
	void setOtherEntity(PropsEntity otherEntity);

}

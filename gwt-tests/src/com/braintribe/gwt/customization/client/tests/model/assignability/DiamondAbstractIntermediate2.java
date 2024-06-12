package com.braintribe.gwt.customization.client.tests.model.assignability;

import com.braintribe.model.generic.annotation.Abstract;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

@Abstract
public interface DiamondAbstractIntermediate2 extends DiamondAbstractBase {

	final EntityType<DiamondAbstractIntermediate2> T = EntityTypes.T(DiamondAbstractIntermediate2.class);
}

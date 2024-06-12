package com.braintribe.gwt.customization.client.tests;

import static com.braintribe.utils.lcd.CollectionTools2.asList;

import java.util.List;

import com.braintribe.gwt.customization.client.tests.model.essentialmd.EssentialMdEntity;
import com.braintribe.gwt.customization.client.tests.model.essentialmd.EssentialMdSiblingEntity;
import com.braintribe.gwt.customization.client.tests.model.essentialmd.EssentialMdSubEntity;
import com.braintribe.model.generic.GmfException;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.meta.GmMetaModel;

/**
 * @author peter.gazdik
 */
public class EssentialMdTest extends AbstractGmGwtTest {

	private static final List<EntityType<?>> types = asList( //
			EssentialMdEntity.T, //
			EssentialMdSiblingEntity.T, //
			EssentialMdSubEntity.T //
	);

	@Override
	protected void tryRun() throws GmfException {
		GmMetaModel model = generateModel("test.gwt:essential-md-test-model", types);

		makeSignaturesDynamic(model);
		ensureModelTypes(model);

		testNonConfidential(EssentialMdEntity.class.getName(), EssentialMdEntity.regularProperty);
		testConfidential(EssentialMdEntity.class.getName(), EssentialMdEntity.confidential);
		testConfidential(EssentialMdSubEntity.class.getName(), EssentialMdEntity.confidential);
		testConfidential(EssentialMdSubEntity.class.getName(), EssentialMdSubEntity.siblingConfidential);
	}

	private void testConfidential(String typeSignature, String propertyName) {
		testConfidentialHelper(typeSignature, propertyName);
		testConfidentialHelper(makeSignatureDynamic(typeSignature), propertyName);
	}

	private void testConfidentialHelper(String typeSignature, String propertyName) {
		EntityType<?> et = typeReflection.getEntityType(typeSignature);

		Property p = et.getProperty(propertyName);

		if (p.isConfidential())
			log("    Confidential property [" + propertyName + "]: '" + typeSignature + "' [OK]");
		else
			logError("Property [" + propertyName + "] is not confidential : " + typeSignature);
	}

	private void testNonConfidential(String typeSignature, String propertyName) {
		testNonConfidentialHelper(typeSignature, propertyName);
		testNonConfidentialHelper(makeSignatureDynamic(typeSignature), propertyName);
	}

	private void testNonConfidentialHelper(String typeSignature, String propertyName) {
		EntityType<?> et = typeReflection.getEntityType(typeSignature);

		Property p = et.getProperty(propertyName);

		if (p.isConfidential())
			logError("Property [" + propertyName + "] should not be confidential: " + typeSignature);
		else
			log("    Non-confidential property [" + propertyName + "]: '" + typeSignature + "' [OK]");
	}

}

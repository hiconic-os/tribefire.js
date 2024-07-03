// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
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
package com.braintribe.model.processing.query.support;

import java.util.Arrays;
import java.util.List;

import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.CloningContext;
import com.braintribe.model.generic.reflection.CollectionType;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.GenericModelType;
import com.braintribe.model.generic.reflection.Property;
import com.braintribe.model.generic.reflection.TypeCode;
import com.braintribe.model.generic.value.PersistentEntityReference;
import com.braintribe.model.processing.query.eval.api.RuntimeQueryEvaluationException;
import com.braintribe.model.processing.query.fluent.SelectQueryBuilder;
import com.braintribe.model.query.EntityQuery;
import com.braintribe.model.query.From;
import com.braintribe.model.query.Join;
import com.braintribe.model.query.Ordering;
import com.braintribe.model.query.PropertyOperand;
import com.braintribe.model.query.PropertyQuery;
import com.braintribe.model.query.Restriction;
import com.braintribe.model.query.SelectQuery;
import com.braintribe.model.query.Source;
import com.braintribe.model.query.conditions.Condition;
import com.braintribe.model.query.conditions.Conjunction;
import com.braintribe.model.query.conditions.FulltextComparison;

/**
 * 
 */
public class QueryConverter {

	protected static SelectQuery convertEntityQuery(EntityQuery query) {
		From from = From.T.create();
		from.setEntityTypeSignature(query.getEntityTypeSignature());

		List<From> fromAsList = Arrays.asList(from);

		SelectQuery result = SelectQuery.T.create();
		result.setSelections(QueryConverter.<List<Object>> cast(fromAsList));
		result.setFroms(fromAsList);
		result.setOrdering(cloneOrdering(query.getOrdering(), from));
		result.setRestriction(cloneRestriction(query.getRestriction(), from));
		result.setTraversingCriterion(query.getTraversingCriterion());
		result.setEvaluationExcludes(query.getEvaluationExcludes());
		result.setNoAbsenceInformation(query.getNoAbsenceInformation());

		return result;
	}

	public static SelectQuery convertPropertyQuery(PropertyQuery query) {
		PersistentEntityReference ref = query.getEntityReference();
		EntityType<?> entityType = ref.valueType();

		Property property = entityType.getProperty(query.getPropertyName());

		return convertPropertyQuery(query, entityType, property);
	}

	public static SelectQuery convertPropertyQuery(PropertyQuery query, EntityType<?> entityType, Property property) {
		SelectQuery result = convertPropertyQueryHelper(query, entityType, property);

		result.setTraversingCriterion(query.getTraversingCriterion());
		result.setEvaluationExcludes(query.getEvaluationExcludes());
		result.setNoAbsenceInformation(query.getNoAbsenceInformation());

		return result;
	}

	private static SelectQuery convertPropertyQueryHelper(PropertyQuery query, EntityType<?> entityType, Property property) {
		GenericModelType propertyType = property.getType();

		// check for general query validity by property type (regarding the restriction)
		if (propertyType instanceof CollectionType) {
			CollectionType collectionType = (CollectionType) propertyType;

			switch (collectionType.getCollectionKind()) {
				case map:
					return convertMapPropertyQuery(query, entityType);
				case set:
					return convertJoinablePropertyQuery(query, entityType, TypeCode.setType);
				case list:
					return convertJoinablePropertyQuery(query, entityType, TypeCode.listType);
				default:
					throw new RuntimeQueryEvaluationException("Unknown collection of kind: " + collectionType.getCollectionKind());
			}
		}

		if (propertyType.isEntity())
			return convertJoinablePropertyQuery(query, entityType, TypeCode.entityType);

		if (query.getRestriction() != null)
			throw new RuntimeQueryEvaluationException("Restriction is not allowed for property type " + property.getType().getTypeSignature());

		return convertStandardPropertyQuery(query, entityType, null);
	}

	private static SelectQuery convertMapPropertyQuery(PropertyQuery query, EntityType<?> et) {
		String propertyName = query.getPropertyName();

		SelectQueryBuilder builder = new SelectQueryBuilder().from(et, "f");

		builder = builder.join("f", propertyName, "map").select().mapKey("map").select("map");

		builder = applyOwnerRestriction(builder, query.getEntityReference());

		// TODO we are ignoring original conditions here. Should they be applied?

		return builder.done();
	}

	private static SelectQuery convertJoinablePropertyQuery(PropertyQuery query, EntityType<?> et, TypeCode typeCode) {
		SelectQuery selectQuery = convertStandardPropertyQuery(query, et, typeCode);

		From from = selectQuery.getFroms().get(0);
		Join defaultSource = from.getJoins().iterator().next();

		Restriction propertyRestriction = cloneRestriction(query.getRestriction(), defaultSource);
		Restriction mergedRestrction = merge(selectQuery.getRestriction(), propertyRestriction);

		selectQuery.setRestriction(mergedRestrction);
		if (typeCode == TypeCode.setType)
			selectQuery.setOrdering(cloneOrdering(query.getOrdering(), defaultSource));

		return selectQuery;
	}

	private static SelectQuery convertStandardPropertyQuery(PropertyQuery query, EntityType<?> et, TypeCode typeCode) {
		PersistentEntityReference ref = query.getEntityReference();
		String propertyName = query.getPropertyName();

		SelectQueryBuilder builder = new SelectQueryBuilder().from(et, "f");

		if (typeCode != null)
			builder = builder.join("f", propertyName, "p").select("p");
		else
			builder = builder.select("f", propertyName);

		builder = applyOwnerRestriction(builder, ref);

		if (typeCode == TypeCode.listType)
			builder.orderBy().listIndex("p");

		return builder.done();
	}

	private static SelectQueryBuilder applyOwnerRestriction(SelectQueryBuilder builder, PersistentEntityReference ref) {
		return builder.where().entity("f").eq().entityReference(ref);
	}

	// ################################################
	// ## . . . Cloning restriction and ordering . . ##
	// ################################################

	private static Restriction cloneRestriction(Restriction restriction, Source defaultSource) {
		if (restriction == null)
			return null;

		Condition condition = restriction.getCondition();

		if (condition == null)
			return restriction;

		return cloneWithDefaultFrom(restriction, defaultSource);
	}

	private static Ordering cloneOrdering(Ordering ordering, Source defaultSource) {
		return ordering == null ? null : cloneWithDefaultFrom(ordering, defaultSource);
	}

	private static <T extends GenericEntity> T cloneWithDefaultFrom(T t, Source defaultSource) {
		return cast(t.entityType().clone(cloningContext(defaultSource), t, null));
	}

	/**
	 * Creates new {@link CloningContext} that puts the default source to all {@link PropertyOperand}s where the source is <tt>null</tt>.
	 */
	private static CloningContext cloningContext(final Source defaultSource) {
		return new NullPropertyIgnoringCloningContext() {
			@SuppressWarnings("unusable-by-js")
			@Override
			public GenericEntity supplyRawClone(EntityType<? extends GenericEntity> entityType, GenericEntity instanceToBeCloned) {
				if (instanceToBeCloned instanceof PropertyOperand) {
					PropertyOperand po = (PropertyOperand) instanceToBeCloned;
					if (po.getSource() == null) {
						po = PropertyOperand.T.create();
						po.setSource(defaultSource);
						return po;
					}

				} else if (instanceToBeCloned instanceof FulltextComparison) {
					FulltextComparison fc = (FulltextComparison) instanceToBeCloned;
					if (fc.getSource() == null) {
						fc = FulltextComparison.T.create();
						fc.setSource(defaultSource);
						return fc;
					}
				}

				return super.supplyRawClone(entityType, instanceToBeCloned);
			}
		};
	}

	// ################################################
	// ## . . . . . . Merging conditions . . . . . . ##
	// ################################################

	private static Restriction merge(Restriction r1, Restriction r2) {
		if (r2 == null)
			return r1;

		r1.setPaging(r2.getPaging());

		Condition condition = r2.getCondition();
		if (condition != null)
			r1.setCondition(conjunction(r1.getCondition(), condition));

		return r1;
	}

	private static Condition conjunction(Condition c1, Condition c2) {
		Conjunction result = Conjunction.T.create();
		result.setOperands(Arrays.asList(c1, c2));

		return result;
	}

	private static <T> T cast(Object o) {
		return (T) o;
	}

}

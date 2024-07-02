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
package com.braintribe.gwt.customization.client.tests.model.keyword;

import com.braintribe.gwt.customization.client.tests.model.keyword.with.non_dynamic.KeywordPackageEntity;
import com.braintribe.model.generic.GenericEntity;
import com.braintribe.model.generic.reflection.EntityType;
import com.braintribe.model.generic.reflection.EntityTypes;

/**
 * @author peter.gazdik
 */
public interface KeywordEntity extends GenericEntity {

	EntityType<KeywordEntity> T = EntityTypes.T(KeywordEntity.class);

	KeywordPackageEntity getKeywordPackage();
	void setKeywordPackage(KeywordPackageEntity keywordPackage);

	String getArguments();
	void setArguments(String value);

	String getAwait();
	void setAwait(String value);

	String getBreak();
	void setBreak(String value);

	String getCase();
	void setCase(String value);

	String getCatch();
	void setCatch(String value);

	String getConst();
	void setConst(String value);

	String getContinue();
	void setContinue(String value);

	String getDebugger();
	void setDebugger(String value);

	String getDefault();
	void setDefault(String value);

	String getDelete();
	void setDelete(String value);

	String getDo();
	void setDo(String value);

	String getElse();
	void setElse(String value);

	String getEnum();
	void setEnum(String value);

	String getEval();
	void setEval(String value);

	String getExport();
	void setExport(String value);

	String getExtends();
	void setExtends(String value);

	String getFalse();
	void setFalse(String value);

	String getFinally();
	void setFinally(String value);

	String getFor();
	void setFor(String value);

	String getFunction();
	void setFunction(String value);

	String getIf();
	void setIf(String value);

	String getImplements();
	void setImplements(String value);

	String getImport();
	void setImport(String value);

	String getIn();
	void setIn(String value);

	String getInstanceof();
	void setInstanceof(String value);

	String getInterface();
	void setInterface(String value);

	String getLet();
	void setLet(String value);

	String getNew();
	void setNew(String value);

	String getNull();
	void setNull(String value);

	String getPackage();
	void setPackage(String value);

	String getPrivate();
	void setPrivate(String value);

	String getProtected();
	void setProtected(String value);

	String getPrototype();
	void setPrototype(String value);

	String getPublic();
	void setPublic(String value);

	String getReturn();
	void setReturn(String value);

	String getStatic();
	void setStatic(String value);

	String getSuper();
	void setSuper(String value);

	String getSwitch();
	void setSwitch(String value);

	String getThis();
	void setThis(String value);

	String getThrow();
	void setThrow(String value);

	String getTrue();
	void setTrue(String value);

	String getTry();
	void setTry(String value);

	String getTypeof();
	void setTypeof(String value);

	String getVar();
	void setVar(String value);

	String getVoid();
	void setVoid(String value);

	String getWhile();
	void setWhile(String value);

	String getWith();
	void setWith(String value);

	String getYield();
	void setYield(String value);

	String getYield_();
	void setYield_(String value);
}

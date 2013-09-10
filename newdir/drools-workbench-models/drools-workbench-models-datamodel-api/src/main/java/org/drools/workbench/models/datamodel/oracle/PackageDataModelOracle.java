/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.models.datamodel.oracle;

import java.util.List;

import org.drools.workbench.models.commons.shared.imports.Imports;
import org.drools.workbench.models.commons.shared.rule.DSLSentence;
import org.drools.workbench.models.datamodel.model.MethodInfo;

public interface PackageDataModelOracle extends ProjectDataModelOracle {

    // Global Variable related methods
    String[] getGlobalVariables();

    boolean isGlobalVariable( final String variable );

    String[] getFieldCompletionsForGlobalVariable( final String variable );

    String getGlobalVariable( final String variable );

    List<MethodInfo> getMethodInfosForGlobalVariable( final String variable );

    String[] getGlobalCollections();

    // DSL related methods
    List<DSLSentence> getDSLActions();

    List<DSLSentence> getDSLConditions();

    //Import related methods
    String[] getAllFactTypes();

    String[] getExternalFactTypes();

    void filter( final Imports imports );

    void filter();

}

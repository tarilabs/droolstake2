/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.kie.dmn.core;

import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.dmn.core.api.*;
import org.kie.dmn.core.ast.InputDataNode;
import org.kie.dmn.core.ast.ItemDefNode;
import org.kie.dmn.core.impl.FeelTypeImpl;
import org.kie.dmn.feel.lang.types.BuiltInType;

import java.math.BigDecimal;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DMNCompilerTest {

    protected DMNRuntime createRuntime( String resourceName ) {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = KieHelper.getKieContainer(
                ks.newReleaseId( "org.kie", "dmn-test", "1.0" ),
                ks.getResources().newClassPathResource( resourceName, DMNCompilerTest.class ) );

        // the method getKieRuntime() needs to be moved to the public API
        DMNRuntime runtime = ((StatefulKnowledgeSessionImpl) kieContainer.newKieSession()).getKieRuntime( DMNRuntime.class );
        assertNotNull( runtime );
        return runtime;
    }

    @Test
    public void testItemDefAllowedValuesString() {
        DMNRuntime runtime = createRuntime( "0003-input-data-string-allowed-values.dmn" );
        DMNModel dmnModel = runtime.getModel( "https://github.com/droolsjbpm/kie-dmn", "0003-input-data-string-allowed-values" );
        assertThat( dmnModel, notNullValue() );

        ItemDefNode itemDef = dmnModel.getItemDefinitionByName( "tEmploymentStatus" );

        assertThat( itemDef.getName(), is( "tEmploymentStatus" ) );
        assertThat( itemDef.getId(), is( nullValue() ) );

        DMNType type = itemDef.getType();

        assertThat( type, is( notNullValue() ) );
        assertThat( type.getName(), is( "tEmploymentStatus" ) );
        assertThat( type.getId(), is( nullValue() ) );
        assertThat( type, is( instanceOf( FeelTypeImpl.class ) ) );

        FeelTypeImpl feelType = (FeelTypeImpl) type;

        assertThat( feelType.getFeelType(), is( BuiltInType.STRING ) );
        assertThat( feelType.getAllowedValues().size(), is( 4 ) );
        assertThat( feelType.getAllowedValues().get( 0 ), is( "UNEMPLOYED" ) );
        assertThat( feelType.getAllowedValues().get( 1 ), is( "EMPLOYED" ) );
        assertThat( feelType.getAllowedValues().get( 2 ), is( "SELF-EMPLOYED" ) );
        assertThat( feelType.getAllowedValues().get( 3 ), is( "STUDENT" ) );

    }


}

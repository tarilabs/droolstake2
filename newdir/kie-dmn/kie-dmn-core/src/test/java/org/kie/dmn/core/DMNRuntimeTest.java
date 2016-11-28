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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.dmn.core.api.DMNContext;
import org.kie.dmn.core.api.DMNFactory;
import org.kie.dmn.core.api.DMNMessage;
import org.kie.dmn.core.api.DMNModel;
import org.kie.dmn.core.api.DMNResult;
import org.kie.dmn.core.api.DMNRuntime;
import org.kie.dmn.core.api.event.AfterEvaluateDecisionEvent;
import org.kie.dmn.core.api.event.AfterEvaluateDecisionTableEvent;
import org.kie.dmn.core.api.event.BeforeEvaluateDecisionEvent;
import org.kie.dmn.core.api.event.BeforeEvaluateDecisionTableEvent;
import org.kie.dmn.core.api.event.DMNRuntimeEventListener;
import org.kie.dmn.core.util.DMNRuntimeUtil;
import org.mockito.ArgumentCaptor;

public class DMNRuntimeTest {

    @Test
    public void testCompositeItemDefinition() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "0008-LX-arithmetic.dmn", this.getClass() );
        DMNModel dmnModel = runtime.getModel( "https://github.com/droolsjbpm/kie-dmn", "0008-LX-arithmetic" );
        assertThat( dmnModel, notNullValue() );

        DMNContext context = DMNFactory.newContext();
        Map loan = new HashMap(  );
        loan.put( "principal", 600000 );
        loan.put( "rate", 0.0375 );
        loan.put( "termMonths", 360 );
        context.set( "loan", loan );

        DMNResult dmnResult = runtime.evaluateAll( dmnModel, context );

        DMNContext result = dmnResult.getContext();

        assertThat( result.get( "payment" ), is( new BigDecimal( "2778.693549432766720839844710324306" ) ) );
    }

    @Test
    public void testTrisotechNamespace() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "trisotech_namespace.dmn", this.getClass() );
        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/dmn/definitions/_b8feec86-dadf-4051-9feb-8e6093bbb530", "Solution 3" );
        assertThat( dmnModel, notNullValue() );

        DMNContext context = DMNFactory.newContext();
        context.set( "IsDoubleHulled", true );
        context.set("Residual Cargo Size", new BigDecimal( 0.1 ) );
        context.set("Ship Size", new BigDecimal( 50 ) );
        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        DMNContext result = dmnResult.getContext();
        assertThat( result.get("Ship can enter a Dutch port"), is( true ) );
    }

    @Test
    public void testEmptyDecision1() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "empty_decision.dmn", this.getClass() );
        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/dmn/definitions/_ba9fc4b1-5ced-4d00-9b61-290de4bf3213", "Solution 3" );
        assertThat( dmnModel, notNullValue() );

        DMNContext context = DMNFactory.newContext();
        Map shipInfo = new HashMap(  );
        shipInfo.put( "Size", BigDecimal.valueOf( 70 ) );
        shipInfo.put( "Is Double Hulled", Boolean.FALSE );
        shipInfo.put( "Residual Cargo Size", BigDecimal.valueOf( 0.1 ) );
        context.set( "Ship Info", shipInfo );

        // Test that even if one decision is empty or missing input data,
        // the other decisions in the model are still evaluated
        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        DMNContext result = dmnResult.getContext();
        assertThat( dmnResult.hasErrors(), is( true ) );
        assertThat( result.get("Ship Can Enter v2"), is( true ) );
    }

    @Test
    public void testEmptyDecision2() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "empty_decision.dmn", this.getClass() );
        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/dmn/definitions/_ba9fc4b1-5ced-4d00-9b61-290de4bf3213", "Solution 3" );
        assertThat( dmnModel, notNullValue() );

        DMNContext context = DMNFactory.newContext();
        Map shipInfo = new HashMap(  );
        shipInfo.put( "Size", BigDecimal.valueOf( 70 ) );
        shipInfo.put( "Is Double Hulled", Boolean.FALSE );
        shipInfo.put( "Residual Cargo Size", BigDecimal.valueOf( 0.1 ) );
        context.set( "Ship Info", shipInfo );
        context.set( "Ship Size", BigDecimal.valueOf( 70 ) );
        context.set( "IsDoubleHulled", Boolean.FALSE );
        context.set( "Residual Cargo Size", BigDecimal.valueOf( 0.1 ) );

        // check that if all the input data is available, but the
        // decision expression is empty, the model returns a warning
        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);

        List<DMNMessage> messages = dmnResult.getMessages( DMNMessage.Severity.WARN );
        assertThat( messages.size(), is( 1 ) );
        assertThat( messages.get( 0 ).getSeverity(), is( DMNMessage.Severity.WARN ) );
        assertThat( messages.get( 0 ).getSourceId(), is( "_42806504-8ed5-488f-b274-de98c1bc67b9" ) );

        DMNContext result = dmnResult.getContext();
        assertThat( result.get("Ship Can Enter v2"), is( true ) );
    }

    @Test
    public void testEventListeners() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "car_damage_responsibility.dmn", this.getClass() );

        DMNRuntimeEventListener listener = mock( DMNRuntimeEventListener.class );
        runtime.addListener( listener );
        runtime.addListener( DMNRuntimeUtil.createListener() );

        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/definitions/_820611e9-c21c-47cd-8e52-5cba2be9f9cc", "Car Damage Responsibility" );
        assertThat( dmnModel, notNullValue() );

        DMNContext context = DMNFactory.newContext();
        context.set( "Membership Level", "Silver" );
        context.set( "Damage Types", "Body" );
        context.set( "Responsible", "Driver" );

        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);

        ArgumentCaptor<AfterEvaluateDecisionTableEvent> argument = ArgumentCaptor.forClass( AfterEvaluateDecisionTableEvent.class);
        verify( listener, times(2) )
                .beforeEvaluateDecision( any( BeforeEvaluateDecisionEvent.class ) );
        verify( listener, times(2) )
                .afterEvaluateDecision( any( AfterEvaluateDecisionEvent.class ) );
        verify( listener, times(2) )
                .beforeEvaluateDecisionTable( any( BeforeEvaluateDecisionTableEvent.class ) );
        verify( listener, times(2) )
                .afterEvaluateDecisionTable( argument.capture() );

        AfterEvaluateDecisionTableEvent dte = argument.getAllValues().get( 0 );
        assertThat( dte.getDecisionTableName(), is("Car Damage Responsibility") );
        assertThat( dte.getMatches(), is( Arrays.asList( 4 )) );

        dte = argument.getAllValues().get( 1 );
        assertThat( dte.getDecisionTableName(), is("Payment method") );
        assertThat( dte.getMatches(), is( Arrays.asList( 2 )) );

        assertThat( dmnResult.hasErrors(), is( false ) );

        DMNContext result = dmnResult.getContext();
        assertThat( (Map<String,Object>)result.get("Car Damage Responsibility"), hasEntry( is( "EU Rent" ), is( BigDecimal.valueOf( 40 )) ));
        assertThat( (Map<String,Object>)result.get("Car Damage Responsibility"), hasEntry( is( "Renter" ), is( BigDecimal.valueOf( 60 )) ));
        assertThat( result.get("Payment method"), is( "Check" ) );
    }

    @Test
    public void testOutputReuse() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "Input_reuse_in_output.dmn", this.getClass() );
        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/dmn/definitions/_098bb607-eff7-4772-83ac-6ded8b371fa7", "Input reuse in output" );
        assertThat( dmnModel, notNullValue() );

        DMNContext context = DMNFactory.newContext();
        context.set( "Age", 40 );
        context.set( "Requested Product", "Fixed30" );

        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        System.out.println(dmnResult.getMessages());
        assertThat( dmnResult.hasErrors(), is( false ) );

        DMNContext result = dmnResult.getContext();
        assertThat( result.get("My Decision"), is( "Fixed30" ) );
    }

    @Test
    public void testSimpleNot() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "Simple_Not.dmn", this.getClass() );
        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/definitions/_98436ebb-7c42-48c0-8d11-d693e2a817c9", "Simple Not" );
        assertThat( dmnModel, notNullValue() );

        DMNContext context = DMNFactory.newContext();
        context.set( "Occupation", "Student" );

        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        System.out.println(dmnResult.getMessages());
        assertThat( dmnResult.hasErrors(), is( false ) );

        DMNContext result = dmnResult.getContext();
        assertThat( result.get("a"), is( "Is Student" ) );
    }

    @Test
    public void testSimpleNot2() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "Simple_Not.dmn", this.getClass() );
        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/definitions/_98436ebb-7c42-48c0-8d11-d693e2a817c9", "Simple Not" );
        assertThat( dmnModel, notNullValue() );

        DMNContext context = DMNFactory.newContext();
        context.set( "Occupation", "Engineer" );

        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        System.out.println(dmnResult.getMessages());
        assertThat( dmnResult.hasErrors(), is( false ) );

        DMNContext result = dmnResult.getContext();
        assertThat( result.get("a"), is( "Is not a Student" ) );
    }

    @Test
    public void testDinner() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "Dinner.dmn", this.getClass() );
        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/definitions/_0c45df24-0d57-4acc-b296-b4cba8b71a36", "Dinner" );
        assertThat( dmnModel, notNullValue() );
        assertThat( dmnModel.hasErrors(), is(false) );

        DMNContext context = DMNFactory.newContext();
        context.set( "Guests with children", true );
        context.set( "Season", "Fall" );
        context.set( "Number of guests", 4 );
        context.set( "Temp", 25 );
        context.set( "Rain Probability", 30 );

        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        assertThat( dmnResult.hasErrors(), is( false ) );
        assertThat( dmnResult.getContext().get("Where to eat"), is( "Outside" ) );
        assertThat( dmnResult.getContext().get("Dish"), is( "Spareribs" ) );
        assertThat( dmnResult.getContext().get("Drinks"), is( Arrays.asList( "Apero", "Ale", "Juice Boxes" ) ) );
    }

    @Test @Ignore("not implemented yet")
    public void testBoxedContext() {
        DMNRuntime runtime = DMNRuntimeUtil.createRuntime( "BoxedContext.dmn", this.getClass() );
        DMNModel dmnModel = runtime.getModel( "http://www.trisotech.com/dmn/definitions/_0de36357-fec0-4b4e-b7f1-382d381e06e9", "Dessin 1" );
        assertThat( dmnModel, notNullValue() );
        assertThat( dmnModel.getMessages().toString(), dmnModel.hasErrors(), is(false) );

        DMNContext context = DMNFactory.newContext();
        context.set( "a", 10 );
        context.set( "b", 5 );

        DMNResult dmnResult = runtime.evaluateAll(dmnModel, context);
        assertThat( dmnResult.hasErrors(), is( false ) );
        assertThat( (Map<String, Object>) dmnResult.getContext().get( "Math" ), hasEntry( "Sum", BigDecimal.valueOf( 15 ) ) );
        assertThat( (Map<String, Object>) dmnResult.getContext().get( "Math" ), hasEntry( "Product", BigDecimal.valueOf( 50 ) ) );
    }
}

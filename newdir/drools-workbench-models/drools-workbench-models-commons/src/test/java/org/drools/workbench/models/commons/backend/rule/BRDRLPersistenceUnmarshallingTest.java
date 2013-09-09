/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.workbench.models.commons.backend.rule;

import org.drools.workbench.models.commons.shared.oracle.DataType;
import org.drools.workbench.models.commons.shared.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.commons.shared.rule.CEPWindow;
import org.drools.workbench.models.commons.shared.rule.CompositeFactPattern;
import org.drools.workbench.models.commons.shared.rule.CompositeFieldConstraint;
import org.drools.workbench.models.commons.shared.rule.ExpressionField;
import org.drools.workbench.models.commons.shared.rule.ExpressionFormLine;
import org.drools.workbench.models.commons.shared.rule.ExpressionUnboundFact;
import org.drools.workbench.models.commons.shared.rule.ExpressionVariable;
import org.drools.workbench.models.commons.shared.rule.FactPattern;
import org.drools.workbench.models.commons.shared.rule.FreeFormLine;
import org.drools.workbench.models.commons.shared.rule.IPattern;
import org.drools.workbench.models.commons.shared.rule.RuleModel;
import org.drools.workbench.models.commons.shared.rule.SingleFieldConstraint;
import org.drools.workbench.models.commons.shared.rule.SingleFieldConstraintEBLeftSide;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.*;

public class BRDRLPersistenceUnmarshallingTest {

    @Test
    public void testFactPattern() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Applicant()\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "Applicant",
                      fp.getFactType() );

        assertEquals( 0,
                      fp.getNumberOfConstraints() );
    }

    @Test
    public void testFactPatternWithBinding() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "$a : Applicant()\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "Applicant",
                      fp.getFactType() );
        assertEquals( "$a",
                      fp.getBoundName() );

        assertEquals( 0,
                      fp.getNumberOfConstraints() );
    }

    @Test
    public void testSingleFieldConstraint() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Applicant( age < 55 )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "Applicant",
                      fp.getFactType() );

        assertEquals( 1,
                      fp.getConstraintList().getConstraints().length );
        assertTrue( fp.getConstraint( 0 ) instanceof SingleFieldConstraint );

        SingleFieldConstraint sfp = (SingleFieldConstraint) fp.getConstraint( 0 );
        assertEquals( "Applicant",
                      sfp.getFactType() );
        assertEquals( "age",
                      sfp.getFieldName() );
        assertEquals( "<",
                      sfp.getOperator() );
        assertEquals( "55",
                      sfp.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      sfp.getConstraintValueType() );
    }

    @Test
    public void testSingleFieldConstraintWithBinding() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Applicant( $a : age < 55 )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "Applicant",
                      fp.getFactType() );

        assertEquals( 1,
                      fp.getConstraintList().getConstraints().length );
        assertTrue( fp.getConstraint( 0 ) instanceof SingleFieldConstraint );

        SingleFieldConstraint sfp = (SingleFieldConstraint) fp.getConstraint( 0 );
        assertEquals( "Applicant",
                      sfp.getFactType() );
        assertEquals( "age",
                      sfp.getFieldName() );
        assertEquals( "<",
                      sfp.getOperator() );
        assertEquals( "55",
                      sfp.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      sfp.getConstraintValueType() );
        assertEquals( "$a",
                      sfp.getFieldBinding() );
    }

    @Test
    public void testCompositeFieldConstraint() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Applicant( age < 55 || age > 70 )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "Applicant",
                      fp.getFactType() );

        assertEquals( 1,
                      fp.getConstraintList().getConstraints().length );
        assertTrue( fp.getConstraint( 0 ) instanceof CompositeFieldConstraint );

        CompositeFieldConstraint cfp = (CompositeFieldConstraint) fp.getConstraint( 0 );
        assertEquals( "||",
                      cfp.getCompositeJunctionType() );
        assertEquals( 2,
                      cfp.getNumberOfConstraints() );
        assertTrue( cfp.getConstraint( 0 ) instanceof SingleFieldConstraint );
        assertTrue( cfp.getConstraint( 1 ) instanceof SingleFieldConstraint );

        SingleFieldConstraint sfp1 = (SingleFieldConstraint) cfp.getConstraint( 0 );
        assertEquals( "Applicant",
                      sfp1.getFactType() );
        assertEquals( "age",
                      sfp1.getFieldName() );
        assertEquals( "<",
                      sfp1.getOperator() );
        assertEquals( "55",
                      sfp1.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      sfp1.getConstraintValueType() );

        SingleFieldConstraint sfp2 = (SingleFieldConstraint) cfp.getConstraint( 1 );
        assertEquals( "Applicant",
                      sfp2.getFactType() );
        assertEquals( "age",
                      sfp2.getFieldName() );
        assertEquals( ">",
                      sfp2.getOperator() );
        assertEquals( "70",
                      sfp2.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      sfp2.getConstraintValueType() );
    }

    @Test
    public void testSingleFieldConstraintIsNullOperator() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Applicant( age == null )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "Applicant",
                      fp.getFactType() );

        assertEquals( 1,
                      fp.getConstraintList().getConstraints().length );
        assertTrue( fp.getConstraint( 0 ) instanceof SingleFieldConstraint );

        SingleFieldConstraint sfp = (SingleFieldConstraint) fp.getConstraint( 0 );
        assertEquals( "Applicant",
                      sfp.getFactType() );
        assertEquals( "age",
                      sfp.getFieldName() );
        assertEquals( "== null",
                      sfp.getOperator() );
        assertNull( sfp.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_UNDEFINED,
                      sfp.getConstraintValueType() );
    }

    @Test
    public void testSingleFieldConstraintIsNotNullOperator() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Applicant( age != null )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "Applicant",
                      fp.getFactType() );

        assertEquals( 1,
                      fp.getConstraintList().getConstraints().length );
        assertTrue( fp.getConstraint( 0 ) instanceof SingleFieldConstraint );

        SingleFieldConstraint sfp = (SingleFieldConstraint) fp.getConstraint( 0 );
        assertEquals( "Applicant",
                      sfp.getFactType() );
        assertEquals( "age",
                      sfp.getFieldName() );
        assertEquals( "!= null",
                      sfp.getOperator() );
        assertNull( sfp.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_UNDEFINED,
                      sfp.getConstraintValueType() );
    }

    @Test
    public void testCompositeFieldConstraintWithNotNullOperator() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Applicant( age != null && age > 70 )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "Applicant",
                      fp.getFactType() );

        assertEquals( 1,
                      fp.getConstraintList().getConstraints().length );
        assertTrue( fp.getConstraint( 0 ) instanceof CompositeFieldConstraint );

        CompositeFieldConstraint cfp = (CompositeFieldConstraint) fp.getConstraint( 0 );
        assertEquals( "&&",
                      cfp.getCompositeJunctionType() );
        assertEquals( 2,
                      cfp.getNumberOfConstraints() );
        assertTrue( cfp.getConstraint( 0 ) instanceof SingleFieldConstraint );
        assertTrue( cfp.getConstraint( 1 ) instanceof SingleFieldConstraint );

        SingleFieldConstraint sfp1 = (SingleFieldConstraint) cfp.getConstraint( 0 );
        assertEquals( "Applicant",
                      sfp1.getFactType() );
        assertEquals( "age",
                      sfp1.getFieldName() );
        assertEquals( "!= null",
                      sfp1.getOperator() );
        assertNull( sfp1.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_UNDEFINED,
                      sfp1.getConstraintValueType() );

        SingleFieldConstraint sfp2 = (SingleFieldConstraint) cfp.getConstraint( 1 );
        assertEquals( "Applicant",
                      sfp2.getFactType() );
        assertEquals( "age",
                      sfp2.getFieldName() );
        assertEquals( ">",
                      sfp2.getOperator() );
        assertEquals( "70",
                      sfp2.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      sfp2.getConstraintValueType() );
    }

    @Test
    public void testSingleFieldConstraintCEPOperator() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Applicant( dob after \"26-Jun-2013\" )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "Applicant",
                      fp.getFactType() );

        assertEquals( 1,
                      fp.getNumberOfConstraints() );
        assertTrue( fp.getConstraint( 0 ) instanceof SingleFieldConstraint );

        SingleFieldConstraint sfp = (SingleFieldConstraint) fp.getConstraint( 0 );
        assertEquals( "Applicant",
                      sfp.getFactType() );
        assertEquals( "dob",
                      sfp.getFieldName() );
        assertEquals( "after",
                      sfp.getOperator() );
        assertEquals( "26-Jun-2013",
                      sfp.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      sfp.getConstraintValueType() );
    }

    @Test
    public void testSingleFieldConstraintCEPOperator1Parameter() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "$e : Event()\n"
                + "Event( this after[1d] $e )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 2,
                      m.lhs.length );

        IPattern p1 = m.lhs[ 0 ];
        assertTrue( p1 instanceof FactPattern );
        FactPattern fp1 = (FactPattern) p1;
        assertEquals( "Event",
                      fp1.getFactType() );
        assertEquals( "$e",
                      fp1.getBoundName() );
        assertEquals( 0,
                      fp1.getNumberOfConstraints() );

        IPattern p2 = m.lhs[ 1 ];
        assertTrue( p2 instanceof FactPattern );
        FactPattern fp2 = (FactPattern) p2;
        assertEquals( "Event",
                      fp2.getFactType() );
        assertNull( fp2.getBoundName() );
        assertEquals( 1,
                      fp2.getNumberOfConstraints() );
        SingleFieldConstraint sfp = (SingleFieldConstraint) fp2.getConstraint( 0 );
        assertEquals( "Event",
                      sfp.getFactType() );
        assertEquals( "this",
                      sfp.getFieldName() );
        assertEquals( "after",
                      sfp.getOperator() );
        assertEquals( "$e",
                      sfp.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_VARIABLE,
                      sfp.getConstraintValueType() );
        assertEquals( 3,
                      sfp.getParameters().size() );
        assertEquals( "1d",
                      sfp.getParameter( "0" ) );
        assertEquals( "1",
                      sfp.getParameter( "org.kie.guvnor.guided.editor.visibleParameterSet" ) );
        assertEquals( "org.drools.workbench.models.commons.backend.rule.CEPOperatorParameterDRLBuilder",
                      sfp.getParameter( "org.kie.guvnor.guided.server.util.BRDRLPersistence.operatorParameterGenerator" ) );
    }

    @Test
    public void testReciprocal_SingleFieldConstraintCEPOperator1Parameter() {
        //This is the inverse of "SingleFieldConstraintCEPOperator1Parameter"
        String drl = "rule \"rule1\"\n"
                + "dialect \"mvel\"\n"
                + "when\n"
                + "$e : Event()\n"
                + "Event( this after[1d] $e )\n"
                + "then\n"
                + "end";

        RuleModel m = new RuleModel();
        m.name = "rule1";

        FactPattern fp1 = new FactPattern();
        fp1.setFactType( "Event" );
        fp1.setBoundName( "$e" );

        FactPattern fp2 = new FactPattern();
        fp2.setFactType( "Event" );

        SingleFieldConstraint sfp = new SingleFieldConstraint();
        sfp.setFactType( "Event" );
        sfp.setFieldName( "this" );
        sfp.setOperator( "after" );
        sfp.setValue( "$e" );
        sfp.setConstraintValueType( BaseSingleFieldConstraint.TYPE_VARIABLE );
        sfp.getParameters().put( "0",
                                 "1d" );
        sfp.getParameters().put( "org.kie.guvnor.guided.editor.visibleParameterSet",
                                 "1" );
        sfp.getParameters().put( "org.kie.guvnor.guided.server.util.BRDRLPersistence.operatorParameterGenerator",
                                 "org.drools.workbench.models.commons.backend.rule.CEPOperatorParameterDRLBuilder" );

        fp2.addConstraint( sfp );
        m.addLhsItem( fp1 );
        m.addLhsItem( fp2 );

        String actualDrl = BRDRLPersistence.getInstance().marshal( m );
        assertEqualsIgnoreWhitespace( drl,
                                      actualDrl );
    }

    @Test
    public void testSingleFieldConstraintCEPOperator2Parameters() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "$e : Event()\n"
                + "Event( this after[1d, 2d] $e )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 2,
                      m.lhs.length );

        IPattern p1 = m.lhs[ 0 ];
        assertTrue( p1 instanceof FactPattern );
        FactPattern fp1 = (FactPattern) p1;
        assertEquals( "Event",
                      fp1.getFactType() );
        assertEquals( "$e",
                      fp1.getBoundName() );
        assertEquals( 0,
                      fp1.getNumberOfConstraints() );

        IPattern p2 = m.lhs[ 1 ];
        assertTrue( p2 instanceof FactPattern );
        FactPattern fp2 = (FactPattern) p2;
        assertEquals( "Event",
                      fp2.getFactType() );
        assertNull( fp2.getBoundName() );
        assertEquals( 1,
                      fp2.getNumberOfConstraints() );
        SingleFieldConstraint sfp = (SingleFieldConstraint) fp2.getConstraint( 0 );
        assertEquals( "Event",
                      sfp.getFactType() );
        assertEquals( "this",
                      sfp.getFieldName() );
        assertEquals( "after",
                      sfp.getOperator() );
        assertEquals( "$e",
                      sfp.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_VARIABLE,
                      sfp.getConstraintValueType() );
        assertEquals( 4,
                      sfp.getParameters().size() );
        assertEquals( "1d",
                      sfp.getParameter( "0" ) );
        assertEquals( "2d",
                      sfp.getParameter( "1" ) );
        assertEquals( "2",
                      sfp.getParameter( "org.kie.guvnor.guided.editor.visibleParameterSet" ) );
        assertEquals( "org.drools.workbench.models.commons.backend.rule.CEPOperatorParameterDRLBuilder",
                      sfp.getParameter( "org.kie.guvnor.guided.server.util.BRDRLPersistence.operatorParameterGenerator" ) );
    }

    @Test
    public void testReciprocal_SingleFieldConstraintCEPOperator2Parameters() {
        //This is the inverse of "SingleFieldConstraintCEPOperator2Parameters"
        String drl = "rule \"rule1\"\n"
                + "dialect \"mvel\"\n"
                + "when\n"
                + "$e : Event()\n"
                + "Event( this after[1d, 2d] $e )\n"
                + "then\n"
                + "end";

        RuleModel m = new RuleModel();
        m.name = "rule1";

        FactPattern fp1 = new FactPattern();
        fp1.setFactType( "Event" );
        fp1.setBoundName( "$e" );

        FactPattern fp2 = new FactPattern();
        fp2.setFactType( "Event" );

        SingleFieldConstraint sfp = new SingleFieldConstraint();
        sfp.setFactType( "Event" );
        sfp.setFieldName( "this" );
        sfp.setOperator( "after" );
        sfp.setValue( "$e" );
        sfp.setConstraintValueType( BaseSingleFieldConstraint.TYPE_VARIABLE );
        sfp.getParameters().put( "0",
                                 "1d" );
        sfp.getParameters().put( "1",
                                 "2d" );
        sfp.getParameters().put( "org.kie.guvnor.guided.editor.visibleParameterSet",
                                 "2" );
        sfp.getParameters().put( "org.kie.guvnor.guided.server.util.BRDRLPersistence.operatorParameterGenerator",
                                 "org.drools.workbench.models.commons.backend.rule.CEPOperatorParameterDRLBuilder" );

        fp2.addConstraint( sfp );
        m.addLhsItem( fp1 );
        m.addLhsItem( fp2 );

        String actualDrl = BRDRLPersistence.getInstance().marshal( m );
        assertEqualsIgnoreWhitespace( drl,
                                      actualDrl );
    }

    @Test
    public void testSingleFieldConstraintCEPOperatorTimeWindow() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Event() over window:time (1d)\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );

        IPattern p1 = m.lhs[ 0 ];
        assertTrue( p1 instanceof FactPattern );
        FactPattern fp1 = (FactPattern) p1;
        assertEquals( "Event",
                      fp1.getFactType() );
        assertNull( fp1.getBoundName() );
        assertEquals( 0,
                      fp1.getNumberOfConstraints() );

        assertNotNull( fp1.getWindow() );
        CEPWindow window = fp1.getWindow();
        assertEquals( "over window:time",
                      window.getOperator() );
        assertEquals( 2,
                      window.getParameters().size() );
        assertEquals( "1d",
                      window.getParameter( "1" ) );
        assertEquals( "org.drools.workbench.models.commons.backend.rule.CEPWindowOperatorParameterDRLBuilder",
                      window.getParameter( "org.kie.guvnor.guided.server.util.BRDRLPersistence.operatorParameterGenerator" ) );
    }

    @Test
    public void testReciprocal_SingleFieldConstraintCEPOperatorTimeWindow() {
        //This is the inverse of "SingleFieldConstraintCEPOperatorTimeWindow"
        String drl = "rule \"rule1\"\n"
                + "dialect \"mvel\"\n"
                + "when\n"
                + "Event() over window:time (1d)\n"
                + "then\n"
                + "end";

        RuleModel m = new RuleModel();
        m.name = "rule1";

        FactPattern fp1 = new FactPattern();
        fp1.setFactType( "Event" );

        CEPWindow window = new CEPWindow();
        window.setOperator( "over window:time" );
        window.getParameters().put( "1",
                                    "1d" );
        window.getParameters().put( "org.kie.guvnor.guided.server.util.BRDRLPersistence.operatorParameterGenerator",
                                    "org.drools.workbench.models.commons.backend.rule.CEPWindowOperatorParameterDRLBuilder" );
        fp1.setWindow( window );

        m.addLhsItem( fp1 );

        String actualDrl = BRDRLPersistence.getInstance().marshal( m );
        assertEqualsIgnoreWhitespace( drl,
                                      actualDrl );
    }

    @Test
    public void testSingleFieldConstraintCEPOperatorTimeLength() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "Event() over window:length (10)\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );

        IPattern p1 = m.lhs[ 0 ];
        assertTrue( p1 instanceof FactPattern );
        FactPattern fp1 = (FactPattern) p1;
        assertEquals( "Event",
                      fp1.getFactType() );
        assertNull( fp1.getBoundName() );
        assertEquals( 0,
                      fp1.getNumberOfConstraints() );

        assertNotNull( fp1.getWindow() );
        CEPWindow window = fp1.getWindow();
        assertEquals( "over window:length",
                      window.getOperator() );
        assertEquals( 2,
                      window.getParameters().size() );
        assertEquals( "10",
                      window.getParameter( "1" ) );
        assertEquals( "org.drools.workbench.models.commons.backend.rule.CEPWindowOperatorParameterDRLBuilder",
                      window.getParameter( "org.kie.guvnor.guided.server.util.BRDRLPersistence.operatorParameterGenerator" ) );
    }

    @Test
    public void testReciprocal_SingleFieldConstraintCEPOperatorTimeLength() {
        //This is the inverse of "SingleFieldConstraintCEPOperatorTimeLength"
        String drl = "rule \"rule1\"\n"
                + "dialect \"mvel\"\n"
                + "when\n"
                + "Event() over window:length (10)\n"
                + "then\n"
                + "end";

        RuleModel m = new RuleModel();
        m.name = "rule1";

        FactPattern fp1 = new FactPattern();
        fp1.setFactType( "Event" );

        CEPWindow window = new CEPWindow();
        window.setOperator( "over window:length" );
        window.getParameters().put( "1",
                                    "10" );
        window.getParameters().put( "org.kie.guvnor.guided.server.util.BRDRLPersistence.operatorParameterGenerator",
                                    "org.drools.workbench.models.commons.backend.rule.CEPWindowOperatorParameterDRLBuilder" );
        fp1.setWindow( window );

        m.addLhsItem( fp1 );

        String actualDrl = BRDRLPersistence.getInstance().marshal( m );
        assertEqualsIgnoreWhitespace( drl,
                                      actualDrl );
    }

    @Test
    public void testExtends() {
        String drl = "rule \"rule1\" extends \"rule2\" \n"
                + "when\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( "rule2", m.parentName );
    }

    @Test
    public void testRuleNameWithoutTheQuotes() {
        String drl = "rule rule1\n"
                + "when\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

    }

    @Test
    public void testMetaData() {
        String drl = "rule rule1\n"
                + "@author( Bob )\n"
                + "when\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( 1,
                      m.metadataList.length );
        assertEquals( "author",
                      m.metadataList[ 0 ].getAttributeName() );
        assertEquals( "Bob",
                      m.metadataList[ 0 ].getValue() );

    }

    @Test
    public void testAttributes() {
        String drl = "rule rule1\n"
                + "salience 42\n"
                + "when\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( 1,
                      m.attributes.length );
        assertEquals( "salience",
                      m.attributes[ 0 ].getAttributeName() );
        assertEquals( "42",
                      m.attributes[ 0 ].getValue() );

    }

    @Test
    public void testEval() {
        String drl = "rule rule1\n"
                + "when\n"
                + "eval( true )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( 1, m.lhs.length );
        assertTrue( m.lhs[ 0 ] instanceof FreeFormLine );
        assertEquals( "eval( true )", ( (FreeFormLine) m.lhs[ 0 ] ).getText() );
    }

    @Test
    public void testNestedFieldExpressions() {
        String drl =
                "rule rule1\n"
                        + "when\n"
                        + "Person( address.postalCode == 12345 )\n"
                        + "then\n"
                        + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal(drl);

        assertNotNull(m);
        assertEquals(1, m.lhs.length);
        assertTrue(m.lhs[0] instanceof FactPattern);
        assertTrue(((FactPattern) m.lhs[0]).getFieldConstraints()[0] instanceof SingleFieldConstraintEBLeftSide);

        SingleFieldConstraintEBLeftSide ebLeftSide = (SingleFieldConstraintEBLeftSide) ((FactPattern) m.lhs[0]).getFieldConstraints()[0];
        assertEquals("postalCode", ebLeftSide.getFieldName());
        //assertEquals("java.lang.Integer", ebLeftSide.getFieldType());
        assertEquals("==", ebLeftSide.getOperator());
        assertEquals("12345", ebLeftSide.getValue());

        assertEquals(3, ebLeftSide.getExpressionLeftSide().getParts().size());
        assertTrue(ebLeftSide.getExpressionLeftSide().getParts().get(0) instanceof ExpressionUnboundFact);
        ExpressionUnboundFact expressionUnboundFact = ((ExpressionUnboundFact) ebLeftSide.getExpressionLeftSide().getParts().get(0));
        assertEquals("Person", expressionUnboundFact.getName());
        assertEquals("Person", expressionUnboundFact.getClassType());
        assertEquals("Person", expressionUnboundFact.getGenericType());
        assertEquals(m.lhs[0], expressionUnboundFact.getFact());

        assertEquals(null, expressionUnboundFact.getPrevious());
        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(1), expressionUnboundFact.getNext());

        assertTrue(ebLeftSide.getExpressionLeftSide().getParts().get(1) instanceof ExpressionField);
        ExpressionField expressionField1 = (ExpressionField) ebLeftSide.getExpressionLeftSide().getParts().get(1);
        assertEquals("address", expressionField1.getName());
        //assertEquals("Address", expressionField1.getClassType()); // Now this needs needs some magic, Address is a declarative model
        //assertEquals("Address", expressionField1.getGenericType());

        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(0), expressionField1.getPrevious());
        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(2), expressionField1.getNext());

        assertTrue(ebLeftSide.getExpressionLeftSide().getParts().get(2) instanceof ExpressionField);
        ExpressionField expressionField2 = (ExpressionField) ebLeftSide.getExpressionLeftSide().getParts().get(2);
        assertEquals("postalCode", expressionField2.getName());
        //assertEquals("java.lang.Integer", expressionField2.getClassType());
        assertEquals("Integer", expressionField2.getGenericType());

        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(1), expressionField2.getPrevious());
        assertEquals(null, expressionField2.getNext());
    }

    @Test
    public void testNestedFieldExpressionsWithAFunction() {
        String drl =
                "rule rule1\n"
                        + "when\n"
                        + "Person( address.postalCode == myFunction() )\n"
                        + "then\n"
                        + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal(drl);

        assertNotNull(m);
        assertEquals(1, m.lhs.length);
        assertTrue(m.lhs[0] instanceof FactPattern);
        assertTrue(((FactPattern) m.lhs[0]).getFieldConstraints()[0] instanceof SingleFieldConstraintEBLeftSide);

        SingleFieldConstraintEBLeftSide ebLeftSide = (SingleFieldConstraintEBLeftSide) ((FactPattern) m.lhs[0]).getFieldConstraints()[0];
        assertEquals("postalCode", ebLeftSide.getFieldName());
        //assertEquals("java.lang.Integer", ebLeftSide.getFieldType());
        assertEquals("==", ebLeftSide.getOperator());
        assertEquals("myFunction()", ebLeftSide.getValue());

        assertEquals(3, ebLeftSide.getExpressionLeftSide().getParts().size());
        assertTrue(ebLeftSide.getExpressionLeftSide().getParts().get(0) instanceof ExpressionUnboundFact);
        ExpressionUnboundFact expressionUnboundFact = ((ExpressionUnboundFact) ebLeftSide.getExpressionLeftSide().getParts().get(0));
        assertEquals("Person", expressionUnboundFact.getName());
        //assertEquals("Person", expressionUnboundFact.getClassType());
        //assertEquals("Person", expressionUnboundFact.getGenericType());
        assertEquals(m.lhs[0], expressionUnboundFact.getFact());

        assertEquals(null, expressionUnboundFact.getPrevious());
        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(1), expressionUnboundFact.getNext());

        assertTrue(ebLeftSide.getExpressionLeftSide().getParts().get(1) instanceof ExpressionField);
        ExpressionField expressionField1 = (ExpressionField) ebLeftSide.getExpressionLeftSide().getParts().get(1);
        assertEquals("address", expressionField1.getName());
        //assertEquals("Address", expressionField1.getClassType()); // Now this needs needs some magic, Address is a declarative model
        //assertEquals("Address", expressionField1.getGenericType());

        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(0), expressionField1.getPrevious());
        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(2), expressionField1.getNext());

        assertTrue(ebLeftSide.getExpressionLeftSide().getParts().get(2) instanceof ExpressionField);
        ExpressionField expressionField2 = (ExpressionField) ebLeftSide.getExpressionLeftSide().getParts().get(2);
        assertEquals("postalCode", expressionField2.getName());
        //assertEquals("java.lang.Integer", expressionField2.getClassType());
        //assertEquals("Integer", expressionField2.getGenericType());

        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(1), expressionField2.getPrevious());
        assertEquals(null, expressionField2.getNext());
    }


    @Test @Ignore("it is not possible to distinguish an expression from an enum value like CHEESE.Cheddar")
    public void testNestedFieldExpressionsWithAnotherExpression() {
        String drl =
                "rule rule1\n"
                        + "when\n"
                        + "p : Person( address.postalCode == p.address.postalCode) )\n"
                        + "then\n"
                        + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal(drl);

        assertNotNull(m);
        assertEquals(1, m.lhs.length);
        assertTrue(m.lhs[0] instanceof FactPattern);
        assertTrue(((FactPattern) m.lhs[0]).getFieldConstraints()[0] instanceof SingleFieldConstraintEBLeftSide);

        SingleFieldConstraintEBLeftSide ebLeftSide = (SingleFieldConstraintEBLeftSide) ((FactPattern) m.lhs[0]).getFieldConstraints()[0];
        assertEquals("postalCode", ebLeftSide.getFieldName());
        //assertEquals("java.lang.Integer", ebLeftSide.getFieldType());
        assertEquals("==", ebLeftSide.getOperator());
        assertEquals("", ebLeftSide.getValue());

        assertEquals(3,ebLeftSide.getExpressionValue().getParts().size());

        assertTrue(ebLeftSide.getExpressionValue().getParts().get(0) instanceof ExpressionVariable);
        ExpressionVariable expressionVariable=(ExpressionVariable)ebLeftSide.getExpressionValue().getParts().get(0);
        assertEquals("p",expressionVariable.getName());
        //assertEquals("Person",expressionVariable.getClassType());
        //assertEquals("Person",expressionVariable.getGenericType());

        assertTrue(ebLeftSide.getExpressionValue().getParts().get(1) instanceof ExpressionField);
        ExpressionField ef1=(ExpressionField)ebLeftSide.getExpressionValue().getParts().get(1);
        assertEquals("address",ef1.getName());
        //assertEquals("Address",ef1.getClassType());
        //assertEquals("Address",ef1.getGenericType());


        assertTrue(ebLeftSide.getExpressionValue().getParts().get(2) instanceof ExpressionField);
        ExpressionField ef2=(ExpressionField)ebLeftSide.getExpressionValue().getParts().get(2);
        assertEquals("postalCode",ef2.getName());
        //assertEquals("java.lang.Integer",ef2.getClassType());
        //assertEquals("Integer",ef2.getGenericType());



        assertEquals(3, ebLeftSide.getExpressionLeftSide().getParts().size());
        assertTrue(ebLeftSide.getExpressionLeftSide().getParts().get(0) instanceof ExpressionUnboundFact);
        ExpressionUnboundFact expressionUnboundFact = ((ExpressionUnboundFact) ebLeftSide.getExpressionLeftSide().getParts().get(0));
        assertEquals("Person", expressionUnboundFact.getName());
        //assertEquals("Person", expressionUnboundFact.getClassType());
        //assertEquals("Person", expressionUnboundFact.getGenericType());
        assertEquals(m.lhs[0], expressionUnboundFact.getFact());

        assertEquals(null, expressionUnboundFact.getPrevious());
        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(1), expressionUnboundFact.getNext());

        assertTrue(ebLeftSide.getExpressionLeftSide().getParts().get(1) instanceof ExpressionField);
        ExpressionField expressionField1 = (ExpressionField) ebLeftSide.getExpressionLeftSide().getParts().get(1);
        assertEquals("address", expressionField1.getName());
        //assertEquals("Address", expressionField1.getClassType()); // Now this needs needs some magic, Address is a declarative model
        //assertEquals("Address", expressionField1.getGenericType());

        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(0), expressionField1.getPrevious());
        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(2), expressionField1.getNext());

        assertTrue(ebLeftSide.getExpressionLeftSide().getParts().get(2) instanceof ExpressionField);
        ExpressionField expressionField2 = (ExpressionField) ebLeftSide.getExpressionLeftSide().getParts().get(2);
        assertEquals("postalCode", expressionField2.getName());
        //assertEquals("java.lang.Integer", expressionField2.getClassType());
        //assertEquals("Integer", expressionField2.getGenericType());

        assertEquals(ebLeftSide.getExpressionLeftSide().getParts().get(1), expressionField2.getPrevious());
        assertEquals(null, expressionField2.getNext());
    }

    @Test
    public void testSingleFieldConstraintContainsOperator() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "$is : IncomeSource( )\n"
                + "Applicant( incomes contains $is )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 2,
                      m.lhs.length );
        IPattern p0 = m.lhs[ 0 ];
        assertTrue( p0 instanceof FactPattern );

        FactPattern fp0 = (FactPattern) p0;
        assertEquals( "IncomeSource",
                      fp0.getFactType() );
        assertEquals( "$is",
                      fp0.getBoundName() );

        IPattern p1 = m.lhs[ 1 ];
        assertTrue( p1 instanceof FactPattern );

        FactPattern fp1 = (FactPattern) p1;
        assertEquals( "Applicant",
                      fp1.getFactType() );

        assertEquals( 0,
                      fp0.getNumberOfConstraints() );

        assertEquals( 1,
                      fp1.getNumberOfConstraints() );
        assertTrue( fp1.getConstraint( 0 ) instanceof SingleFieldConstraint );

        SingleFieldConstraint sfp = (SingleFieldConstraint) fp1.getConstraint( 0 );
        assertEquals( "Applicant",
                      sfp.getFactType() );
        assertEquals( "incomes",
                      sfp.getFieldName() );
        assertEquals( "contains",
                      sfp.getOperator() );
        assertEquals( "$is",
                      sfp.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_VARIABLE,
                      sfp.getConstraintValueType() );
    }

    @Test
    public void testCompositeFactPatternWithOr() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "( Person( age == 42 ) or Person( age == 43 ) )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        //LHS Pattern
        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof CompositeFactPattern );
        CompositeFactPattern cfp = (CompositeFactPattern) p;
        assertEquals( CompositeFactPattern.COMPOSITE_TYPE_OR,
                      cfp.getType() );

        //LHS sub-patterns
        assertEquals( 2,
                      cfp.getPatterns().length );
        IPattern cfp_p1 = cfp.getPatterns()[ 0 ];
        assertTrue( cfp_p1 instanceof FactPattern );
        FactPattern fp1 = (FactPattern) cfp_p1;
        assertEquals( "Person",
                      fp1.getFactType() );
        assertEquals( 1,
                      fp1.getConstraintList().getConstraints().length );
        assertTrue( fp1.getConstraint( 0 ) instanceof SingleFieldConstraint );
        SingleFieldConstraint cfp_sfp1 = (SingleFieldConstraint) fp1.getConstraint( 0 );
        assertEquals( "Person",
                      cfp_sfp1.getFactType() );
        assertEquals( "age",
                      cfp_sfp1.getFieldName() );
        assertEquals( "==",
                      cfp_sfp1.getOperator() );
        assertEquals( "42",
                      cfp_sfp1.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      cfp_sfp1.getConstraintValueType() );

        IPattern cfp_p2 = cfp.getPatterns()[ 1 ];
        assertTrue( cfp_p2 instanceof FactPattern );
        FactPattern fp2 = (FactPattern) cfp_p2;
        assertEquals( "Person",
                      fp2.getFactType() );
        assertTrue( fp2.getConstraint( 0 ) instanceof SingleFieldConstraint );
        SingleFieldConstraint cfp_sfp2 = (SingleFieldConstraint) fp2.getConstraint( 0 );
        assertEquals( "Person",
                      cfp_sfp2.getFactType() );
        assertEquals( "age",
                      cfp_sfp2.getFieldName() );
        assertEquals( "==",
                      cfp_sfp2.getOperator() );
        assertEquals( "43",
                      cfp_sfp2.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      cfp_sfp2.getConstraintValueType() );
    }

    @Test
    public void testReciprocal_CompositeFactPatternWithOr() {
        //This is the inverse of "CompositeFactPatternWithOr"
        String drl = "rule \"rule1\"\n"
                + "dialect \"mvel\"\n"
                + "when\n"
                + "( Person( age == 42 ) or Person( age == 43 ) )\n"
                + "then\n"
                + "end";

        RuleModel m = new RuleModel();
        m.name = "rule1";

        //LHS Patterns
        CompositeFactPattern cfp = new CompositeFactPattern();
        cfp.setType( CompositeFactPattern.COMPOSITE_TYPE_OR );

        //LHS sub-patterns
        FactPattern fp1 = new FactPattern();
        fp1.setFactType( "Person" );

        SingleFieldConstraint cfp_sfp1 = new SingleFieldConstraint();
        cfp_sfp1.setFactType( "Person" );
        cfp_sfp1.setFieldName( "age" );
        cfp_sfp1.setOperator( "==" );
        cfp_sfp1.setValue( "42" );
        cfp_sfp1.setFieldType( DataType.TYPE_NUMERIC_INTEGER );
        cfp_sfp1.setConstraintValueType( BaseSingleFieldConstraint.TYPE_LITERAL );
        fp1.addConstraint( cfp_sfp1 );
        cfp.addFactPattern( fp1 );

        FactPattern fp2 = new FactPattern();
        fp2.setFactType( "Person" );

        SingleFieldConstraint cfp_sfp2 = new SingleFieldConstraint();
        cfp_sfp2.setFactType( "Person" );
        cfp_sfp2.setFieldName( "age" );
        cfp_sfp2.setOperator( "==" );
        cfp_sfp2.setValue( "43" );
        cfp_sfp2.setFieldType( DataType.TYPE_NUMERIC_INTEGER );
        cfp_sfp2.setConstraintValueType( BaseSingleFieldConstraint.TYPE_LITERAL );
        fp2.addConstraint( cfp_sfp2 );
        cfp.addFactPattern( fp2 );

        m.addLhsItem( cfp );

        String actualDrl = BRDRLPersistence.getInstance().marshal( m );
        assertEqualsIgnoreWhitespace( drl,
                                      actualDrl );
    }

    @Test
    public void testCompositeFactPatternWithOrAndCompositeFieldConstraint() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "( Person( age == 42 ) or Person( age == 43 || age == 44) )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        //LHS Pattern
        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof CompositeFactPattern );

        CompositeFactPattern cfp = (CompositeFactPattern) p;
        assertEquals( CompositeFactPattern.COMPOSITE_TYPE_OR,
                      cfp.getType() );

        //LHS sub-patterns
        assertEquals( 2,
                      cfp.getPatterns().length );
        IPattern cfp_p1 = cfp.getPatterns()[ 0 ];
        assertTrue( cfp_p1 instanceof FactPattern );
        FactPattern fp1 = (FactPattern) cfp_p1;
        assertEquals( "Person",
                      fp1.getFactType() );
        assertEquals( 1,
                      fp1.getConstraintList().getConstraints().length );
        assertTrue( fp1.getConstraint( 0 ) instanceof SingleFieldConstraint );
        SingleFieldConstraint cfp_sfp1 = (SingleFieldConstraint) fp1.getConstraint( 0 );
        assertEquals( "Person",
                      cfp_sfp1.getFactType() );
        assertEquals( "age",
                      cfp_sfp1.getFieldName() );
        assertEquals( "==",
                      cfp_sfp1.getOperator() );
        assertEquals( "42",
                      cfp_sfp1.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      cfp_sfp1.getConstraintValueType() );

        IPattern cfp_p2 = cfp.getPatterns()[ 1 ];
        assertTrue( cfp_p2 instanceof FactPattern );
        FactPattern fp2 = (FactPattern) cfp_p2;
        assertEquals( 1,
                      fp2.getConstraintList().getConstraints().length );
        assertTrue( fp2.getConstraint( 0 ) instanceof CompositeFieldConstraint );

        CompositeFieldConstraint cfp_p2_cfp = (CompositeFieldConstraint) fp2.getConstraint( 0 );
        assertEquals( "||",
                      cfp_p2_cfp.getCompositeJunctionType() );
        assertEquals( 2,
                      cfp_p2_cfp.getNumberOfConstraints() );
        assertTrue( cfp_p2_cfp.getConstraint( 0 ) instanceof SingleFieldConstraint );
        assertTrue( cfp_p2_cfp.getConstraint( 1 ) instanceof SingleFieldConstraint );

        SingleFieldConstraint cfp_p2_sfp1 = (SingleFieldConstraint) cfp_p2_cfp.getConstraint( 0 );
        assertEquals( "Person",
                      cfp_p2_sfp1.getFactType() );
        assertEquals( "age",
                      cfp_p2_sfp1.getFieldName() );
        assertEquals( "==",
                      cfp_p2_sfp1.getOperator() );
        assertEquals( "43",
                      cfp_p2_sfp1.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      cfp_p2_sfp1.getConstraintValueType() );

        SingleFieldConstraint cfp_p2_sfp2 = (SingleFieldConstraint) cfp_p2_cfp.getConstraint( 1 );
        assertEquals( "Person",
                      cfp_p2_sfp2.getFactType() );
        assertEquals( "age",
                      cfp_p2_sfp2.getFieldName() );
        assertEquals( "==",
                      cfp_p2_sfp2.getOperator() );
        assertEquals( "44",
                      cfp_p2_sfp2.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      cfp_p2_sfp2.getConstraintValueType() );
    }

    @Test
    public void testReciprocal_CompositeFactPatternWithOrAndCompositeFieldConstraint() {
        //This is the inverse of "CompositeFactPatternWithOrAndCompositeFieldConstraint"
        String drl = "rule \"rule1\"\n"
                + "dialect \"mvel\"\n"
                + "when\n"
                + "( Person( age == 42 ) or Person( age == 43 || age == 44) )\n"
                + "then\n"
                + "end";

        RuleModel m = new RuleModel();
        m.name = "rule1";

        //LHS Pattern
        CompositeFactPattern cfp = new CompositeFactPattern();
        cfp.setType( CompositeFactPattern.COMPOSITE_TYPE_OR );

        //LHS sub-patterns
        FactPattern fp1 = new FactPattern();
        fp1.setFactType( "Person" );

        SingleFieldConstraint fp1_sfp1 = new SingleFieldConstraint();
        fp1_sfp1.setFactType( "Person" );
        fp1_sfp1.setFieldName( "age" );
        fp1_sfp1.setOperator( "==" );
        fp1_sfp1.setValue( "42" );
        fp1_sfp1.setFieldType( DataType.TYPE_NUMERIC_INTEGER );
        fp1_sfp1.setConstraintValueType( BaseSingleFieldConstraint.TYPE_LITERAL );
        fp1.addConstraint( fp1_sfp1 );

        FactPattern fp2 = new FactPattern();
        fp2.setFactType( "Person" );

        CompositeFieldConstraint fp2_cfp = new CompositeFieldConstraint();
        fp2_cfp.setCompositeJunctionType( CompositeFieldConstraint.COMPOSITE_TYPE_OR );
        fp2.addConstraint( fp2_cfp );

        SingleFieldConstraint fp2_sfp1 = new SingleFieldConstraint();
        fp2_sfp1.setFactType( "Person" );
        fp2_sfp1.setFieldName( "age" );
        fp2_sfp1.setOperator( "==" );
        fp2_sfp1.setValue( "43" );
        fp2_sfp1.setFieldType( DataType.TYPE_NUMERIC_INTEGER );
        fp2_sfp1.setConstraintValueType( BaseSingleFieldConstraint.TYPE_LITERAL );
        fp2_cfp.addConstraint( fp2_sfp1 );

        SingleFieldConstraint fp2_sfp2 = new SingleFieldConstraint();
        fp2_sfp2.setFactType( "Person" );
        fp2_sfp2.setFieldName( "age" );
        fp2_sfp2.setOperator( "==" );
        fp2_sfp2.setValue( "44" );
        fp2_sfp2.setFieldType( DataType.TYPE_NUMERIC_INTEGER );
        fp2_sfp2.setConstraintValueType( BaseSingleFieldConstraint.TYPE_LITERAL );
        fp2_cfp.addConstraint( fp2_sfp2 );

        cfp.addFactPattern( fp1 );
        cfp.addFactPattern( fp2 );
        m.addLhsItem( cfp );

        String actualDrl = BRDRLPersistence.getInstance().marshal( m );
        assertEqualsIgnoreWhitespace( drl,
                                      actualDrl );
    }

    @Test
    public void testNestedFieldConstraints() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "ParentType( this != null, this.parentChildField != null, this.parentChildField.childField == \"hello\" )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "ParentType",
                      fp.getFactType() );

        assertEquals( 3,
                      fp.getConstraintList().getConstraints().length );

        assertTrue( fp.getConstraint( 0 ) instanceof SingleFieldConstraint );
        SingleFieldConstraint sfp0 = (SingleFieldConstraint) fp.getConstraint( 0 );
        assertEquals( "ParentType",
                      sfp0.getFactType() );
        assertEquals( "this",
                      sfp0.getFieldName() );
        assertEquals( DataType.TYPE_THIS,
                      sfp0.getFieldType() );
        assertEquals( "!= null",
                      sfp0.getOperator() );
        assertNull( sfp0.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_UNDEFINED,
                      sfp0.getConstraintValueType() );
        assertNull( sfp0.getParent() );

        assertTrue( fp.getConstraint( 1 ) instanceof SingleFieldConstraint );
        SingleFieldConstraint sfp1 = (SingleFieldConstraint) fp.getConstraint( 1 );
        assertEquals( "ParentType",
                      sfp1.getFactType() );
        assertEquals( "parentChildField",
                      sfp1.getFieldName() );
        //assertEquals( "ChildType",
        //              sfp1.getFieldType() );
        assertEquals( "!= null",
                      sfp1.getOperator() );
        assertNull( sfp1.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_UNDEFINED,
                      sfp1.getConstraintValueType() );
        assertSame( sfp0,
                    sfp1.getParent() );

        assertTrue( fp.getConstraint( 2 ) instanceof SingleFieldConstraint );
        SingleFieldConstraint sfp2 = (SingleFieldConstraint) fp.getConstraint( 2 );
        assertEquals( "childField",
                      sfp2.getFieldName() );
        //assertEquals( DataType.TYPE_STRING,
        //              sfp2.getFieldType() );
        assertEquals( "==",
                      sfp2.getOperator() );
        assertEquals( "hello",
                      sfp2.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      sfp2.getConstraintValueType() );
        assertSame( sfp1,
                    sfp2.getParent() );
    }

    @Test
    public void testReciprocal_NestedFieldConstraints() {
        //This is the inverse of "NestedFieldConstraints"
        String drl = "rule \"rule1\"\n"
                + "dialect \"mvel\"\n"
                + "when\n"
                + "ParentType( this != null, this.parentChildField != null, this.parentChildField.childField == \"hello\" )\n"
                + "then\n"
                + "end";

        RuleModel m = new RuleModel();
        m.name = "rule1";

        //LHS Pattern
        FactPattern fp1 = new FactPattern();
        fp1.setFactType( "ParentType" );

        SingleFieldConstraint fp1_sfp1 = new SingleFieldConstraint();
        fp1_sfp1.setFactType( "ParentType" );
        fp1_sfp1.setFieldName( "this" );
        fp1_sfp1.setFieldType( DataType.TYPE_THIS );
        fp1_sfp1.setOperator( "!= null" );
        fp1_sfp1.setConstraintValueType( BaseSingleFieldConstraint.TYPE_UNDEFINED );
        fp1.addConstraint( fp1_sfp1 );

        SingleFieldConstraint fp1_sfp2 = new SingleFieldConstraint();
        fp1_sfp2.setFactType( "ParentType" );
        fp1_sfp2.setFieldName( "parentChildField" );
        fp1_sfp2.setFieldType( "ChildType" );
        fp1_sfp2.setOperator( "!= null" );
        fp1_sfp2.setConstraintValueType( BaseSingleFieldConstraint.TYPE_UNDEFINED );
        fp1.addConstraint( fp1_sfp2 );
        fp1_sfp2.setParent( fp1_sfp1 );

        SingleFieldConstraint fp1_sfp3 = new SingleFieldConstraint();
        fp1_sfp3.setFactType( "ChildType" );
        fp1_sfp3.setFieldName( "childField" );
        fp1_sfp3.setFieldType( DataType.TYPE_STRING );
        fp1_sfp3.setOperator( "==" );
        fp1_sfp3.setValue( "hello" );
        fp1_sfp3.setConstraintValueType( BaseSingleFieldConstraint.TYPE_LITERAL );
        fp1.addConstraint( fp1_sfp3 );
        fp1_sfp3.setParent( fp1_sfp2 );

        m.addLhsItem( fp1 );

        String actualDrl = BRDRLPersistence.getInstance().marshal( m );
        assertEqualsIgnoreWhitespace( drl,
                                      actualDrl );
    }

    @Test @Ignore("We always use SingleFieldConstraintEBLeftSide to unmarshall constraints")
    public void testNestedFieldConstraintsOnlyLeafOperator() {
        String drl = "rule \"rule1\"\n"
                + "when\n"
                + "ParentType( this.parentChildField.childField == \"hello\" )\n"
                + "then\n"
                + "end";

        RuleModel m = BRDRLPersistence.getInstance().unmarshal( drl );

        assertNotNull( m );
        assertEquals( "rule1",
                      m.name );

        assertEquals( 1,
                      m.lhs.length );
        IPattern p = m.lhs[ 0 ];
        assertTrue( p instanceof FactPattern );

        FactPattern fp = (FactPattern) p;
        assertEquals( "ParentType",
                      fp.getFactType() );

        assertEquals( 3,
                      fp.getConstraintList().getConstraints().length );

        assertTrue( fp.getConstraint( 0 ) instanceof SingleFieldConstraint );
        SingleFieldConstraint sfp0 = (SingleFieldConstraint) fp.getConstraint( 0 );
        assertEquals( "ParentType",
                      sfp0.getFactType() );
        assertEquals( "this",
                      sfp0.getFieldName() );
        assertEquals( DataType.TYPE_THIS,
                      sfp0.getFieldType() );
        assertNull( sfp0.getOperator() );
        assertNull( sfp0.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_UNDEFINED,
                      sfp0.getConstraintValueType() );
        assertNull( sfp0.getParent() );

        assertTrue( fp.getConstraint( 1 ) instanceof SingleFieldConstraint );
        SingleFieldConstraint sfp1 = (SingleFieldConstraint) fp.getConstraint( 1 );
        assertEquals( "ParentType",
                      sfp1.getFactType() );
        assertEquals( "parentChildField",
                      sfp1.getFieldName() );
        //assertEquals( "ChildType",
        //              sfp1.getFieldType() );
        assertNull( sfp1.getOperator() );
        assertNull( sfp1.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_UNDEFINED,
                      sfp1.getConstraintValueType() );
        assertSame( sfp0,
                    sfp1.getParent() );

        assertTrue( fp.getConstraint( 2 ) instanceof SingleFieldConstraint );
        SingleFieldConstraint sfp2 = (SingleFieldConstraint) fp.getConstraint( 2 );
        //assertEquals( "ChildType",
        //              sfp2.getFactType() );
        assertEquals( "childField",
                      sfp2.getFieldName() );
        //assertEquals( DataType.TYPE_STRING,
        //              sfp2.getFieldType() );
        assertEquals( "==",
                      sfp2.getOperator() );
        assertEquals( "hello",
                      sfp2.getValue() );
        assertEquals( BaseSingleFieldConstraint.TYPE_LITERAL,
                      sfp2.getConstraintValueType() );
        assertSame( sfp1,
                    sfp2.getParent() );
    }

    @Test
    public void testReciprocal_NestedFieldConstraintsOnlyLeafOperator() {
        //This is the inverse of "NestedFieldConstraintsOnlyLeafOperator"
        String drl = "rule \"rule1\"\n"
                + "dialect \"mvel\"\n"
                + "when\n"
                + "ParentType( this.parentChildField.childField == \"hello\" )\n"
                + "then\n"
                + "end";

        RuleModel m = new RuleModel();
        m.name = "rule1";

        //LHS Pattern
        FactPattern fp1 = new FactPattern();
        fp1.setFactType( "ParentType" );

        SingleFieldConstraint fp1_sfp1 = new SingleFieldConstraint();
        fp1_sfp1.setFactType( "ParentType" );
        fp1_sfp1.setFieldName( "this" );
        fp1_sfp1.setFieldType( DataType.TYPE_THIS );
        fp1_sfp1.setConstraintValueType( BaseSingleFieldConstraint.TYPE_UNDEFINED );
        fp1.addConstraint( fp1_sfp1 );

        SingleFieldConstraint fp1_sfp2 = new SingleFieldConstraint();
        fp1_sfp2.setFactType( "ParentType" );
        fp1_sfp2.setFieldName( "parentChildField" );
        fp1_sfp2.setFieldType( "ChildType" );
        fp1_sfp2.setConstraintValueType( BaseSingleFieldConstraint.TYPE_UNDEFINED );
        fp1.addConstraint( fp1_sfp2 );
        fp1_sfp2.setParent( fp1_sfp1 );

        SingleFieldConstraint fp1_sfp3 = new SingleFieldConstraint();
        fp1_sfp3.setFactType( "ChildType" );
        fp1_sfp3.setFieldName( "childField" );
        fp1_sfp3.setFieldType( DataType.TYPE_STRING );
        fp1_sfp3.setOperator( "==" );
        fp1_sfp3.setValue( "hello" );
        fp1_sfp3.setConstraintValueType( BaseSingleFieldConstraint.TYPE_LITERAL );
        fp1.addConstraint( fp1_sfp3 );
        fp1_sfp3.setParent( fp1_sfp2 );

        m.addLhsItem( fp1 );

        String actualDrl = BRDRLPersistence.getInstance().marshal( m );
        assertEqualsIgnoreWhitespace( drl,
                                      actualDrl );
    }

    private void assertEqualsIgnoreWhitespace( final String expected,
                                               final String actual ) {
        final String cleanExpected = expected.replaceAll( "\\s+",
                                                          "" );
        final String cleanActual = actual.replaceAll( "\\s+",
                                                      "" );

        assertEquals( cleanExpected,
                      cleanActual );
    }

}

package org.drools.examples;

import java.io.InputStreamReader;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.audit.WorkingMemoryFileLogger;
import org.drools.compiler.PackageBuilder;

public class StateExampleWithDynamicRules {

    /**
     * @param args
     */
    public static void main(final String[] args) throws Exception {

        PackageBuilder builder = new PackageBuilder();
        builder.addPackageFromDrl( new InputStreamReader( StateExampleWithDynamicRules.class.getResourceAsStream( "StateExampleUsingSalience.drl" ) ) );

        final RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage( builder.getPackage() );

        final WorkingMemory workingMemory = ruleBase.newWorkingMemory();

        final WorkingMemoryFileLogger logger = new WorkingMemoryFileLogger( workingMemory );
        logger.setFileName( "log/state" );

        final State a = new State( "A" );
        final State b = new State( "B" );
        final State c = new State( "C" );
        final State d = new State( "D" );
        final State e = new State( "E" );

        // By setting dynamic to TRUE, Drools will use JavaBean
        // PropertyChangeListeners so you don't have to call modifyObject().
        final boolean dynamic = true;

        workingMemory.assertObject( a,
                                    dynamic );
        workingMemory.assertObject( b,
                                    dynamic );
        workingMemory.assertObject( c,
                                    dynamic );
        workingMemory.assertObject( d,
                                    dynamic );
        workingMemory.assertObject( e,
                                    dynamic );

        workingMemory.fireAllRules();

        builder = new PackageBuilder();
        builder.addPackageFromDrl( new InputStreamReader( StateExampleWithDynamicRules.class.getResourceAsStream( "/StateExampleDynamicRule.drl" ) ) );
        ruleBase.addPackage( builder.getPackage() );

        workingMemory.fireAllRules();

        logger.writeToDisk();
    }

}

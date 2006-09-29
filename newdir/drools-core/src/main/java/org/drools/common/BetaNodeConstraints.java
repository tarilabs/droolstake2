package org.drools.common;

/*
 * Copyright 2005 JBoss Inc
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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.drools.WorkingMemory;
import org.drools.base.evaluators.Operator;
import org.drools.reteoo.BetaMemory;
import org.drools.reteoo.ObjectHashTable;
import org.drools.rule.Declaration;
import org.drools.rule.LiteralConstraint;
import org.drools.rule.VariableConstraint;
import org.drools.spi.Evaluator;
import org.drools.spi.FieldConstraint;
import org.drools.spi.FieldExtractor;
import org.drools.spi.Tuple;
import org.drools.util.FactHashTable;
import org.drools.util.FieldIndexHashTable;
import org.drools.util.LinkedList;
import org.drools.util.LinkedListEntry;
import org.drools.util.TupleHashTable;

public class BetaNodeConstraints
    implements
    Serializable {

    /**
     * 
     */
    private static final long               serialVersionUID         = 320L;

    public final static BetaNodeConstraints emptyBetaNodeConstraints = new BetaNodeConstraints();

    private final LinkedList         constraints;

    public BetaNodeConstraints() {
        this.constraints = null;
    }

    public BetaNodeConstraints(final FieldConstraint constraint) {
        this( new FieldConstraint[]{constraint} );
    }

    public BetaNodeConstraints(final FieldConstraint[] constraints) {
        this.constraints =  new  LinkedList();
        for ( int  i  = 0, length = constraints.length; i < length; i++ ) {
            this.constraints.add( new LinkedListEntry( constraints[i] ) );
        }        
    }

    public boolean isAllowed(final InternalFactHandle handle,
                             final Tuple tuple,
                             final WorkingMemory workingMemory) {
        if ( this.constraints == null ) {
            return true;
        }
                
        for ( LinkedListEntry entry = ( LinkedListEntry ) this.constraints.getFirst(); entry != null; entry = ( LinkedListEntry ) entry.getNext() ) {
            FieldConstraint constraint = (FieldConstraint) entry.getObject();
            if ( constraint.isAllowed( handle.getObject(),
                                       tuple,
                                       workingMemory ) ) {
                return false;
            }            
        }
        return true;
    }    

//    public Set getRequiredDeclarations() {
//        final Set declarations = new HashSet();
//        for ( int i = 0; i < this.constraints.length; i++ ) {
//            final Declaration[] array = this.constraints[i].getRequiredDeclarations();
//            for ( int j = 0; j < array.length; j++ ) {
//                declarations.add( array[j] );
//            }
//        }
//        return declarations;
//    }

    public int hashCode() {
        return this.constraints.hashCode();
    }

    public LinkedList getConstraints() {
        return this.constraints;
    }

    /**
     * Determine if another object is equal to this.
     * 
     * @param object
     *            The object to test.
     * 
     * @return <code>true</code> if <code>object</code> is equal to this,
     *         otherwise <code>false</code>.
     */
    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }

        if ( object == null || getClass() != object.getClass() ) {
            return false;
        }

        final BetaNodeConstraints other = (BetaNodeConstraints) object;

        if ( this.constraints == other.constraints ) {
            return true;
        }

        if ( this.constraints.size() != other.constraints.size() ) {
            return false;
        }
        
        return this.constraints.equals( other );
    }

}
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
import org.drools.common.InstanceNotEqualsConstraint.InstanceNotEqualsConstraintContextEntry;
import org.drools.reteoo.BetaMemory;
import org.drools.reteoo.ObjectHashTable;
import org.drools.reteoo.ReteTuple;
import org.drools.rule.ContextEntry;
import org.drools.rule.Declaration;
import org.drools.rule.LiteralConstraint;
import org.drools.rule.VariableConstraint;
import org.drools.spi.BetaNodeFieldConstraint;
import org.drools.spi.Constraint;
import org.drools.spi.Evaluator;
import org.drools.spi.AlphaNodeFieldConstraint;
import org.drools.spi.FieldExtractor;
import org.drools.spi.Tuple;
import org.drools.util.CompositeFieldIndexHashTable;
import org.drools.util.FactHashTable;
import org.drools.util.FieldIndexHashTable;
import org.drools.util.LinkedList;
import org.drools.util.LinkedListEntry;
import org.drools.util.TupleHashTable;
import org.drools.util.CompositeFieldIndexHashTable.FieldIndex;

public class DefaultBetaConstraints
    implements
    Serializable, BetaConstraints {

    /**
     * 
     */
    private static final long               serialVersionUID         = 320L;

    private final LinkedList                constraints;

    private ContextEntry                    contexts;

    private boolean                         indexed;   

    public DefaultBetaConstraints(final BetaNodeFieldConstraint constraint) {
        this( new BetaNodeFieldConstraint[]{constraint} );
    }        

    public DefaultBetaConstraints(final BetaNodeFieldConstraint[] constraints) {
        this.constraints = new LinkedList();
        ContextEntry current = null;
        for ( int i = 0, length = constraints.length; i < length; i++ ) {
            // Determine  if this constraint is indexable
            // it is only indexable if there is already no indexed constraints
            // An indexed constraint is always the first constraint
            if ( isIndexable( constraints[i] ) && !indexed ) {
                this.constraints.insertAfter( null, new LinkedListEntry( constraints[i] ) );
                this.indexed = true;
            } else {
                this.constraints.add( new LinkedListEntry( constraints[i] ) );
            }
            //Setup  the  contextEntry cache to be iterated  in the same order
            ContextEntry context = constraints[i].getContextEntry();
            if ( current == null ) {
                current = context;
                this.contexts = context;
            } else {
                current.setNext( context );
            }
            current = context;
        }
    }
    
    private  boolean isIndexable(final BetaNodeFieldConstraint constraint) {        
        if ( constraint.getClass() == VariableConstraint.class ) {
            VariableConstraint variableConstraint = (VariableConstraint) constraint;
            return ( variableConstraint.getEvaluator().getOperator() == Operator.EQUAL );
        } else {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#updateFromTuple(org.drools.reteoo.ReteTuple)
     */
    public void updateFromTuple(ReteTuple tuple) {
        for ( ContextEntry context = this.contexts; context != null; context = context.getNext() ) {
            context.updateFromTuple( tuple );
        }
    }

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#updateFromFactHandle(org.drools.common.InternalFactHandle)
     */
    public void updateFromFactHandle(InternalFactHandle handle) {
        for ( ContextEntry context = this.contexts; context != null; context = context.getNext() ) {
            context.updateFromFactHandle( handle );
        }
    }
    
    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#isAllowedCachedLeft(java.lang.Object)
     */
    public boolean isAllowedCachedLeft(Object object ) {       
        LinkedListEntry entry = (LinkedListEntry) this.constraints.getFirst();
        ContextEntry context = this.contexts;
        while ( entry != null ) {
            if ( !((BetaNodeFieldConstraint) entry.getObject()).isAllowedCachedLeft(context, object ) ) {
                return false;
            }
            entry = (LinkedListEntry) entry.getNext();
            context = context.getNext();
        }
        return true;        
    }    
    
    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#isAllowedCachedRight(org.drools.reteoo.ReteTuple)
     */
    public boolean isAllowedCachedRight(ReteTuple tuple) {
        LinkedListEntry entry = (LinkedListEntry) this.constraints.getFirst();
        ContextEntry context = this.contexts;
        while ( entry != null ) {
            if ( !((BetaNodeFieldConstraint) entry.getObject()).isAllowedCachedRight(tuple, context ) ) {
                return false;
            }
            entry = (LinkedListEntry) entry.getNext();
            context = context.getNext();
        }
        return true;        
    }   
    
    public boolean isIndexed() {
        return this.indexed;
    }
    
    public boolean isEmpty() {
        return false;   
    }
    
    public BetaMemory createBetaMemory()  {
        BetaMemory memory;
        if ( this.indexed ) {
            Constraint constraint = ( Constraint ) this.constraints.getFirst();
            VariableConstraint variableConstraint = (VariableConstraint) constraint;
            FieldIndex  index  = new  FieldIndex(variableConstraint.getFieldExtractor(), variableConstraint.getRequiredDeclarations()[0]);
            memory = new BetaMemory( new TupleHashTable(),
                                     new CompositeFieldIndexHashTable( new FieldIndex[] { index }  ) );
        } else  {        
            memory = new BetaMemory( new TupleHashTable(),
                                     new FactHashTable() );
        }
        
        return memory;        
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

    /* (non-Javadoc)
     * @see org.drools.common.BetaNodeConstraints#getConstraints()
     */
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

        final DefaultBetaConstraints other = (DefaultBetaConstraints) object;

        if ( this.constraints == other.constraints ) {
            return true;
        }

        if ( this.constraints.size() != other.constraints.size() ) {
            return false;
        }

        return this.constraints.equals( other );
    }

}
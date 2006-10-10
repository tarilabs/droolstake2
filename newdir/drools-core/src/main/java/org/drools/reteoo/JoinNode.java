package org.drools.reteoo;

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

import org.drools.common.BetaNodeConstraints;
import org.drools.common.InternalFactHandle;
import org.drools.common.InternalWorkingMemory;
import org.drools.spi.PropagationContext;
import org.drools.util.Iterator;
import org.drools.util.AbstractHashTable.FactEntry;

/**
 * <code>JoinNode</code> extends <code>BetaNode</code> to perform
 * <code>ReteTuple</code> and <code>FactHandle</code> joins. Tuples are
 * considered to be asserted from the left input and facts from the right input.
 * The <code>BetaNode</code> provides the BetaMemory to store assserted
 * ReteTuples and
 * <code>FactHandleImpl<code>s. Each fact handle is stored in the right memory as a key in a <code>HashMap</code>, the value is an <code>ObjectMatches</code> 
 * instance which maintains a <code>LinkedList of <code>TuplesMatches - The tuples that are matched with the handle. the left memory is a <code>LinkedList</code> 
 * of <code>ReteTuples</code> which maintains a <code>HashMa</code>, where the keys are the matching <code>FactHandleImpl</code>s and the value is 
 * populated <code>TupleMatche</code>es, the keys are matched fact handles. <code>TupleMatch</code> maintains a <code>List</code> of resulting joins, 
 * where there is joined <code>ReteTuple</code> per <code>TupleSink</code>.
 *  
 * 
 * The BetaNode provides
 * the BetaMemory which stores the 
 * 
 * @see BetaNode
 * @see ObjectMatches
 * @see TupleMatch
 * @see TupleSink
 * 
 * @author <a href="mailto:mark.proctor@jboss.com">Mark Proctor</a>
 * @author <a href="mailto:bob@werken.com">Bob McWhirter</a>
 *
 */
class JoinNode extends BetaNode {
    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 320;

    /**
     * Construct.
     * 
     * @param leftInput
     *            The left input <code>TupleSource</code>.
     * @param rightInput
     *            The right input <code>TupleSource</code>.
     */
    JoinNode(final int id,
             final TupleSource leftInput,
             final ObjectSource rightInput) {
        super( id,
               leftInput,
               rightInput );
    }

    JoinNode(final int id,
             final TupleSource leftInput,
             final ObjectSource rightInput,
             final BetaNodeConstraints binder) {
        super( id,
               leftInput,
               rightInput,
               binder );
    }

    /**
     * Assert a new <code>ReteTuple</code>. The right input of
     * <code>FactHandleInput</code>'s is iterated and joins attemped, via the
     * binder, any successful bindings results in joined tuples being created
     * and propaged. there is a joined tuple per TupleSink.
     * 
     * @see ReteTuple
     * @see ObjectMatches
     * @see TupleSink
     * @see TupleMatch
     * 
     * @param tuple
     *            The <code>Tuple</code> being asserted.
     * @param context
     *            The <code>PropagationContext</code>
     * @param workingMemory
     *            The working memory seesion.
     */
    public void assertTuple(final ReteTuple leftTuple,
                            final PropagationContext context,
                            final InternalWorkingMemory workingMemory) {
        final BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );
        memory.getTupleMemory().add( leftTuple );
        Iterator it = memory.getObjectMemory().iterator( leftTuple );
        
        this.constraints.updateFromTuple( leftTuple );
        for ( FactEntry entry = ( FactEntry ) it.next(); entry != null; entry = ( FactEntry ) it.next() ) {
            InternalFactHandle handle = entry.getFactHandle();
            this.constraints.updateFromFactHandle( handle );
            if ( this.constraints.isAllowed() ) {
                sink.propagateAssertTuple( leftTuple, handle, context, workingMemory );
            }
        }        
    }

    /**
     * Assert a new <code>FactHandleImpl</code>. The left input of
     * <code>ReteTuple</code>s is iterated and joins attemped, via the
     * binder, any successful bindings results in joined tuples being created
     * and propaged. there is a joined tuple per TupleSink.
     * 
     * @see ReteTuple
     * @see ObjectMatches
     * @see TupleSink
     * @see TupleMatch
     * 
     * @param handle
     *            The <code>FactHandleImpl</code> being asserted.
     * @param context
     *            The <code>PropagationContext</code>
     * @param workingMemory
     *            The working memory seesion.
     */
    public void assertObject(final InternalFactHandle handle,
                             final PropagationContext context,
                             final InternalWorkingMemory workingMemory) {
        final BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );
        memory.getObjectMemory().add( handle );
        
        Iterator it = memory.getTupleMemory().iterator();
        this.constraints.updateFromFactHandle( handle );
        for ( ReteTuple tuple = ( ReteTuple ) it.next(); tuple != null; tuple = ( ReteTuple ) it.next() ) {
            this.constraints.updateFromTuple( tuple );
            if ( this.constraints.isAllowed( ) ) {
                sink.propagateAssertTuple( tuple, handle, context, workingMemory );
            }
        }
    }

    /**
     * Retract a FactHandleImpl. Iterates the referenced TupleMatches stored in
     * the handle's ObjectMatches retracting joined tuples.
     * 
     * @param handle
     *            the <codeFactHandleImpl</code> being retracted
     * @param context
     *            The <code>PropagationContext</code>
     * @param workingMemory
     *            The working memory seesion.
     */
    public void retractObject(final InternalFactHandle handle,
                              final PropagationContext context,
                              final InternalWorkingMemory workingMemory) {
        final BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );
        if ( !memory.getObjectMemory().remove( handle ) ) {
            return;
        }
        
        Iterator it = memory.getTupleMemory().iterator();
        for ( ReteTuple tuple = ( ReteTuple ) it.next(); tuple != null; tuple = ( ReteTuple ) it.next() ) {
            sink.propagateRetractTuple( tuple, handle, context, workingMemory );
        }
    }

    /**
     * Retract a <code>ReteTuple</code>. Iterates the referenced
     * <code>TupleMatche</code>'s stored in the tuples <code>Map</code>
     * retracting all joined tuples.
     * 
     * @param key
     *            The tuple key.
     * @param context
     *            The <code>PropagationContext</code>
     * @param workingMemory
     *            The working memory seesion.
     */
    public void retractTuple(final ReteTuple leftTuple,
                             final PropagationContext context,
                             final InternalWorkingMemory workingMemory) {
        final BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );
        ReteTuple tuple  = ( ReteTuple ) memory.getTupleMemory().remove( leftTuple );
        if ( tuple == null)  {
            leftTuple.release();
            return;
        }
        
        Iterator it = memory.getObjectMemory().iterator( leftTuple );
        for ( FactEntry entry = ( FactEntry ) it.next(); entry != null; entry = ( FactEntry ) it.next() ) {
            sink.propagateRetractTuple( leftTuple, entry.getFactHandle(), context, workingMemory );
        }   
        tuple.release();
        leftTuple.release();
    }        

    /* (non-Javadoc)
     * @see org.drools.reteoo.BaseNode#updateNewNode(org.drools.reteoo.WorkingMemoryImpl, org.drools.spi.PropagationContext)
     */
    public void updateSink(final TupleSink sink,
                           final PropagationContext context,
                           final InternalWorkingMemory workingMemory) {

        final BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );

        Iterator tupleIter = memory.getTupleMemory().iterator();
        for ( ReteTuple tuple = ( ReteTuple ) tupleIter.next(); tuple != null; tuple = ( ReteTuple ) tupleIter.next() ) {
            Iterator objectIter = memory.getObjectMemory().iterator( tuple );
            this.constraints.updateFromTuple( tuple );
            for ( FactEntry entry = ( FactEntry ) objectIter.next(); entry != null; entry = ( FactEntry ) objectIter.next() ) {
                InternalFactHandle handle = entry.getFactHandle();
                this.constraints.updateFromFactHandle( handle );
                if ( this.constraints.isAllowed( ) ) {
                    sink.assertTuple( new ReteTuple( tuple, handle),  context, workingMemory );
                }
            }               
        }
    }    
    
    public String toString() {
        ObjectSource source = this.rightInput;
        while ( source.getClass() != ObjectTypeNode.class ) {
            source = source.objectSource;
        }
        
        return "[JoinNode - " + ((ObjectTypeNode)source).getObjectType()+ "]";
    }
}

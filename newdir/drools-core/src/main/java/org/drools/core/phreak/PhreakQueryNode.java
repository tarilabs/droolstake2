package org.drools.core.phreak;

import org.drools.core.base.DroolsQuery;
import org.drools.core.base.extractors.ArrayElementReader;
import org.drools.core.common.InternalFactHandle;
import org.drools.core.common.InternalWorkingMemory;
import org.drools.core.common.LeftTupleSets;
import org.drools.core.reteoo.LeftInputAdapterNode;
import org.drools.core.reteoo.LeftInputAdapterNode.LiaNodeMemory;
import org.drools.core.reteoo.LeftTuple;
import org.drools.core.reteoo.LeftTupleSink;
import org.drools.core.reteoo.QueryElementNode;
import org.drools.core.reteoo.QueryElementNode.QueryElementNodeMemory;
import org.drools.core.reteoo.QueryElementNode.UnificationNodeViewChangedEventListener;
import org.drools.core.rule.Declaration;
import org.drools.core.spi.PropagationContext;
import org.kie.api.runtime.rule.Variable;

/**
* Created with IntelliJ IDEA.
* User: mdproctor
* Date: 03/05/2013
* Time: 15:43
* To change this template use File | Settings | File Templates.
*/
public class PhreakQueryNode {
    public void doNode(QueryElementNode queryNode,
                       QueryElementNodeMemory qmem,
                       StackEntry stackEntry,
                       LeftTupleSink sink,
                       InternalWorkingMemory wm,
                       LeftTupleSets srcLeftTuples) {

        if (srcLeftTuples.getDeleteFirst() != null) {
            doLeftDeletes(qmem, wm, srcLeftTuples);
        }

        if (srcLeftTuples.getUpdateFirst() != null) {
            doLeftUpdates(queryNode, qmem, sink, wm, srcLeftTuples);
        }

        if (srcLeftTuples.getInsertFirst() != null) {
            doLeftInserts(queryNode, qmem, stackEntry, wm, srcLeftTuples);
        }

        srcLeftTuples.resetAll();
    }

    public void doLeftInserts(QueryElementNode queryNode,
                              QueryElementNodeMemory qmem,
                              StackEntry stackEntry,
                              InternalWorkingMemory wm,
                              LeftTupleSets srcLeftTuples) {
        for (LeftTuple leftTuple = srcLeftTuples.getInsertFirst(); leftTuple != null; ) {
            LeftTuple next = leftTuple.getStagedNext();

            PropagationContext pCtx = (PropagationContext) leftTuple.getPropagationContext();

            InternalFactHandle handle = queryNode.createFactHandle(pCtx,
                                                                   wm,
                                                                   leftTuple);

            DroolsQuery dquery = queryNode.createDroolsQuery(leftTuple, handle, stackEntry,
                                                             qmem.getSegmentMemory().getPathMemories(),
                                                             qmem.getResultLeftTuples(),
                                                             stackEntry.getSink(), wm);

            LeftInputAdapterNode lian = (LeftInputAdapterNode) qmem.getQuerySegmentMemory().getRootNode();
            LiaNodeMemory lm = (LiaNodeMemory) qmem.getQuerySegmentMemory().getNodeMemories().get(0);
            LeftInputAdapterNode.doInsertObject(handle, pCtx, lian, wm, lm, false, dquery.isOpen());

            leftTuple.clearStaged();
            leftTuple = next;
        }
    }

    public void doLeftUpdates(QueryElementNode queryNode,
                              QueryElementNodeMemory qmem,
                              LeftTupleSink sink,
                              InternalWorkingMemory wm,
                              LeftTupleSets srcLeftTuples) {
        for (LeftTuple leftTuple = srcLeftTuples.getUpdateFirst(); leftTuple != null; ) {
            LeftTuple next = leftTuple.getStagedNext();

            InternalFactHandle fh = (InternalFactHandle) leftTuple.getObject();
            DroolsQuery dquery = (DroolsQuery) fh.getObject();

            Object[] argTemplate = queryNode.getQueryElement().getArgTemplate(); // an array of declr, variable and literals
            Object[] args = new Object[argTemplate.length]; // the actual args, to be created from the  template

            // first copy everything, so that we get the literals. We will rewrite the declarations and variables next
            System.arraycopy(argTemplate,
                             0,
                             args,
                             0,
                             args.length);

            int[] declIndexes = queryNode.getQueryElement().getDeclIndexes();

            for (int i = 0, length = declIndexes.length; i < length; i++) {
                Declaration declr = (Declaration) argTemplate[declIndexes[i]];

                Object tupleObject = leftTuple.get(declr).getObject();

                Object o;

                if (tupleObject instanceof DroolsQuery) {
                    // If the query passed in a Variable, we need to use it
                    ArrayElementReader arrayReader = (ArrayElementReader) declr.getExtractor();
                    if (((DroolsQuery) tupleObject).getVariables()[arrayReader.getIndex()] != null) {
                        o = Variable.v;
                    } else {
                        o = declr.getValue(wm,
                                           tupleObject);
                    }
                } else {
                    o = declr.getValue(wm,
                                       tupleObject);
                }

                args[declIndexes[i]] = o;
            }

            int[] varIndexes = queryNode.getQueryElement().getVariableIndexes();
            for (int i = 0, length = varIndexes.length; i < length; i++) {
                if (argTemplate[varIndexes[i]] == Variable.v) {
                    // Need to check against the arg template, as the varIndexes also includes re-declared declarations
                    args[varIndexes[i]] = Variable.v;
                }
            }

            dquery.setParameters(args);
            ((UnificationNodeViewChangedEventListener) dquery.getQueryResultCollector()).setVariables(varIndexes);

            LeftInputAdapterNode lian = (LeftInputAdapterNode) qmem.getQuerySegmentMemory().getRootNode();
            if (dquery.isOpen()) {
                LeftTuple childLeftTuple = fh.getFirstLeftTuple(); // there is only one, all other LTs are peers
                LeftInputAdapterNode.doUpdateObject(childLeftTuple, childLeftTuple.getPropagationContext(), wm, lian, false, qmem.getQuerySegmentMemory());
            } else {
                if (fh.getFirstLeftTuple() != null) {
                    throw new RuntimeException("defensive programming while testing"); // @TODO remove later (mdp)
                }
                LiaNodeMemory lm = (LiaNodeMemory) qmem.getQuerySegmentMemory().getNodeMemories().get(0);
                LeftInputAdapterNode.doInsertObject(fh, leftTuple.getPropagationContext(), lian, wm, lm, false, dquery.isOpen());
            }


            leftTuple.clearStaged();
            leftTuple = next;
        }
    }

    public void doLeftDeletes(QueryElementNodeMemory qmem,
                              InternalWorkingMemory wm,
                              LeftTupleSets srcLeftTuples) {
        for (LeftTuple leftTuple = srcLeftTuples.getDeleteFirst(); leftTuple != null; ) {
            LeftTuple next = leftTuple.getStagedNext();

            InternalFactHandle fh = (InternalFactHandle) leftTuple.getObject();
            DroolsQuery dquery = (DroolsQuery) fh.getObject();
            if (dquery.isOpen()) {
                LeftInputAdapterNode lian = (LeftInputAdapterNode) qmem.getQuerySegmentMemory().getRootNode();
                LiaNodeMemory lm = (LiaNodeMemory) qmem.getQuerySegmentMemory().getNodeMemories().get(0);
                LeftTuple childLeftTuple = fh.getFirstLeftTuple(); // there is only one, all other LTs are peers
                LeftInputAdapterNode.doDeleteObject(childLeftTuple, childLeftTuple.getPropagationContext(), qmem.getQuerySegmentMemory(), wm, lian, false, lm);
            } // else do nothing, no state is maintained

            leftTuple.clearStaged();
            leftTuple = next;
        }
    }
}

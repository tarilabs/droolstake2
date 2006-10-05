package org.drools.reteoo;

import org.drools.common.InternalFactHandle;
import org.drools.common.InternalWorkingMemory;
import org.drools.spi.PropagationContext;

public class SingleObjectSinkAdapter
    implements
    ObjectSinkPropagator {
    
    private ObjectSink sink;
    
    public SingleObjectSinkAdapter(ObjectSink sink) {    
        this.sink = sink;
    }

    public void propagateAssertObject(InternalFactHandle handle,
                                      PropagationContext context,
                                      InternalWorkingMemory workingMemory) {              
        this.sink.assertObject( handle, context, workingMemory );

    }
    
    public void propagateRetractObject(InternalFactHandle handle,
                                       PropagationContext context,
                                       InternalWorkingMemory workingMemory,
                                       boolean useHash) {
        this.sink.retractObject( handle, context, workingMemory );

    }    

    public ObjectSink[] getSinks() {
        return new ObjectSink[] { this.sink };
    }



}

/*
 * Copyright 2010 JBoss Inc
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

package org.drools.command.runtime.rule;

import org.drools.command.Context;
import org.drools.command.impl.GenericCommand;
import org.drools.command.impl.KnowledgeCommandContext;
import org.drools.common.DisconnectedFactHandle;
import org.drools.common.InternalFactHandle;
import org.kie.runtime.StatefulKnowledgeSession;
import org.kie.runtime.rule.FactHandle;

public class UpdateCommand
        implements GenericCommand<Object> {

    private static final long serialVersionUID = 3255044102543531497L;
    
    private DisconnectedFactHandle handle;
    private Object     object;
    private String     entryPoint;

    public UpdateCommand(FactHandle handle,
                         Object object) {
        this.handle = DisconnectedFactHandle.newFrom( handle );
        this.object = object;
    }

    public Object execute(Context context) {
        StatefulKnowledgeSession ksession = ((KnowledgeCommandContext) context).getStatefulKnowledgesession();
        
        ksession.getWorkingMemoryEntryPoint( handle.getEntryPointId() ).update( handle,
                                                                                object );
        return null;
    }
    

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public String toString() {
        return "session.update( " + handle + ", " + object + " );";
    }

}

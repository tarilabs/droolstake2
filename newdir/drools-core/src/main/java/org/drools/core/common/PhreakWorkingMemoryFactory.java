/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.core.common;

import org.drools.core.SessionConfiguration;
import org.drools.core.event.AgendaEventSupport;
import org.drools.core.event.RuleEventListenerSupport;
import org.drools.core.event.RuleRuntimeEventSupport;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.drools.core.spi.FactHandleFactory;
import org.kie.api.runtime.Environment;

import java.io.Serializable;

public class PhreakWorkingMemoryFactory implements WorkingMemoryFactory, Serializable {

    private static final WorkingMemoryFactory INSTANCE = new PhreakWorkingMemoryFactory();

    public static WorkingMemoryFactory getInstance() {
        return INSTANCE;
    }

    public InternalWorkingMemory createWorkingMemory(long id, InternalKnowledgeBase kBase, SessionConfiguration config, Environment environment) {
        InternalWorkingMemory cachedWm = kBase.getCachedSession(config, environment);
        if (cachedWm != null) {
            return cachedWm;
        }
        return new StatefulKnowledgeSessionImpl(id, kBase, true, config,  environment);
    }

    public InternalWorkingMemory createWorkingMemory(long id, InternalKnowledgeBase kBase, FactHandleFactory handleFactory, long propagationContext, SessionConfiguration config, InternalAgenda agenda, Environment environment) {
        return new StatefulKnowledgeSessionImpl(id, kBase, handleFactory, propagationContext, config, agenda, environment);
    }

    public InternalWorkingMemory createWorkingMemory(long id, InternalKnowledgeBase kBase, FactHandleFactory handleFactory, InternalFactHandle initialFactHandle, long propagationContext, SessionConfiguration config, Environment environment, RuleRuntimeEventSupport workingMemoryEventSupport, AgendaEventSupport agendaEventSupport, RuleEventListenerSupport ruleEventListenerSupport, InternalAgenda agenda) {
        return new StatefulKnowledgeSessionImpl(id, kBase, handleFactory, true, propagationContext, config, environment, workingMemoryEventSupport, agendaEventSupport, ruleEventListenerSupport, agenda);
    }
}

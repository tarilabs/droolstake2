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

package org.drools.event.knowlegebase.impl;

import org.kie.KnowledgeBase;
import org.kie.definition.KnowledgePackage;
import org.kie.event.kiebase.BeforeKnowledgePackageRemovedEvent;


public class BeforeKnowledgePackageRemovedEventImpl extends KnowledgeBaseEventImpl implements BeforeKnowledgePackageRemovedEvent {
    private KnowledgePackage knowledgePackage;
    
    public BeforeKnowledgePackageRemovedEventImpl(KnowledgeBase knowledgeBase, KnowledgePackage knowledgePackage) {
        super( knowledgeBase );
        this.knowledgePackage = knowledgePackage;
    }

    public KnowledgePackage getKnowledgePackage() {
        return this.knowledgePackage;
    }

    @Override
    public String toString() {
        return "==>[BeforeKnowledgePackageRemovedEventImpl: getKnowledgePackage()=" + getKnowledgePackage()
                + ", getKnowledgeBase()=" + getKnowledgeBase() + "]";
    }

}

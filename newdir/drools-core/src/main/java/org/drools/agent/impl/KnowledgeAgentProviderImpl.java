package org.drools.agent.impl;

import java.util.Properties;

import org.drools.KnowledgeBase;
import org.drools.SystemEventListener;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentProvider;

public class KnowledgeAgentProviderImpl implements KnowledgeAgentProvider {
    
    public KnowledgeAgentConfiguration newKnowledgeAgentConfiguration() {
        return new KnowledgeAgentConfigurationImpl();
    }
    
    public KnowledgeAgentConfiguration newKnowledgeAgentConfiguration(Properties properties) {
        return new KnowledgeAgentConfigurationImpl(properties);
    }    

    public KnowledgeAgent newKnowledgeAgent(String name,
                                            KnowledgeBase kbase) {
        return new KnowledgeAgentImpl(name, kbase, new KnowledgeAgentConfigurationImpl() );
    }

    public KnowledgeAgent newKnowledgeAgent(String name,
                                            KnowledgeBase kbase,
                                            KnowledgeAgentConfiguration configuration) {
        return new KnowledgeAgentImpl(name, kbase, configuration);
    }

}

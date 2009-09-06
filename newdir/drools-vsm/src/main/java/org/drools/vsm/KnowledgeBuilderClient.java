package org.drools.vsm;

import java.util.Collection;

import org.apache.mina.core.future.WriteFuture;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.ResourceConfiguration;
import org.drools.builder.ResourceType;
import org.drools.command.FinishedCommand;
import org.drools.command.KnowledgeContextResolveFromContextCommand;
import org.drools.command.builder.KnowledgeBuilderAddCommand;
import org.drools.command.builder.KnowledgeBuilderGetKnowledgePackagesCommand;
import org.drools.command.builder.KnowledgeBuilderHasErrorsCommand;
import org.drools.command.builder.KnowledgeBuilderGetErrorsCommand;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.runtime.ExecutionResults;
import org.drools.vsm.responsehandlers.BlockingMessageResponseHandler;

public class KnowledgeBuilderClient
    implements
    KnowledgeBuilder {
    private ServiceManagerClient serviceManager;
    private String               instanceId;

    public KnowledgeBuilderClient(String instanceId,
                                  ServiceManagerClient serviceManager) {
        this.instanceId = instanceId;
        this.serviceManager = serviceManager;
    }

    public void add(Resource resource,
                    ResourceType resourceType) {
        add( resource,
             resourceType,
             null );
    }

    public void add(Resource resource,
                    ResourceType resourceType,
                    ResourceConfiguration configuration) {
        Message msg = new Message( serviceManager.getSessionId(),
                                   serviceManager.counter.incrementAndGet(),
                                   false,
                                   null,
                                   new KnowledgeContextResolveFromContextCommand( new KnowledgeBuilderAddCommand( resource,
                                                                                                                  resourceType,
                                                                                                                  configuration ),
                                                                                  instanceId,
                                                                                  null,
                                                                                  null,
                                                                                  null ) );

        BlockingMessageResponseHandler handler = new BlockingMessageResponseHandler();

        try {
            serviceManager.client.handler.addResponseHandler( msg.getResponseId(),
                                                              handler );

            serviceManager.client.session.write( msg );

            Object object = handler.getMessage().getPayload();

            if ( !(object instanceof FinishedCommand) ) {
                throw new RuntimeException( "Response was not correctly ended" );
            }

        } catch ( Exception e ) {
            throw new RuntimeException( "Unable to execute message",
                                        e );
        }

    }

    public KnowledgeBuilderErrors getErrors() {
        String commandId = "kbuilder.getErrors_" + serviceManager.getNextId();
        String kresultsId = "kresults_" + serviceManager.getSessionId();
        
        Message msg = new Message( serviceManager.getSessionId(),
                                   serviceManager.counter.incrementAndGet(),
                                   false,
                                   null,
                                   new KnowledgeContextResolveFromContextCommand( new KnowledgeBuilderGetErrorsCommand( commandId ),
                                                                                  instanceId,
                                                                                  null,
                                                                                  null,
                                                                                  kresultsId ) );

        BlockingMessageResponseHandler handler = new BlockingMessageResponseHandler();

        try {
            serviceManager.client.handler.addResponseHandler( msg.getResponseId(),
                                                              handler );

            serviceManager.client.session.write( msg );

            Object object = handler.getMessage().getPayload();

            if ( object == null ) {
                throw new RuntimeException( "Response was not correctly received" );
            }

            return (KnowledgeBuilderErrors) ((ExecutionResults) object).getValue( commandId );
        } catch ( Exception e ) {
            throw new RuntimeException( "Unable to execute message",
                                        e );
        }
    }

    public Collection<KnowledgePackage> getKnowledgePackages() {
        return new CollectionClient<KnowledgePackage>( this.instanceId );
    }

    public boolean hasErrors() {
        String commandId = "kbuilder.hasErrors_" + serviceManager.getNextId();
        String kresultsId = "kresults_" + serviceManager.getSessionId();

        Message msg = new Message( serviceManager.getSessionId(),
                                   serviceManager.getNextId(),
                                   false,
                                   null,
                                   new KnowledgeContextResolveFromContextCommand( new KnowledgeBuilderHasErrorsCommand( commandId ),
                                                                                  instanceId,
                                                                                  null,
                                                                                  null,
                                                                                  kresultsId) );

        BlockingMessageResponseHandler handler = new BlockingMessageResponseHandler();

        try {
            serviceManager.client.handler.addResponseHandler( msg.getResponseId(),
                                                              handler );

            serviceManager.client.session.write( msg );

            Object object = handler.getMessage().getPayload();

            if ( object == null ) {
                throw new RuntimeException( "Response was not correctly received" );
            }
            
            System.out.println( "object" + object);

            return (Boolean) ((ExecutionResults) object).getValue( commandId );
        } catch ( Exception e ) {
            throw new RuntimeException( "Unable to execute message",
                                        e );
        }
    }

}

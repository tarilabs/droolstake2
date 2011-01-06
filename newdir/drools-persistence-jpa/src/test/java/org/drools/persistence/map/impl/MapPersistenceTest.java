package org.drools.persistence.map.impl;

import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ByteArrayResource;
import org.drools.persistence.info.SessionInfo;
import org.drools.persistence.info.WorkItemInfo;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.persistence.map.EnvironmentBuilder;
import org.drools.persistence.map.KnowledgeSessionStorage;
import org.drools.persistence.map.KnowledgeSessionStorageEnvironmentBuilder;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.junit.Assert;
import org.junit.Test;

public class MapPersistenceTest {

    @Test
    public void createPersistentSession() {
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        KnowledgeSessionStorage storage = new KnowledgeSessionStorage() {

            public void saveOrUpdate(SessionInfo storedObject) {
                System.out.println( "saving sessionInfo" );
            }

            public SessionInfo findSessionInfo(Long id) {
                System.out.println( "finding sessionInfo" );
                return null;
            }

            public void saveOrUpdate(WorkItemInfo workItemInfo) {
                System.out.println( "saving workItemInfo" );
            }

            public Long getNextWorkItemId() {
                return null;
            }

            public WorkItemInfo findWorkItemInfo(Long id) {
                System.out.println( "finding workItemInfo" );
                return null;
            }

            public void remove(WorkItemInfo workItemInfo) {
                System.out.println( "removing workItemInfo" );
            }
        };
        
        StatefulKnowledgeSession crmPersistentSession = createSession( kbase,
                                                                       storage );
        crmPersistentSession.fireAllRules();

        crmPersistentSession = createSession( kbase,
                                              storage );
        Assert.assertNotNull( crmPersistentSession );
    }

    @Test
    public void createPersistentSessionWithRules() {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
                .newKnowledgeBuilder();

        String rule = "package org.drools.persistence.map.impl\n";
        rule += "import org.drools.persistence.map.impl.Buddy;\n";
        rule += "rule \"echo2\" \n";
        rule += "dialect \"mvel\"\n";
        rule += "when\n";
        rule += "    $m : Buddy()\n";
        rule += "then\n";
        rule += "    System.out.println(\"buddy inserted\")";
        rule += "end\n";
        kbuilder.add( new ByteArrayResource( rule.getBytes() ),
                      ResourceType.DRL );
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if ( errors != null && errors.size() > 0 ) {
            for ( KnowledgeBuilderError error : errors ) {
                System.out.println( "Error: " + error.getMessage() );
            }
            Assert.fail( "KnowledgeBase did not build" );
        }

        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages( kbuilder.getKnowledgePackages() );

        KnowledgeSessionStorage storage = getSimpleStorage();
        
        StatefulKnowledgeSession ksession = createSession( kbase,
                                                           storage );

        FactHandle buddyFactHandle = ksession.insert( new Buddy() );
        ksession.fireAllRules();

        Assert.assertEquals( 1,
                             ksession.getObjects().size() );

        ksession = disposeAndReloadSession( ksession,
                                            kbase,
                                            storage );

        Assert.assertNotNull( ksession );

        Assert.assertEquals( 1,
                             ksession.getObjects().size() );

        Assert.assertNull( "An object can't be retrieved with a FactHandle from a disposed session",
                           ksession.getObject( buddyFactHandle ) );

    }

    @Test
    public void dontCreateMoreSessionsThanNecessary() {
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        final Map<Long, SessionInfo> savedEntities = new HashMap<Long, SessionInfo>();

        KnowledgeSessionStorage storage = new KnowledgeSessionStorage() {

            private Map<Long, WorkItemInfo> workItems = new HashMap<Long, WorkItemInfo>();
            
            public void saveOrUpdate(SessionInfo storedObject) {
                storedObject.update();
                savedEntities.put( storedObject.getId(), storedObject );
            }

            public SessionInfo findSessionInfo(Long id) {
                return savedEntities.get( id );
            }

            public void saveOrUpdate(WorkItemInfo workItemInfo) {
                workItems.put( workItemInfo.getId(), workItemInfo );
            }

            public Long getNextWorkItemId() {
                return new Long(workItems.size() + 1);
            }

            public WorkItemInfo findWorkItemInfo(Long id) {
                return workItems.get( id );
            }

            public void remove(WorkItemInfo workItemInfo) {
                workItems.remove( workItemInfo.getId() );
            }
        };

        StatefulKnowledgeSession crmPersistentSession = createSession(kbase, storage);

        long ksessionId = crmPersistentSession.getId();
        crmPersistentSession.fireAllRules();

        crmPersistentSession = disposeAndReloadSession(crmPersistentSession, kbase, storage);

        Assert.assertEquals(ksessionId, crmPersistentSession.getId());

        ksessionId = crmPersistentSession.getId();
        crmPersistentSession.fireAllRules();

        crmPersistentSession = disposeAndReloadSession(crmPersistentSession, kbase, storage);

        crmPersistentSession.fireAllRules();

        Assert.assertEquals(1, savedEntities.size());
        crmPersistentSession.dispose();
    }

    @Test
    public void insertObjectIntoKsessionAndRetrieve() {
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        KnowledgeSessionStorage storage = getSimpleStorage();
        
        StatefulKnowledgeSession crmPersistentSession = createSession(kbase, storage);
        Buddy bestBuddy = new Buddy("john");
        crmPersistentSession.insert(bestBuddy);

        crmPersistentSession = disposeAndReloadSession(crmPersistentSession, kbase, storage);
        Object obtainedBuddy = crmPersistentSession
                .getObjects().iterator().next();
        Assert.assertNotSame( bestBuddy, obtainedBuddy );
        Assert.assertEquals(bestBuddy, obtainedBuddy);

        crmPersistentSession.dispose();
    }

    private KnowledgeSessionStorage getSimpleStorage() {
        return new KnowledgeSessionStorage() {

            private Map<Long, SessionInfo> ksessions = new HashMap<Long, SessionInfo>();
            private Map<Long, WorkItemInfo> workItems = new HashMap<Long, WorkItemInfo>();
            
            public SessionInfo findSessionInfo(Long id) {
                return ksessions.get( id );
            }

            public void saveOrUpdate(SessionInfo storedObject) {
                storedObject.update();
                ksessions.put( storedObject.getId(), storedObject );
            }

            public void saveOrUpdate(WorkItemInfo workItemInfo) {
                workItems.put( workItemInfo.getId(), workItemInfo );
            }

            public Long getNextWorkItemId() {
                return new Long(workItems.size() + 1);
            }

            public WorkItemInfo findWorkItemInfo(Long id) {
                return workItems.get( id );
            }

            public void remove(WorkItemInfo workItemInfo) {
                workItems.remove( workItemInfo.getId() );
            }
        };
    }

    private StatefulKnowledgeSession createSession(KnowledgeBase kbase,
                                                   KnowledgeSessionStorage storage) {
        
        EnvironmentBuilder envBuilder = new KnowledgeSessionStorageEnvironmentBuilder( storage );
        Environment env = KnowledgeBaseFactory.newEnvironment();
        //FIXME temporary usage of this constants
        env.set( EnvironmentName.TRANSACTION_MANAGER,
                 envBuilder.getTransactionManager() );
        env.set( EnvironmentName.ENTITY_MANAGER_FACTORY,
                 envBuilder.getPersistenceContextManager() );

        return JPAKnowledgeService.newStatefulKnowledgeSession( kbase,
                                                                null,
                                                                env );
    }

    private StatefulKnowledgeSession disposeAndReloadSession(StatefulKnowledgeSession ksession,
                                                             KnowledgeBase kbase,
                                                             KnowledgeSessionStorage storage) {
        long sessionId = ksession.getId();
        ksession.dispose();
        EnvironmentBuilder envBuilder = new KnowledgeSessionStorageEnvironmentBuilder( storage );
        Environment env = KnowledgeBaseFactory.newEnvironment();
        //FIXME temporary usage of this constants
        env.set( EnvironmentName.TRANSACTION_MANAGER,
                 envBuilder.getTransactionManager() );
        env.set( EnvironmentName.ENTITY_MANAGER_FACTORY,
                 envBuilder.getPersistenceContextManager() );
        
        return JPAKnowledgeService.loadStatefulKnowledgeSession( sessionId, kbase, null, env );
    }

}

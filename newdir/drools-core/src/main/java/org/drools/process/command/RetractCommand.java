package org.drools.process.command;

import org.drools.StatefulSession;
import org.drools.runtime.rule.FactHandle;

public class RetractCommand
    implements
    Command<Object> {

    private FactHandle handle;

    public RetractCommand(FactHandle handle) {
        this.handle = handle;
    }

    public Object execute(StatefulSession session) {
        session.retract( handle );
        return null;
    }

    public String toString() {
        return "session.retract( " + handle + " );";
    }
}

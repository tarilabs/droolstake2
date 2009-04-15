package org.drools.process.command;

import org.drools.common.InternalAgenda;
import org.drools.reteoo.ReteooWorkingMemory;
import org.drools.runtime.rule.impl.AgendaImpl;

public class ClearAgendaGroupCommand implements Command<Object> {

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object execute(ReteooWorkingMemory session) {
		new AgendaImpl((InternalAgenda) session.getAgenda()).getAgendaGroup(name).clear();
		return null;
	}

	public String toString() {
		return "session.getAgenda().getAgendaGroup(" + name + ").clear();";
	}
	
}

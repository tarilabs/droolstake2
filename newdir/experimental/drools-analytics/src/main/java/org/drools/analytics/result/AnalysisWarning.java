package org.drools.analytics.result;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Toni Rikkola
 */
public class AnalysisWarning extends AnalysisMessage implements Serializable {
	private static final long serialVersionUID = 1791682284155920123L;

	private static int warningIndex = 0;

	public AnalysisWarning(String ruleName, String message, List<Cause> reasons) {
		super(ruleName, message, reasons);
		id = warningIndex++;
	}

	public String toString() {
		StringBuffer str = new StringBuffer("Warning id = ");
		str.append(id);
		str.append(":\n");

		if (ruleName != null) {
			str.append("in rule ");
			str.append(ruleName);
			str.append(": ");
		}

		str.append(message);
		str.append(" \n\tCauses are [ \n");

		for (Cause cause : causes) {
			str.append("\t\t");
			str.append(cause);
			str.append("\n");
		}
		str.append("\t]");

		return str.toString();
	}
}

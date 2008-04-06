package org.drools.xml.processes;

import org.drools.workflow.core.Node;
import org.drools.workflow.core.impl.DroolsConsequenceAction;
import org.drools.workflow.core.node.ActionNode;
import org.drools.xml.Configuration;
import org.drools.xml.ExtensibleXmlParser;
import org.xml.sax.SAXException;

public class ActionNodeHandler extends AbstractNodeHandler {

    protected Node createNode() {
        return new ActionNode();
    }

    public void handleNode(final Node node, final Configuration config, final String uri,
            final String localName, final ExtensibleXmlParser parser)
            throws SAXException {
        super.handleNode(node, config, uri, localName, parser);
        ActionNode actionNode = (ActionNode) node;
        String text = config.getText();
        if (text != null) {
            text.trim();
            if ("".equals(text)) {
                text = null;
            }
        }
        final String dialect = config.getAttribute("dialect");
        DroolsConsequenceAction actionText = new DroolsConsequenceAction(dialect, text);
        actionNode.setAction(actionText);
    }

    public Class generateNodeFor() {
        return ActionNode.class;
    }

}

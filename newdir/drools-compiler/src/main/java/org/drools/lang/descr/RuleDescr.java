package org.drools.lang.descr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleDescr extends PatternDescr  {
    private String name;
    private String documentation;
    
    private AndDescr lhs;
    private String   consequence;
    private int consequenceLine;
    private int consequenceColumn;    
    private int offset;
    private List attributes = Collections.EMPTY_LIST;
    
    private String className;

    public RuleDescr(String name) {
        this( name, "");
    }
    
    public RuleDescr(String ruleName, String documentation) {
        this.name = ruleName;
        this.documentation = documentation;
    }
    
    public String getName() {
        return name;
    }       
      
    public String getClassName() {
        return this.className;
    }
    
    public void SetClassName(String className) {
        this.className = className;
    }    
    
    public String getDocumentation() {
        return documentation;
    }    
    
    public List getAttributes() {
        return attributes;
    }    
    
    public void addAttribute(AttributeDescr attribute) {
        if ( this.attributes == Collections.EMPTY_LIST) {
            this.attributes = new ArrayList();
        }
        this.attributes.add( attribute );
    }      
    
    public void setAttributes(List attributes) {
    		this.attributes = new ArrayList( attributes );
    }

    public AndDescr getLhs() {
        return lhs;
    }


    public void setLhs(AndDescr lhs) {
        this.lhs = lhs;
    }


    public String getConsequence() {
        return this.consequence;
    }


    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }
    
    public void setConsequenceLocation(int line, int column) {
        this.consequenceLine   = line;
        this.consequenceColumn = column;
    }
    
    public void setConsequenceOffset(int offset) {
        this.offset = offset;
    }
    
    public int getConsequenceOffset() {
        return this.offset;
    }
    
    public int getConsequenceLine() {
        return this.consequenceLine;
    }
    
    public int getConsequenceColumn() {
        return this.consequenceColumn;
    }    
}

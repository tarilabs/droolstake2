package org.drools.util.asm;

public abstract class AbstractClass {
    public String HTML;

    public AbstractClass() {
        
    }
    
    public AbstractClass(String HTML) {
        super();
        this.HTML = HTML;
    }

    /**
     * @return the uRI
     */
    public String getHTML() {
        return this.HTML;
    }

    /**
     * @param uri the uRI to set
     */
    public void setHTML(String HTML) {
        this.HTML = HTML;
    }
    
    
    
}

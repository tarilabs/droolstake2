//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.02 at 03:31:08 PM MEZ 
//


package reactionruleml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for scope.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scope.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/1.0/xsd}scope.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/1.0/xsd}scope.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scope.type", propOrder = {
    "ind"
})
public class ScopeType {

    @XmlElement(name = "Ind")
    protected Object ind;
    @XmlAttribute(name = "arg")
    protected String arg;

    /**
     * Gets the value of the ind property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getInd() {
        return ind;
    }

    /**
     * Sets the value of the ind property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setInd(Object value) {
        this.ind = value;
    }

    /**
     * Gets the value of the arg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArg() {
        return arg;
    }

    /**
     * Sets the value of the arg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArg(String value) {
        this.arg = value;
    }

}

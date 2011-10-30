//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.02 at 03:31:08 PM MEZ 
//


package reactionruleml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for Update.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Update.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/1.0/xsd}Update.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/1.0/xsd}Update.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Update.type", propOrder = {
    "content"
})
public class UpdateType {

    @XmlElementRefs({
        @XmlElementRef(name = "Equal", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class),
        @XmlElementRef(name = "Forall", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class),
        @XmlElementRef(name = "oid", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class),
        @XmlElementRef(name = "Rulebase", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class),
        @XmlElementRef(name = "Entails", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class),
        @XmlElementRef(name = "Rule", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class),
        @XmlElementRef(name = "Atom", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class),
        @XmlElementRef(name = "Neg", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class),
        @XmlElementRef(name = "Equivalent", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class),
        @XmlElementRef(name = "Implies", namespace = "http://www.ruleml.org/1.0/xsd", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> content;
    @XmlAttribute(name = "safety")
    protected String safety;
    @XmlAttribute(name = "mapMaterial")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String mapMaterial;
    @XmlAttribute(name = "mapClosure")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String mapClosure;
    @XmlAttribute(name = "mapDirection")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String mapDirection;
    @XmlAttribute(name = "all")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String all;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "Oid" is used by two different parts of a schema. See: 
     * line 237 of http://svn.codehaus.org/logicabyss/RuleML2Drools/trunk/resources/rrml/modules/rule_module.xsd
     * line 233 of http://svn.codehaus.org/logicabyss/RuleML2Drools/trunk/resources/rrml/modules/rule_module.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link EqualType }{@code >}
     * {@link JAXBElement }{@code <}{@link ForallType }{@code >}
     * {@link JAXBElement }{@code <}{@link RulebaseType }{@code >}
     * {@link JAXBElement }{@code <}{@link OidType }{@code >}
     * {@link JAXBElement }{@code <}{@link EntailsType }{@code >}
     * {@link JAXBElement }{@code <}{@link RuleType }{@code >}
     * {@link JAXBElement }{@code <}{@link NegType }{@code >}
     * {@link JAXBElement }{@code <}{@link AtomType }{@code >}
     * {@link JAXBElement }{@code <}{@link EquivalentType }{@code >}
     * {@link JAXBElement }{@code <}{@link ImpliesType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<?>>();
        }
        return this.content;
    }

    /**
     * Gets the value of the safety property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSafety() {
        if (safety == null) {
            return "normal";
        } else {
            return safety;
        }
    }

    /**
     * Sets the value of the safety property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSafety(String value) {
        this.safety = value;
    }

    /**
     * Gets the value of the mapMaterial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapMaterial() {
        if (mapMaterial == null) {
            return "yes";
        } else {
            return mapMaterial;
        }
    }

    /**
     * Sets the value of the mapMaterial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapMaterial(String value) {
        this.mapMaterial = value;
    }

    /**
     * Gets the value of the mapClosure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapClosure() {
        return mapClosure;
    }

    /**
     * Sets the value of the mapClosure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapClosure(String value) {
        this.mapClosure = value;
    }

    /**
     * Gets the value of the mapDirection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapDirection() {
        if (mapDirection == null) {
            return "bidirectional";
        } else {
            return mapDirection;
        }
    }

    /**
     * Sets the value of the mapDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapDirection(String value) {
        this.mapDirection = value;
    }

    /**
     * Gets the value of the all property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAll() {
        if (all == null) {
            return "no";
        } else {
            return all;
        }
    }

    /**
     * Sets the value of the all property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAll(String value) {
        this.all = value;
    }

}

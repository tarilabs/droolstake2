package org.drools.brms.client.modeldriven;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.drools.brms.client.modeldriven.brxml.DSLSentence;
import org.drools.brms.client.modeldriven.brxml.PortableObject;

/**
 * An suggestion completion processor. This should be usable in both GWT/Web and the IDE.
 * The data for this can be loaded into this from simple string lists.
 *  
 * @author Michael Neale
 */
public class SuggestionCompletionEngine implements PortableObject {

    /** These are the explicit types supported */
    public static final String TYPE_COLLECTION = "Collection";
    public static final String TYPE_COMPARABLE = "Comparable";
    public static final String TYPE_STRING = "String";
    public static final String TYPE_NUMERIC = "Numeric";
    
    /**
     * The operators that are used at different times (based on type).
     */
    private static final String[] STANDARD_CONNECTIVES = new String[] { "| ==", "| !=", "& !="};
    private static final String[] STRING_CONNECTIVES = new String[] { "| ==", "| !=", "& !=", "& matches", "| matches"};
    private static final String[] COMPARABLE_CONNECTIVES = new String[] {"| ==", "| !=", "& !=", "& >", "& <", "| >", "| <", "& >=", "& <=", "| <=", "| >="};
    private static final String[] COLLECTION_CONNECTIVES = new String[] {"| ==", "| !=", "& !=", "| contains", "& contains", "| excludes", "& excludes"};
    
    private static final String[] STANDARD_OPERATORS = new String[] { "==", "!=" };
    private static final String[] COMPARABLE_OPERATORS = new String[] { "==", "!=", "<", ">", "<=", ">="};
    private static final String[] STRING_OPERATORS = new String[] {"==", "!=", "matches"};
    private static final String[] COLLECTION_OPERATORS = new String[] {"contains", "excludes", "==", "!="};
        
    /** The top level conditional elements (first order logic) */
    private static final String[] CONDITIONAL_ELEMENTS = new String[] {"not", "exists", "or"};
    
    
    /** 
     * A list of fact types (never fully qualified).
     */
    public String[] factTypes;
    
    /**
     * A map of types to the fields. key is type, value is (String[] of fields)
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String[]>
     */
    public Map fieldsForType;
    
    /**
     * A map of the Fields to their types, needed for operator completions,
     * as well as other things like rendering strings, dates etc.
     * This is in the format of: { 'Type.field' => 'typename' }.
     * Should not be the exact type, perhaps just a high level interface, eg "Comparable".
     * 
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    public Map fieldTypes;
    
    
    
    
    /**
     * Contains a map of globals (name is key) and their type (value).
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    public Map globalTypes = new HashMap();
    
    
    /** Operators (from the grammar):
     *      op=(    '=='
        |   '>'
        |   '>='
        |   '<'
        |   '<='
        |   '!='
        |   'contains'
        |   'matches'
        |       'excludes'
        )
     * Connectives add "&" and "|" to this.
     */
    

    


    /**
     * DSL language extensions, if needed, if provided by the package.
     */
    public DSLSentence[] conditionDSLSentences = new DSLSentence[0];
    public DSLSentence[] actionDSLSentences = new DSLSentence[0];
    
    
//    /**
//     * For bulk loading up the data (from a previous rule save)
//     * 
//     * @param factToFields A map of "FactType" (key - String) to String[] (value) 
//     * @param factFieldToOperator A map of "FactType.field" (key - String) to String[] operators
//     * @param factFieldToConnectiveOperator A map of "FactType.field" (key -String) to String[] operators 
//     *                                  that are valid CONNECTIVE operators.
//     *                                  
//     * @param globals A map of global variable name to its fields (String[]).
//     * @param boundFacts A map of bound facts to types.
//     * @param conditionDSLs a list of DSLSentence suggestions for the LHS
//     * @param actionDSLs a list of DSLSentence suggestions for the RHS                          
//     *                                       
//     */
//    public void load( 
//                      Map factToFields, 
//                      Map factFieldToOperator, 
//                      Map factFieldToConnectiveOperator,
//                      Map globals,                      
//                      List conditionDSLs,
//                      List actionDSLs
//                    ) {
//        this.factToFields = factToFields;
//        this.factFieldToOperator = factFieldToOperator;
//        this.factFieldToConnectiveOperator = factFieldToConnectiveOperator;
//        this.actionDSLSentences = actionDSLs;
//        this.conditionDSLSentences = conditionDSLs;
//        this.globals = globals;
//
//    }
    
    public SuggestionCompletionEngine() {  

        
        
    }

    
    

    public String[] getConditionalElements() {       
        return CONDITIONAL_ELEMENTS;
    }
    
    public DSLSentence[] getDSLConditions() {
        return conditionDSLSentences;
    }
    
    public DSLSentence[] getDSLActions() {
        return actionDSLSentences;
    }

    public String[] getConnectiveOperatorCompletions(String factType,
                                                     String fieldName) {
        String type = (String) this.fieldTypes.get( factType + "." + fieldName );
        if (type == null) {
          return STANDARD_CONNECTIVES;  
        } else if (type.equals( TYPE_STRING )) {
          return STRING_CONNECTIVES;  
        } else if (type.equals( TYPE_COMPARABLE ) || type.equals( TYPE_NUMERIC )) {
          return COMPARABLE_CONNECTIVES;
        } else if (type.equals( TYPE_COLLECTION )){
            return COLLECTION_CONNECTIVES;
        } else {
            return STANDARD_CONNECTIVES;
        }
        
    }



    public String[] getFactTypes() {
        return this.factTypes;
    }

    public String[] getFieldCompletions(String factType) {
       return (String[]) this.fieldsForType.get( factType );
        
    }

    public String[] getOperatorCompletions(String factType,
                                           String fieldName) {
        String type = (String) this.fieldTypes.get( factType + "." + fieldName );
        if (type == null) {
            return STANDARD_OPERATORS;
        } else if (type.equals( TYPE_STRING )) {
            return STRING_OPERATORS;
        } else if (type.equals( TYPE_COMPARABLE ) || type.equals( TYPE_NUMERIC )) {
            return COMPARABLE_OPERATORS;
        } else if (type.equals( TYPE_COLLECTION )) {
            return COLLECTION_OPERATORS;
        } else {            
            return STANDARD_OPERATORS;
        }
                
    }
    
    public String getFieldType( String factType,
                                String fieldName ) {
        return (String) this.fieldTypes.get( factType + "." + fieldName );
    }
    
    public boolean isGlobalVariable(String variable) {
        return this.globalTypes.containsKey( variable );
    }
    
    public String[] getFieldCompletionsForGlobalVariable(String varName) {
        String type = (String) this.globalTypes.get( varName );        
        return (String[]) this.fieldsForType.get( type );
    }


    private String[] toStringArray(Set set) {
        String[] f = new String[set.size()];
        int i = 0;
        for ( Iterator iter = set.iterator(); iter.hasNext(); ) {
            f[i] = (String) iter.next();
            i++;
        }
        return f;                
    }

    public String[] getGlobalVariables() {         
        return toStringArray( this.globalTypes.keySet() );
    }


    

    
}

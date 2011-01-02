package org.drools.decisiontable.parser;

/**
 * This is for building up LHS and RHS code for a rule row.
 * @author Michael Neale
 */
public interface SourceBuilder {
    String getResult();
    void addTemplate(int row, int col, String content);
    void addCellValue(int row, int col, String value);
    void clearValues();
    boolean hasValues();
}

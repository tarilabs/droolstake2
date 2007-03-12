package org.drools.clp;

import java.math.BigDecimal;
import java.math.BigInteger;

public class LongLiteralValue extends BaseValueHandler {
    private long longValue;
    
    public LongLiteralValue(String longValue) {
        this.longValue = Long.valueOf( longValue ).longValue();
    } 
    
    public LongLiteralValue(long longValue) {
        this.longValue = longValue;
    }

    public void setValue(ExecutionContext context, Object value) {
        throw new RuntimeException( "LiteralValues cannot be set");
    }
    
    public Object getValue(ExecutionContext context) {
        return new Long( this.longValue );
    }
    
    public BigDecimal getBigDecimalValue(ExecutionContext context) throws NumberFormatException {
        return new BigDecimal( this.longValue );
    }

    public BigInteger getBigIntegerValue(ExecutionContext context) throws NumberFormatException {
        return BigInteger.valueOf( this.longValue );
    }

    public boolean getBooleanValue(ExecutionContext context) throws ClassCastException {
        return ( this.longValue == 0 ) ? false : true;
    }

    public double getDoubleValue(ExecutionContext context) throws NumberFormatException {
        return this.longValue;
    }

    public float getFloatValue(ExecutionContext context) throws NumberFormatException {
        return this.longValue;
    }

    public int getIntValue(ExecutionContext context) throws NumberFormatException {
        return (int) this.longValue;
    }

    public long getLongValue(ExecutionContext context) throws NumberFormatException {
        return this.longValue;
    }

    public short getShortValue(ExecutionContext context) throws NumberFormatException {
        return (short) this.longValue;
    }

    public String getStringValue(ExecutionContext context) {
        return Long.toString( this.longValue );
    }      
}

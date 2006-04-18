package org.drools.util.asm;
/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



public class TestBean {

    private String  something;
    private int     number;
    private boolean blah;

    public boolean isBlah() {
        return blah;
    }

    public void setBlah(boolean blah) {
        this.blah = blah;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSomething() {
        return something;
    }

    public void setSomething(String something) {
        this.something = something;
    }

    public String fooBar() {
        return "fooBar";
    }

    public long getLongField() {
        return 424242;
    }

    public Long getOtherLongField() {
        return new Long( 42424242 );
    }

}
/*
 * Copyright 2011 JBoss Inc
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

package org.drools.lang.api.impl;

import org.drools.lang.api.DescrBuilder;
import org.drools.lang.descr.BaseDescr;

/**
 * A base class for all DescrBuilders
 */
public class BaseDescrBuilderImpl<T extends BaseDescr>
    implements
    DescrBuilder<T> {

    protected T descr;

    protected BaseDescrBuilderImpl(final T descr) {
        this.descr = descr;
    }

    public DescrBuilder<T> startLocation( int line,
                                       int column ) {
        descr.setLocation( line,
                           column );
        return this;
    }

    public DescrBuilder<T> endLocation( int line,
                                     int column ) {
        descr.setEndLocation( line,
                              column );
        return this;
    }

    public DescrBuilder<T> startCharacter( int offset ) {
        descr.setStartCharacter( offset );
        return this;
    }

    public DescrBuilder<T> endCharacter( int offset ) {
        descr.setEndCharacter( offset );
        return this;
    }

    public T getDescr() {
        return descr;
    }

}

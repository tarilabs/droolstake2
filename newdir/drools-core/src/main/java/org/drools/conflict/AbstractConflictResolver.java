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

package org.drools.conflict;

import org.drools.spi.Activation;
import org.drools.spi.ConflictResolver;

/**
 * Convenience base class for <code>ConflictResolver</code>s.
 * 
 *
 * @version $Id: AbstractConflictResolver.java,v 1.1 2004/10/06 13:38:05
 *          mproctor Exp $
 */
public abstract class AbstractConflictResolver
    implements
    ConflictResolver {
    /**
     * @see ConflictResolver
     */
    public final int compare(final Object existing,
                             final Object adding) {
        return compare( (Activation) existing,
                        (Activation) adding );
    }
}

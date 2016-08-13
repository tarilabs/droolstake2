/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.feel.lang.runtime.functions;

import org.kie.dmn.feel.lang.EvaluationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JavaFunction
        extends BaseFEELFunction {

    private static final Logger logger = LoggerFactory.getLogger( JavaFunction.class );

    private final List<String> parameters;
    private final Class        clazz;
    private final Method       method;

    public JavaFunction(String name, List<String> parameters, Class clazz, Method method) {
        super( name );
        this.parameters = parameters;
        this.clazz = clazz;
        this.method = method;
    }

    @Override
    public List<List<String>> getParameterNames() {
        return Arrays.asList( parameters );
    }

    public Object apply(EvaluationContext ctx, Object[] params) {
        if ( params.length != parameters.size() ) {
            logger.error( "Illegal invocation of function. Expecting " + getSignature() + " but got " + getName() + "( " + Arrays.asList( params ) + " )" );
            return null;
        }
        try {
            ctx.enterFrame();
            for ( int i = 0; i < parameters.size(); i++ ) {
                ctx.setValue( parameters.get( i ), params[i] );
            }
            Object[] actualParams = prepareParams( params );
            Object result = method.invoke( clazz, actualParams );
            return result;
        } catch ( Exception e ) {
            logger.error( "Error invoking function " + getSignature() + ".", e );
        } finally {
            ctx.exitFrame();
        }
        return null;
    }

    private Object[] prepareParams(Object[] params) {
        Object[] actual = new Object[ params.length ];
        Class[] paramTypes = method.getParameterTypes();
        for( int i = 0; i < paramTypes.length; i++ ) {
            if( paramTypes[i].isAssignableFrom( params[i].getClass() ) ) {
                actual[i] = params[i];
            } else {
                // try to coerce
                if( params[i] == null ) {
                    actual[i] = null;
                } else if( params[i] instanceof Number ) {
                    if( paramTypes[i] == byte.class || paramTypes[i] == Byte.class ) {
                        actual[i] = ((Number)params[i]).byteValue();
                    } else if( paramTypes[i] == short.class || paramTypes[i] == Short.class ) {
                        actual[i] = ((Number) params[i]).shortValue();
                    } else if( paramTypes[i] == int.class || paramTypes[i] == Integer.class ) {
                        actual[i] = ((Number) params[i]).intValue();
                    } else if( paramTypes[i] == long.class || paramTypes[i] == Long.class ) {
                        actual[i] = ((Number) params[i]).longValue();
                    } else if( paramTypes[i] == float.class || paramTypes[i] == Float.class ) {
                        actual[i] = ((Number) params[i]).floatValue();
                    } else if( paramTypes[i] == double.class || paramTypes[i] == Double.class ) {
                        actual[i] = ((Number) params[i]).doubleValue();
                    } else {
                        throw new IllegalArgumentException( "Unable to coerce parameter "+parameters.get( 0 )+". Expected "+paramTypes[i]+" but found "+params[i].getClass() );
                    }
                } else {
                    throw new IllegalArgumentException( "Unable to coerce parameter "+parameters.get( 0 )+". Expected "+paramTypes[i]+" but found "+params[i].getClass() );
                }
            }
        }
        return actual;
    }

    private String getSignature() {
        return getName() + "( " + parameters.stream().collect( Collectors.joining( ", " ) ) + " )";
    }

    @Override
    protected boolean isCustomFunction() {
        return true;
    }

    @Override
    public String toString() {
        return "function " + getSignature();
    }
}

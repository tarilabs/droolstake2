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

package org.kie.dmn.lang.types;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.kie.dmn.feel11.FEEL_1_1Lexer;
import org.kie.dmn.lang.Scope;
import org.kie.dmn.lang.Symbol;
import org.kie.dmn.util.TokenTree;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BaseScope implements Scope {

    private String name;
    private Scope  parentScope;

    private Map<String, Symbol> symbols     = new LinkedHashMap<>();
    private Map<String, Scope>  childScopes = new LinkedHashMap<>();

    private TokenTree tokenTree;

    public BaseScope() {
    }

    public BaseScope(String name, Scope parentScope) {
        this.name = name;
        this.parentScope = parentScope;
        if ( parentScope != null ) {
            parentScope.addChildScope( this );
        }
    }

    public String getName() {
        return name;
    }

    public Scope getParentScope() {
        return parentScope;
    }

    public boolean define(Symbol symbol) {
        if ( symbols.containsKey( symbol.getId() ) ) {
            // duplicate symbol definition
            return false;
        }
        symbols.put( symbol.getId(), symbol );
        if( tokenTree != null ) {
            // also load the symbol into the token tree
            tokenTree.addName( tokenize( symbol.getId() ) );
        }
        return true;
    }

    public Symbol resolve(String id) {
        Symbol s = symbols.get( id );
        if ( s == null && parentScope == null ) {
            return parentScope.resolve( id );
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentScope(Scope parentScope) {
        this.parentScope = parentScope;
    }

    public void addChildScope( Scope scope ) {
        this.childScopes.put( scope.getName(), scope );
    }

    public Map<String, Scope> getChildScopes() {
        return childScopes;
    }

    public void setChildScopes(Map<String, Scope> childScopes) {
        this.childScopes = childScopes;
    }

    public void start( String token ) {
        if( tokenTree == null ) {
            initializeTokenTree();
        }
        this.tokenTree.start( token );
        if( this.parentScope != null ) {
            this.parentScope.start( token );
        }
    }

    public boolean followUp( String token ) {
        // must call followup on parent scope
        boolean parent = this.parentScope != null ? this.parentScope.followUp( token ) : false;
        return this.tokenTree.followUp( token ) || parent;
    }

    private void initializeTokenTree() {
        tokenTree = new TokenTree();
        for( String symbol : symbols.keySet() ) {
            List<String> tokens = tokenize( symbol );
            tokenTree.addName( tokens );
        }
    }

    private List<String> tokenize(String symbol) {
        ANTLRInputStream input = new ANTLRInputStream(symbol);
        FEEL_1_1Lexer lexer = new FEEL_1_1Lexer( input );
        List<String> tokens = new ArrayList<>(  );

        for (Token token = lexer.nextToken();
             token.getType() != Token.EOF;
             token = lexer.nextToken()) {
            tokens.add( token.getText() );
        }
        return tokens;
    }

    @Override
    public String toString() {
        return "Scope{" +
               " name='" + name + '\'' +
               ", parentScope='" + ( parentScope != null ? parentScope.getName() : "<null>" ) +
               "' }";
    }
}

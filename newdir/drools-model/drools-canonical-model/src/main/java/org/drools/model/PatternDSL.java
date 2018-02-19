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

package org.drools.model;

import org.drools.model.consequences.ConditionalConsequenceBuilder;
import org.drools.model.constraints.SingleConstraint1;
import org.drools.model.constraints.SingleConstraint2;
import org.drools.model.constraints.TemporalConstraint;
import org.drools.model.functions.Function1;
import org.drools.model.functions.Function2;
import org.drools.model.functions.Predicate1;
import org.drools.model.functions.Predicate2;
import org.drools.model.functions.temporal.TemporalPredicate;
import org.drools.model.impl.DeclarationImpl;
import org.drools.model.impl.Query0DefImpl;
import org.drools.model.impl.Query1DefImpl;
import org.drools.model.impl.Query2DefImpl;
import org.drools.model.impl.Query3DefImpl;
import org.drools.model.impl.Query4DefImpl;
import org.drools.model.impl.RuleBuilder;
import org.drools.model.impl.ViewBuilder;
import org.drools.model.index.AlphaIndexImpl;
import org.drools.model.index.BetaIndexImpl;
import org.drools.model.view.BindViewItem1;
import org.drools.model.view.BindViewItem2;
import org.drools.model.view.ExprViewItem;
import org.drools.model.view.ViewItem;

import static java.util.UUID.randomUUID;

public class PatternDSL extends DSL {

    private static final ViewBuilder VIEW_BUILDER = ViewBuilder.PATTERN;

    public static <T> Declaration<T> declarationOf( Class<T> type ) {
        return new DeclarationImpl<>( type );
    }

    @SafeVarargs
    public static <T> PatternDef<T> pattern(Variable<T> var, PatternItem<T>... items) {
        return new PatternDefImpl<>( var, items );
    }

    public static <T> PatternItem<T> expr( Predicate1<T> predicate ) {
        return new PatternExpr1<>( new Predicate1.Impl<>(predicate), null, null );
    }

    public static <T> PatternItem<T> expr( String exprId, Predicate1<T> predicate ) {
        return new PatternExpr1<>( exprId, new Predicate1.Impl<>(predicate), null, null );
    }

    public static <T> PatternItem<T> expr( String exprId, Predicate1<T> predicate, AlphaIndex<T, ?> index ) {
        return new PatternExpr1<>( exprId, new Predicate1.Impl<>(predicate), index, null );
    }

    public static <T> PatternItem<T> expr( String exprId, Predicate1<T> predicate, ReactOn reactOn ) {
        return new PatternExpr1<>( exprId, new Predicate1.Impl<>(predicate), null, reactOn );
    }

    public static <T> PatternItem<T> expr( String exprId, Predicate1<T> predicate, AlphaIndex<T, ?> index, ReactOn reactOn ) {
        return new PatternExpr1<>( exprId, new Predicate1.Impl<>(predicate), index, reactOn );
    }

    public static <T, U> PatternItem<T> expr( Variable<U> var2, Predicate2<T, U> predicate ) {
        return new PatternExpr2<>( var2, new Predicate2.Impl<>(predicate), null, null );
    }

    public static <T, U> PatternItem<T> expr( String exprId, Variable<U> var2, Predicate2<T, U> predicate ) {
        return new PatternExpr2<>( exprId, var2, new Predicate2.Impl<>(predicate), null, null );
    }

    public static <T, U> PatternItem<T> expr( String exprId, Variable<U> var2, Predicate2<T, U> predicate, BetaIndexImpl<T, U, ?> index ) {
        return new PatternExpr2<>( exprId, var2, new Predicate2.Impl<>(predicate), index, null );
    }

    public static <T, U> PatternItem<T> expr( String exprId, Variable<U> var2, Predicate2<T, U> predicate, ReactOn reactOn ) {
        return new PatternExpr2<>( exprId, var2, new Predicate2.Impl<>(predicate), null, reactOn );
    }

    public static <T, U> PatternItem<T> expr( String exprId, Variable<U> var2, Predicate2<T, U> predicate, BetaIndexImpl<T, U, ?> index, ReactOn reactOn ) {
        return new PatternExpr2<>( exprId, var2, new Predicate2.Impl<>(predicate), index, reactOn );
    }

    public static <T, U> TemporalPatternExpr<T, U> expr( String exprId, Variable<U> var1, TemporalPredicate temporalPredicate ) {
        return new TemporalPatternExpr<>( exprId, var1, temporalPredicate);
    }

    public static <T, U> AlphaIndex<T, U> alphaIndexedBy( Class<U> indexedClass, Index.ConstraintType constraintType, int indexId, Function1<T, U> leftOperandExtractor, U rightValue ) {
        return new AlphaIndexImpl<>( indexedClass, constraintType, indexId, leftOperandExtractor, rightValue );
    }

    public static <T, U, V> BetaIndexImpl<T, U, V> betaIndexedBy( Class<V> indexedClass, Index.ConstraintType constraintType, int indexId, Function1<T, V> leftOperandExtractor, Function1<U, V> rightOperandExtractor ) {
        return new BetaIndexImpl<>( indexedClass, constraintType, indexId, leftOperandExtractor, rightOperandExtractor );
    }

    public static ReactOn reactOn(String... reactOn) {
        return new ReactOn( reactOn );
    }

    public interface PatternDef<T> extends ViewItem<T> {
        PatternDef<T> watch(String... watch);
    }

    public static class PatternDefImpl<T> implements PatternDef<T> {
        private final Variable<T> variable;
        private final PatternItem<T>[] items;

        private String[] watch;

        public PatternDefImpl( Variable<T> variable, PatternItem<T>... items ) {
            this.variable = variable;
            this.items = items;
        }

        @Override
        public PatternDefImpl<T> get() {
            return this;
        }

        @Override
        public Variable<T> getFirstVariable() {
            return variable;
        }

        public PatternItem<T>[] getItems() {
            return items;
        }

        public PatternDef<T> watch(String... watch) {
            this.watch = watch;
            return this;
        }

        public String[] getWatch() {
            return watch;
        }

        @Override
        public Variable<?>[] getVariables() {
            throw new UnsupportedOperationException();
        }
    }

    public interface PatternItem<T> { }

    public static abstract class PatternExprImpl<T> implements PatternItem<T>  {
        private final String exprId;
        private final ReactOn reactOn;

        public PatternExprImpl( String exprId, ReactOn reactOn ) {
            this.exprId = exprId;
            this.reactOn = reactOn;
        }

        public String getExprId() {
            return exprId;
        }

        public String[] getReactOn() {
            return reactOn != null ? reactOn.getStrings() : new String[0];
        }

        public abstract Constraint asConstraint( PatternDefImpl patternDef );
    }

    public static class PatternExpr1<T> extends PatternExprImpl<T> {

        private final Predicate1<T> predicate;

        private final AlphaIndex<T, ?> index;

        public PatternExpr1(Predicate1<T> predicate, AlphaIndex<T, ?> index, ReactOn reactOn) {
            this(randomUUID().toString(), predicate, index, reactOn);
        }

        public PatternExpr1( String exprId, Predicate1<T> predicate, AlphaIndex<T, ?> index, ReactOn reactOn ) {
            super( exprId, reactOn );
            this.predicate = predicate;
            this.index = index;
        }

        public Predicate1<T> getPredicate() {
            return predicate;
        }

        public AlphaIndex<T, ?> getIndex() {
            return index;
        }

        @Override
        public Constraint asConstraint(PatternDefImpl patternDef) {
            SingleConstraint1 constraint = new SingleConstraint1(getExprId(), patternDef.getFirstVariable(), getPredicate());
            constraint.setIndex( getIndex() );
            constraint.setReactiveProps( getReactOn() );
            return constraint;
        }
    }

    public static class PatternExpr2<T, U> extends PatternExprImpl<T> {
        private final Variable<U> var2;
        private final Predicate2<T, U> predicate;

        private final BetaIndexImpl<T, U, ?> index;

        public PatternExpr2(Variable<U> var2, Predicate2<T, U> predicate, BetaIndexImpl<T, U, ?> index, ReactOn reactOn) {
            this(randomUUID().toString(), var2, predicate, index, reactOn);
        }

        public PatternExpr2( String exprId, Variable<U> var2, Predicate2<T, U> predicate, BetaIndexImpl<T, U, ?> index, ReactOn reactOn ) {
            super( exprId, reactOn );
            this.var2 = var2;
            this.predicate = predicate;
            this.index = index;
        }

        public Predicate2<T, U> getPredicate() {
            return predicate;
        }

        public BetaIndexImpl<T, U, ?> getIndex() {
            return index;
        }

        @Override
        public Constraint asConstraint(PatternDefImpl patternDef) {
            SingleConstraint2 constraint = new SingleConstraint2(getExprId(), patternDef.getFirstVariable(), var2, getPredicate());
            constraint.setIndex( getIndex() );
            constraint.setReactiveProps( getReactOn() );
            return constraint;
        }
    }

    public static class TemporalPatternExpr<T, U> extends PatternExprImpl<T> {
        private final Variable<U> var2;
        private final TemporalPredicate temporalPredicate;

        public TemporalPatternExpr(Variable<U> var2, TemporalPredicate temporalPredicate) {
            this(randomUUID().toString(), var2, temporalPredicate);
        }

        public TemporalPatternExpr( String exprId, Variable<U> var2, TemporalPredicate temporalPredicate) {
            super( exprId, null );
            this.var2 = var2;
            this.temporalPredicate = temporalPredicate;
        }

        @Override
        public Constraint asConstraint(PatternDefImpl patternDef) {
            return new TemporalConstraint(getExprId(), patternDef.getFirstVariable(), var2, temporalPredicate);
        }
    }

    public static class ReactOn {
        public final String[] strings;

        public ReactOn( String... strings ) {
            this.strings = strings;
        }

        public String[] getStrings() {
            return strings;
        }
    }

    public static <A, T> PatternItem<T> bind( Variable<A> boundVar, Function1<T, A> f ) {
        return new PatternBinding1<>(boundVar, f, null);
    }

    public static <A, T> PatternItem<T> bind( Variable<A> boundVar, Function1<T, A> f, ReactOn reactOn ) {
        return new PatternBinding1<>(boundVar, f, reactOn);
    }

    public static <A, T, U> PatternItem<T> bind( Variable<A> boundVar, Variable<U> otherVar, Function2<T, U, A> f ) {
        return new PatternBinding2<>(boundVar, otherVar, f, null);
    }

    public static <A, T, U> PatternItem<T> bind( Variable<A> boundVar, Variable<U> otherVar, Function2<T, U, A> f, ReactOn reactOn ) {
        return new PatternBinding2<>(boundVar, otherVar, f, reactOn);
    }

    public static abstract class PatternBindingImpl<T, A> implements PatternItem<T> {
        protected final Variable<A> boundVar;
        protected final ReactOn reactOn;

        public PatternBindingImpl( Variable<A> boundVar, ReactOn reactOn ) {
            this.boundVar = boundVar;
            this.reactOn = reactOn;
        }

        public String[] getReactOn() {
            return reactOn != null ? reactOn.getStrings() : new String[0];
        }

        public abstract Binding asBinding( PatternDefImpl patternDef );
    }

    public static class PatternBinding1<T, A> extends PatternBindingImpl<T, A> {
        private final Function1<T, A> f;

        public PatternBinding1( Variable<A> boundVar, Function1<T, A> f, ReactOn reactOn ) {
            super( boundVar, reactOn );
            this.f = f;
        }

        @Override
        public Binding asBinding( PatternDefImpl patternDef ) {
            return new BindViewItem1(boundVar, f, patternDef.getFirstVariable(), getReactOn().length > 0 ? getReactOn()[0] : null, null);
        }
    }

    public static class PatternBinding2<T, U, A> extends PatternBindingImpl<T, A> {
        private final Variable<U> otherVar;
        private final Function2<T, U, A> f;

        public PatternBinding2( Variable<A> boundVar, Variable<U> otherVar, Function2<T, U, A> f, ReactOn reactOn ) {
            super( boundVar, reactOn );
            this.f = f;
            this.otherVar = otherVar;
        }

        @Override
        public Binding asBinding( PatternDefImpl patternDef ) {
            return new BindViewItem2(boundVar, f, patternDef.getFirstVariable(), otherVar, getReactOn().length > 0 ? getReactOn()[0] : null, null);
        }
    }

    // -- Conditional Named Consequnce --

    public static <A> ConditionalConsequenceBuilder when( Variable<A> var, Predicate1<A> predicate) {
        return when( FlowDSL.expr( var, predicate ) );
    }

    public static <A> ConditionalConsequenceBuilder when(String exprId, Variable<A> var, Predicate1<A> predicate) {
        return when( FlowDSL.expr( exprId, var, predicate ) );
    }

    public static <A, B> ConditionalConsequenceBuilder when(Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
        return when( FlowDSL.expr( var1, var2, predicate ) );
    }

    public static <A, B> ConditionalConsequenceBuilder when(String exprId, Variable<A> var1, Variable<B> var2, Predicate2<A, B> predicate) {
        return when( FlowDSL.expr( exprId, var1, var2, predicate ) );
    }

    public static ConditionalConsequenceBuilder when(ExprViewItem expr) {
        return new ConditionalConsequenceBuilder( expr );
    }

    // -- rule --

    public static RuleBuilder rule( String name) {
        return new RuleBuilder( VIEW_BUILDER, name );
    }

    public static RuleBuilder rule(String pkg, String name) {
        return new RuleBuilder( VIEW_BUILDER, pkg, name);
    }

    // -- query --

    public static Query0Def query( String name ) {
        return new Query0DefImpl( VIEW_BUILDER, name );
    }

    public static Query0Def query( String pkg, String name ) {
        return new Query0DefImpl( VIEW_BUILDER, pkg, name );
    }

    public static <A> Query1Def<A> query( String name, Class<A> type1 ) {
        return new Query1DefImpl<>( VIEW_BUILDER, name, type1 );
    }

    public static <A> Query1Def<A> query( String name, Class<A> type1, String arg1name ) {
        return new Query1DefImpl<>( VIEW_BUILDER, name, type1, arg1name);
    }

    public static <A> Query1Def<A> query( String pkg, String name, Class<A> type1 ) {
        return new Query1DefImpl<>( VIEW_BUILDER, pkg, name, type1 );
    }

    public static <A,B> Query2Def<A,B> query( String name, Class<A> type1, Class<B> type2 ) {
        return new Query2DefImpl<>( VIEW_BUILDER, name, type1, type2 );
    }

    public static <A,B> Query2Def<A,B> query( String pkg, String name, Class<A> type1, Class<B> type2 ) {
        return new Query2DefImpl<>( VIEW_BUILDER, pkg, name, type1, type2 );
    }

    public static <A,B,C> Query3Def<A,B,C> query( String name, Class<A> type1, Class<B> type2, Class<C> type3 ) {
        return new Query3DefImpl<>(VIEW_BUILDER, name, type1, type2, type3 );
    }

    public static <A,B,C> Query3Def<A,B,C> query( String pkg, String name, Class<A> type1, Class<B> type2, Class<C> type3 ) {
        return new Query3DefImpl<>( VIEW_BUILDER, pkg, name, type1, type2, type3 );
    }

    public static <A,B,C, D> Query4Def<A,B,C,D> query( String name, Class<A> type1, Class<B> type2, Class<C> type3, Class<D> type4) {
        return new Query4DefImpl<>(VIEW_BUILDER, name, type1, type2, type3, type4 );
    }

    public static <A,B,C, D> Query4Def<A,B,C,D> query( String pkg, String name, Class<A> type1, Class<B> type2, Class<C> type3, Class<D> type4) {
        return new Query4DefImpl<>( VIEW_BUILDER, pkg, name, type1, type2, type3, type4 );
    }

    public static <A> Query1Def<A> query( String pkg, String name, Class<A> type1, String arg1name ) {
        return new Query1DefImpl<>( VIEW_BUILDER, pkg, name, type1, arg1name);
    }

    public static <A,B> Query2Def<A,B> query( String name, Class<A> type1, String arg1name, Class<B> type2, String arg2name ) {
        return new Query2DefImpl<>( VIEW_BUILDER, name, type1, arg1name, type2 ,arg2name);
    }

    public static <A,B> Query2Def<A,B> query( String pkg, String name, Class<A> type1, String arg1name, Class<B> type2, String arg2name ) {
        return new Query2DefImpl<>( VIEW_BUILDER, pkg, name, type1, arg1name, type2, arg2name);
    }

    public static <A,B,C> Query3Def<A,B,C> query( String name, Class<A> type1, String arg1name, Class<B> type2, String arg2name, Class<C> type3, String arg3name ) {
        return new Query3DefImpl<>(VIEW_BUILDER, name, type1, arg1name, type2, arg2name, type3, arg3name);
    }

    public static <A,B,C> Query3Def<A,B,C> query( String pkg, String name, Class<A> type1, String arg1name, Class<B> type2, String arg2name, Class<C> type3, String arg3name ) {
        return new Query3DefImpl<>( VIEW_BUILDER, pkg, name, type1, arg1name, type2, arg2name, type3, arg3name);
    }

    public static <A,B,C,D> Query4Def<A,B,C,D> query( String name, Class<A> type1, String arg1name, Class<B> type2, String arg2name, Class<C> type3, String arg3name, Class<D> type4, String arg4name) {
        return new Query4DefImpl<>(VIEW_BUILDER, name, type1, arg1name, type2, arg2name, type3, arg3name, type4, arg4name );
    }

    public static <A,B,C,D> Query4Def<A,B,C,D> query( String pkg, String name, Class<A> type1, String arg1name, Class<B> type2, String arg2name, Class<C> type3, String arg3name, Class<D> type4, String arg4name) {
        return new Query4DefImpl<>(VIEW_BUILDER, pkg, name, type1, arg1name, type2, arg2name, type3, arg3name, type4, arg4name );
    }}

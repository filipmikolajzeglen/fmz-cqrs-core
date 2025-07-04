package com.filipmikolajzeglen.cqrs.core;

/**
 * Represents an invocation of a query in the interceptor chain.
 *
 * @param <TYPE> the element type
 * @param <RESULT> the result type
 */
@FunctionalInterface
public interface QueryInvocation<TYPE, RESULT>
{
   /**
    * Invokes the next element in the query chain with the given result strategy.
    *
    * @param resultStrategy the result strategy
    * @return the result
    */
   RESULT invoke(ResultStrategy<TYPE, RESULT> resultStrategy);
}
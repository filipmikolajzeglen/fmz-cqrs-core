package com.filipmikolajzeglen.cqrs.core;

/**
 * Represents an invocation of a query in the interceptor chain.
 *
 * @param <TYPE> the element type
 * @param <PAGE> the paginated result type
 */
@FunctionalInterface
public interface QueryInvocation<TYPE, PAGE>
{
   /**
    * Invokes the next element in the query chain with the given pagination.
    *
    * @param pagination the pagination strategy
    * @return the paginated result
    */
   PAGE invoke(Pagination<TYPE, PAGE> pagination);
}
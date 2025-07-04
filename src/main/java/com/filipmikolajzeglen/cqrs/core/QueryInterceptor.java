package com.filipmikolajzeglen.cqrs.core;

/**
 * Interceptor for query execution.
 * Allows to add cross-cutting concerns (e.g. logging, transactions) to query handling.
 */
public interface QueryInterceptor
{
   <TYPE, RESULT> RESULT intercept(Query<TYPE> query, ResultStrategy<TYPE, RESULT> resultStrategy, QueryInvocation<TYPE, RESULT> next);
}
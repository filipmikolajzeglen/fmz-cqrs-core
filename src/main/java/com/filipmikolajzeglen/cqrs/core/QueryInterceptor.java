package com.filipmikolajzeglen.cqrs.core;

/**
 * Interceptor for query execution.
 * Allows to add cross-cutting concerns (e.g. logging, transactions) to query handling.
 */
public interface QueryInterceptor
{
   <TYPE, PAGE> PAGE intercept(Query<TYPE> query, Pagination<TYPE, PAGE> pagination, QueryInvocation<TYPE, PAGE> next);
}
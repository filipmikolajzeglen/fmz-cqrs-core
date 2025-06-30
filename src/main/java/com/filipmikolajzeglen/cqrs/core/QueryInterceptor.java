package com.filipmikolajzeglen.cqrs.core;

public interface QueryInterceptor
{
   <TYPE, PAGE> PAGE intercept(Query<TYPE> query, Pagination<TYPE, PAGE> pagination, QueryInvocation<TYPE, PAGE> next);
}
package com.filipmikolajzeglen.cqrs.common;

public interface QueryInterceptor
{
   <TYPE> TYPE intercept(Query<TYPE> query, QueryInvocation<TYPE> next);
}
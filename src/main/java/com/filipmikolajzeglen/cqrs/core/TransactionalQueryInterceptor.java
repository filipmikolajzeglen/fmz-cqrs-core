package com.filipmikolajzeglen.cqrs.core;

/**
 * Query interceptor that can be used to wrap query execution in a transaction.
 */
public class TransactionalQueryInterceptor implements QueryInterceptor
{
   @Override
   public <TYPE, PAGE> PAGE intercept(Query<TYPE> query, Pagination<TYPE, PAGE> pagination, QueryInvocation<TYPE, PAGE> next)
   {
      return next.invoke(pagination);
   }
}
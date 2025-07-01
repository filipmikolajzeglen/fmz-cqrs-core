package com.filipmikolajzeglen.cqrs.core;

public class TransactionalQueryInterceptor implements QueryInterceptor
{
   @Override
   public <TYPE, PAGE> PAGE intercept(Query<TYPE> query, Pagination<TYPE, PAGE> pagination, QueryInvocation<TYPE, PAGE> next)
   {
      return next.invoke(pagination);
   }
}
package com.filipmikolajzeglen.cqrs.core;

import org.springframework.transaction.annotation.Transactional;

public class TransactionalQueryInterceptor implements QueryInterceptor
{
   @Override
   @Transactional(readOnly = true)
   public <TYPE, PAGE> PAGE intercept(Query<TYPE> query, Pagination<TYPE, PAGE> pagination, QueryInvocation<TYPE, PAGE> next)
   {
      return next.invoke(pagination);
   }
}
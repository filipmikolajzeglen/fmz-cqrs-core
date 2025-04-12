package com.filipmikolajzeglen.cqrs.common;

import org.springframework.transaction.annotation.Transactional;

public class TransactionalQueryInterceptor implements QueryInterceptor
{
   @Override
   @Transactional(readOnly = true)
   public <TYPE> TYPE intercept(Query<TYPE> query, QueryInvocation<TYPE> next)
   {
      return next.invoke();
   }
}

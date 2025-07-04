package com.filipmikolajzeglen.cqrs.core;

/**
 * Query interceptor that can be used to wrap query execution in a transaction.
 */
public class TransactionalQueryInterceptor implements QueryInterceptor
{
   @Override
   public <TYPE, RESULT> RESULT intercept(Query<TYPE> query, ResultStrategy<TYPE, RESULT> resultStrategy, QueryInvocation<TYPE, RESULT> next)
   {
      return next.invoke(resultStrategy);
   }
}
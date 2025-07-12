package com.filipmikolajzeglen.cqrs.core;

import lombok.extern.slf4j.Slf4j;

/**
 * Query interceptor that logs query execution and results.
 */
@Slf4j
public class LoggingQueryInterceptor implements QueryInterceptor
{
   @Override
   public <TYPE, RESULT> RESULT intercept(Query<TYPE> query, ResultStrategy<TYPE, RESULT> resultStrategy,
         QueryInvocation<TYPE, RESULT> next)
   {
      log.info("Performing query '{}' with result strategy type '{}'", query.getClass(), resultStrategy.getType());
      return next.invoke(resultStrategy);
   }
}
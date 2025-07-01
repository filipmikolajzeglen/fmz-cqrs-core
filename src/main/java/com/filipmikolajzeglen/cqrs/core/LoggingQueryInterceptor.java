package com.filipmikolajzeglen.cqrs.core;

import lombok.extern.slf4j.Slf4j;

/**
 * Query interceptor that logs query execution and results.
 */
@Slf4j
public class LoggingQueryInterceptor implements QueryInterceptor
{
   @Override
   public <TYPE, PAGE> PAGE intercept(Query<TYPE> query, Pagination<TYPE, PAGE> pagination,
         QueryInvocation<TYPE, PAGE> next)
   {
      log.info("Performing query '{}' with pagination type '{}'", query.getClass(), pagination.getType());
      PAGE result = next.invoke(pagination);
      log.info("Query result '{}'", result.getClass());
      return result;
   }
}
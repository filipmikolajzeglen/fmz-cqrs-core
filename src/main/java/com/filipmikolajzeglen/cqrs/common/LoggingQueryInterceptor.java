package com.filipmikolajzeglen.cqrs.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingQueryInterceptor implements QueryInterceptor
{
   private static final Logger LOGGER = LogManager.getLogger(LoggingQueryInterceptor.class);

   @Override
   public <TYPE, PAGE> PAGE intercept(Query<TYPE> query, Pagination<TYPE, PAGE> pagination,
         QueryInvocation<TYPE, PAGE> next)
   {
      LOGGER.info("Performing query '{}'", query.getClass());
      PAGE result = next.invoke(pagination);
      LOGGER.info("Query result '{}'", result.getClass());
      return result;
   }
}
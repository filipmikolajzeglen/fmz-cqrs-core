package com.filipmikolajzeglen.cqrs.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingQueryInterceptor implements QueryInterceptor
{
   private static final Logger LOGGER = LogManager.getLogger(LoggingQueryInterceptor.class);
   @Override
   public <TYPE> TYPE intercept(Query<TYPE> query, QueryInvocation<TYPE> next)
   {
      LOGGER.info("Performing query '{}'", query.getClass());
      TYPE result = next.invoke();
      LOGGER.info("Query result '{}'", result.getClass());
      return result;
   }
}
package com.filipmikolajzeglen.cqrs.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingCommandInterceptor implements CommandInterceptor
{
   private static final Logger LOGGER = LogManager.getLogger(LoggingCommandInterceptor.class);

   @Override
   public <TYPE> TYPE intercept(Command<TYPE> command, CommandInvocation<TYPE> next)
   {
      LOGGER.info("Executing command '{}'", command.getClass());
      TYPE result = next.invoke();
      LOGGER.info("Command result '{}'", result.getClass());
      return result;
   }
}
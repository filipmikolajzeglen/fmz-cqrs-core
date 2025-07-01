package com.filipmikolajzeglen.cqrs.core;

import lombok.extern.slf4j.Slf4j;

/**
 * Command interceptor that logs command execution and results.
 */
@Slf4j
public class LoggingCommandInterceptor implements CommandInterceptor
{
   @Override
   public <TYPE> TYPE intercept(Command<TYPE> command, CommandInvocation<TYPE> next)
   {
      log.info("Executing command '{}'", command.getClass());
      TYPE result = next.invoke();
      log.info("Command result '{}'", result.getClass());
      return result;
   }
}
package com.filipmikolajzeglen.cqrs.core;

/**
 * Command interceptor that can be used to wrap command execution in a transaction.
 */
public class TransactionalCommandInterceptor implements CommandInterceptor
{
   @Override
   public <TYPE> TYPE intercept(Command<TYPE> command, CommandInvocation<TYPE> next)
   {
      return next.invoke();
   }
}

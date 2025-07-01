package com.filipmikolajzeglen.cqrs.core;

public class TransactionalCommandInterceptor implements CommandInterceptor
{
   @Override
   public <TYPE> TYPE intercept(Command<TYPE> command, CommandInvocation<TYPE> next)
   {
      return next.invoke();
   }
}

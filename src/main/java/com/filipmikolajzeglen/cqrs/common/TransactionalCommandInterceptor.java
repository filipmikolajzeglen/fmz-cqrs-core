package com.filipmikolajzeglen.cqrs.common;

import org.springframework.transaction.annotation.Transactional;

public class TransactionalCommandInterceptor implements CommandInterceptor
{
   @Override
   @Transactional(readOnly = false)
   public <TYPE> TYPE intercept(Command<TYPE> command, CommandInvocation<TYPE> next)
   {
      return next.invoke();
   }
}

package com.filipmikolajzeglen.cqrs.common;

public interface CommandInterceptor
{
   <TYPE> TYPE intercept(Command<TYPE> command, CommandInvocation<TYPE> next);
}
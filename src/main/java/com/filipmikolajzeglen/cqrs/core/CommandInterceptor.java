package com.filipmikolajzeglen.cqrs.core;

public interface CommandInterceptor
{
   <TYPE> TYPE intercept(Command<TYPE> command, CommandInvocation<TYPE> next);
}
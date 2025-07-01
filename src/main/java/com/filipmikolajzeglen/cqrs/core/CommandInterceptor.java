package com.filipmikolajzeglen.cqrs.core;

/**
 * Interceptor for command execution.
 * Allows adding cross-cutting concerns (e.g., logging, transactions) to command handling.
 */
public interface CommandInterceptor
{
   <TYPE> TYPE intercept(Command<TYPE> command, CommandInvocation<TYPE> next);
}
package com.filipmikolajzeglen.cqrs.core;

/**
 * Functional interface representing an invocation in a command processing chain.
 */
@FunctionalInterface
public interface CommandInvocation<TYPE>
{
   /**
    * Invokes the next element in the command chain.
    *
    * @return the result of the command
    */
   TYPE invoke();
}
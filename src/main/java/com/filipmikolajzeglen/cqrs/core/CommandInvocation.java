package com.filipmikolajzeglen.cqrs.core;

@FunctionalInterface
public interface CommandInvocation<TYPE>
{
   TYPE invoke();
}
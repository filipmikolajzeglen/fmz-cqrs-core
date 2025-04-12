package com.filipmikolajzeglen.cqrs.common;

@FunctionalInterface
public interface CommandInvocation<TYPE>
{
   TYPE invoke();
}
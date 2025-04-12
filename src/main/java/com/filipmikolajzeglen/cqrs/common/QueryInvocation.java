package com.filipmikolajzeglen.cqrs.common;

@FunctionalInterface
public interface QueryInvocation<TYPE>
{
   TYPE invoke();
}
package com.filipmikolajzeglen.cqrs.core;

@FunctionalInterface
public interface QueryInvocation<TYPE, PAGE>
{
   PAGE invoke(Pagination<TYPE, PAGE> pagination);
}
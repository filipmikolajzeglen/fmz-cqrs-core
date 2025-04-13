package com.filipmikolajzeglen.cqrs.common;

@FunctionalInterface
public interface QueryInvocation<TYPE, PAGE>
{
   PAGE invoke(Pagination<TYPE, PAGE> pagination);
}
package com.filipmikolajzeglen.cqrs.core;

public abstract class AutonomousQuery<TYPE> extends Query<TYPE>
{
   protected abstract <PAGE> PAGE perform(AutonomousQueryContext context, Pagination<TYPE, PAGE> pagination);
}
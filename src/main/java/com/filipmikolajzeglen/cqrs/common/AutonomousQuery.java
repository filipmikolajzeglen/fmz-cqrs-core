package com.filipmikolajzeglen.cqrs.common;

public abstract class AutonomousQuery<TYPE> extends Query<TYPE>
{
   protected abstract <PAGE> PAGE perform(AutonomousQueryContext context, Pagination<TYPE, PAGE> pagination);
}
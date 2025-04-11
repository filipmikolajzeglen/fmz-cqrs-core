package com.filipmikolajzeglen.cqrs.common;

public abstract class AutonomousQuery<TYPE> extends Query<TYPE>
{
   protected abstract TYPE perform(AutonomousQueryContext context);
}

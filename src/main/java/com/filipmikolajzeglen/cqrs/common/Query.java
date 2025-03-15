package com.filipmikolajzeglen.cqrs.common;

public abstract class Query<TYPE>
{
   public abstract TYPE perform();
}

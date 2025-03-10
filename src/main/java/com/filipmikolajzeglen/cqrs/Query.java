package com.filipmikolajzeglen.cqrs;

public abstract class Query<TYPE>
{
   public abstract TYPE perform();
}

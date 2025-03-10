package com.filipmikolajzeglen.cqrs;

public abstract class Command<TYPE>
{
   public abstract TYPE execute();
}

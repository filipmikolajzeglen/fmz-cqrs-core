package com.filipmikolajzeglen.cqrs.common;

public abstract class AutonomousCommand<TYPE> extends Command<TYPE>
{
   protected abstract TYPE execute(AutonomousCommandContext context);
}
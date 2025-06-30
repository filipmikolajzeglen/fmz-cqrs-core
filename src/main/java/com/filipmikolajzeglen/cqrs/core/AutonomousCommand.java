package com.filipmikolajzeglen.cqrs.core;

public abstract class AutonomousCommand<TYPE> extends Command<TYPE>
{
   protected abstract TYPE execute(AutonomousCommandContext context);
}
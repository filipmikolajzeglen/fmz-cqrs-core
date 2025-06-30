package com.filipmikolajzeglen.cqrs.core;

public interface CommandHandler<COMMAND extends Command<? extends TYPE>, TYPE>
{
   TYPE handle(COMMAND command);
}
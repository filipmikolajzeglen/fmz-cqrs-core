package com.filipmikolajzeglen.cqrs.common;

public interface CommandHandler<COMMAND extends Command<? extends TYPE>, TYPE>
{
   TYPE handle(COMMAND command);
}

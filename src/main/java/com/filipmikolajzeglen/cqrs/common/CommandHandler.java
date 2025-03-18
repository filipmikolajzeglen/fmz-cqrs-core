package com.filipmikolajzeglen.cqrs.common;

public interface CommandHandler<COMMAND extends Command<TYPE>, TYPE>
{
   TYPE handle(COMMAND command);
}

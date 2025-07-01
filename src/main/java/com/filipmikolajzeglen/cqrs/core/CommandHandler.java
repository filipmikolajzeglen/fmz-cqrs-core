package com.filipmikolajzeglen.cqrs.core;

/**
 * Handles a specific type of command.
 *
 * @param <COMMAND> the command type
 * @param <TYPE> the result type returned by the handler
 */
public interface CommandHandler<COMMAND extends Command<? extends TYPE>, TYPE>
{
   /**
    * Handles the given command.
    *
    * @param command the command to handle
    * @return the result of the command
    */
   TYPE handle(COMMAND command);
}
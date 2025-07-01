package com.filipmikolajzeglen.cqrs.core;

/**
 * Represents a business command that can execute itself using the provided {@link AutonomousCommandContext}.
 * Used for advanced scenarios where command logic requires access to the dispatcher.
 * This type of command may invoke other queries and commands, but cannot interact with external resources.
 *
 * @param <TYPE> the result type returned by the command
 */
public abstract class AutonomousCommand<TYPE> extends Command<TYPE>
{
   /**
    * Executes the command using the given context.
    *
    * @param context the autonomous command context
    * @return the result of the command
    */
   protected abstract TYPE execute(AutonomousCommandContext context);
}
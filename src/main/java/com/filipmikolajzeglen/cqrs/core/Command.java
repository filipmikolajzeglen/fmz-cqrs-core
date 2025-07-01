package com.filipmikolajzeglen.cqrs.core;

/**
 * Marker class for all commands in the CQRS pattern.
 * Used to indicate a command object that can be handled by a {@link CommandHandler}.
 *
 * @param <TYPE> the result type returned by the command handler
 */
public abstract class Command<TYPE>
{
}
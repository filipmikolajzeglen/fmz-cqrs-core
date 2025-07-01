package com.filipmikolajzeglen.cqrs.core;

/**
 * Exception thrown when no handler is found for a command or query.
 */
class NoHandlerException extends RuntimeException
{
   private NoHandlerException(String message)
   {
      super(message);
   }

   static NoHandlerException of(Command<?> command)
   {
      return new NoHandlerException(
            String.format("No handler for command %s", command.getClass()));
   }

   static NoHandlerException of(Query<?> query)
   {
      return new NoHandlerException(
            String.format("No handler for query %s", query.getClass()));
   }
}
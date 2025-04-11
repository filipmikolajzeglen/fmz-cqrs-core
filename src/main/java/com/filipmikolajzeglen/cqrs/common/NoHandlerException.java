package com.filipmikolajzeglen.cqrs.common;

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

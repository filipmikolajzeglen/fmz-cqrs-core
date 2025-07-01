package com.filipmikolajzeglen.cqrs.core;

import lombok.RequiredArgsConstructor;

/**
 * Context for executing autonomous commands and queries.
 * Provides access to the dispatcher for chaining commands and queries.
 */
@RequiredArgsConstructor
public class AutonomousCommandContext
{
   private final Dispatcher dispatcher;

   /**
    * Executes a command using the dispatcher.
    *
    * @param command the command to execute
    * @param <TYPE> the result type
    * @return the result of the command
    */
   public  <TYPE> TYPE execute(Command<TYPE> command)
   {
      return dispatcher.execute(command);
   }

   /**
    * Performs a query with pagination using the dispatcher.
    *
    * @param query the query to perform
    * @param pagination the pagination strategy
    * @param <TYPE> the element type
    * @param <PAGE> the paginated result type
    * @return the paginated result
    */
   public  <TYPE, PAGE> PAGE perform(Query<TYPE> query, Pagination<TYPE, PAGE> pagination)
   {
      return dispatcher.perform(query, pagination);
   }
}
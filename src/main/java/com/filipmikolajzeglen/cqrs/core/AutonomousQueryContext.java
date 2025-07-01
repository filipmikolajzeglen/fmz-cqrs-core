package com.filipmikolajzeglen.cqrs.core;

import lombok.RequiredArgsConstructor;

/**
 * Context for performing autonomous queries.
 * Provides access to the dispatcher for chaining queries.
 */
@RequiredArgsConstructor
public class AutonomousQueryContext
{
   private final Dispatcher dispatcher;

   /**
    * Performs a query with pagination using the dispatcher.
    *
    * @param query the query to perform
    * @param pagination the pagination strategy
    * @param <TYPE> the element type
    * @param <PAGE> the paginated result type
    * @return the paginated result
    */
   public <TYPE, PAGE> PAGE perform(Query<TYPE> query, Pagination<TYPE, PAGE> pagination)
   {
      return dispatcher.perform(query, pagination);
   }
}
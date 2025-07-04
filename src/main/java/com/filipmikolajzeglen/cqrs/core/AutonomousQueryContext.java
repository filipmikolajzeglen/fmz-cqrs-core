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
    * Performs a query with a result strategy using the dispatcher.
    *
    * @param query the query to perform
    * @param resultStrategy the result strategy
    * @param <TYPE> the element type
    * @param <RESULT> the result type
    * @return the result
    */
   public <TYPE, RESULT> RESULT perform(Query<TYPE> query, ResultStrategy<TYPE, RESULT> resultStrategy)
   {
      return dispatcher.perform(query, resultStrategy);
   }
}
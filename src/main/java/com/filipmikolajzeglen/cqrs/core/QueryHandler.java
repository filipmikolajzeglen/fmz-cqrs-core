package com.filipmikolajzeglen.cqrs.core;

/**
 * Handles a specific type of query with a result strategy.
 *
 * @param <QUERY> the query type
 * @param <TYPE> the element type
 */
public interface QueryHandler<QUERY extends Query<? extends TYPE>, TYPE>
{
   /**
    * Handles the given query with the specified result strategy.
    *
    * @param query the query to handle
    * @param resultStrategy the result strategy
    * @param <RESULT> the result type
    * @return the result
    */
   <RESULT> RESULT handle(QUERY query, ResultStrategy<TYPE, RESULT> resultStrategy);
}
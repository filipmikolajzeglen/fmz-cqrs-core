package com.filipmikolajzeglen.cqrs.core;

/**
 * Handles a specific type of query with pagination.
 *
 * @param <QUERY> the query type
 * @param <TYPE> the element type
 */
public interface QueryHandler<QUERY extends Query<? extends TYPE>, TYPE>
{
   /**
    * Handles the given query with the specified pagination.
    *
    * @param query the query to handle
    * @param pagination the pagination strategy
    * @param <PAGE> the paginated result type
    * @return the paginated result
    */
   <PAGE> PAGE handle(QUERY query, Pagination<TYPE, PAGE> pagination);
}
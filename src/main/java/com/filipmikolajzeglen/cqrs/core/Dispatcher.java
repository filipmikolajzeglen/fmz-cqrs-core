package com.filipmikolajzeglen.cqrs.core;

/**
 * Central interface for executing commands and performing queries.
 */
public interface Dispatcher
{
   /**
    * Executes the given command and returns the result.
    *
    * @param command the command to execute
    * @param <COMMAND> the command type
    * @param <TYPE> the result type
    * @return the result of the command
    */
   <COMMAND extends Command<? extends TYPE>, TYPE> TYPE execute(COMMAND command);

   /**
    * Performs the given query with the specified result strategy and returns the result.
    *
    * @param query the query to perform
    * @param resultStrategy the result strategy
    * @param <QUERY> the query type
    * @param <TYPE> the element type
    * @param <RESULT> the result type
    * @return the result
    */
   <QUERY extends Query<? extends TYPE>, TYPE, RESULT> RESULT perform(QUERY query, ResultStrategy<TYPE, RESULT> resultStrategy);
}
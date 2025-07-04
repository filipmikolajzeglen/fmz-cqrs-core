package com.filipmikolajzeglen.cqrs.core;

/**
 * Represents a business query that can perform itself using the provided {@link AutonomousQueryContext}.
 * Used for advanced scenarios where query logic requires access to the dispatcher.
 * This type of query may invoke other queries, but cannot interact with external resources.
 *
 * @param <TYPE> the result type returned by the command
 */
public abstract class AutonomousQuery<TYPE> extends Query<TYPE>
{
   /**
    * Performs the query using the given context and result strategy.
    *
    * @param context the autonomous query context
    * @param resultStrategy the result strategy
    * @param <RESULT> the result type
    * @return the result
    */
   protected abstract <RESULT> RESULT perform(AutonomousQueryContext context, ResultStrategy<TYPE, RESULT> resultStrategy);
}
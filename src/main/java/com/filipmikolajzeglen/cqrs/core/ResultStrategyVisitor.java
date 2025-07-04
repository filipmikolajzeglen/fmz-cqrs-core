package com.filipmikolajzeglen.cqrs.core;

/**
 * Visitor interface for handling different result strategies and their results.
 *
 * @param <TYPE> the type of elements being processed
 * @param <RESULT> the type of the result
 * @param <R> the return type of the visitor methods
 */
public interface ResultStrategyVisitor<TYPE, RESULT, R>
{
   /**
    * Visits a single element result strategy.
    *
    * @param resultStrategy the result strategy instance
    * @param result the result
    * @return the result of the visit
    */
   R visitSingle(ResultStrategy<TYPE, RESULT> resultStrategy, RESULT result);

   /**
    * Visits an optional element result strategy.
    *
    * @param resultStrategy the result strategy instance
    * @param result the result
    * @return the result of the visit
    */
   R visitOptional(ResultStrategy<TYPE, RESULT> resultStrategy, RESULT result);

   /**
    * Visits list result strategy.
    *
    * @param resultStrategy the result strategy instance
    * @param result the result
    * @return the result of the visit
    */
   R visitList(ResultStrategy<TYPE, RESULT> resultStrategy, RESULT result);

   /**
    * Visits an existence check result strategy.
    *
    * @param resultStrategy the result strategy instance
    * @param result the result
    * @return the result of the visit
    */
   R visitExist(ResultStrategy<TYPE, RESULT> resultStrategy, RESULT result);

   /**
    * Visits count result strategy.
    *
    * @param resultStrategy the result strategy instance
    * @param result the result
    * @return the result of the visit
    */
   R visitCount(ResultStrategy<TYPE, RESULT> resultStrategy, RESULT result);

   /**
    * Visits a first element result strategy.
    *
    * @param resultStrategy the result strategy instance
    * @param result the result
    * @return the result of the visit
    */
   R visitFirst(ResultStrategy<TYPE, RESULT> resultStrategy, RESULT result);

   /**
    * Visits a paged result strategy.
    *
    * @param resultStrategy the result strategy instance
    * @param result the result
    * @return the result of the visit
    */
   R visitPaged(ResultStrategy<TYPE, RESULT> resultStrategy, RESULT result);

   /**
    * Visits sliced result strategy.
    *
    * @param resultStrategy the result strategy instance
    * @param result the result
    * @return the result of the visit
    */
   R visitSliced(ResultStrategy<TYPE, RESULT> resultStrategy, RESULT result);
}
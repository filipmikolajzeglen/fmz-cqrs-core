package com.filipmikolajzeglen.cqrs.core;

/**
 * Visitor interface for handling different pagination strategies and their results.
 *
 * @param <TYPE> the type of elements being paginated
 * @param <PAGE> the type of the paginated result
 * @param <R> the return type of the visitor methods
 */
public interface PaginationVisitor<TYPE, PAGE, R>
{
   /**
    * Visits a single element pagination.
    *
    * @param pagination the pagination instance
    * @param page the paginated result
    * @return the result of the visit
    */
   R visitSingle(Pagination<TYPE, PAGE> pagination, PAGE page);

   /**
    * Visits an optional element pagination.
    *
    * @param pagination the pagination instance
    * @param page the paginated result
    * @return the result of the visit
    */
   R visitOptional(Pagination<TYPE, PAGE> pagination, PAGE page);

   /**
    * Visits list pagination.
    *
    * @param pagination the pagination instance
    * @param page the paginated result
    * @return the result of the visit
    */
   R visitList(Pagination<TYPE, PAGE> pagination, PAGE page);

   /**
    * Visits an existence check pagination.
    *
    * @param pagination the pagination instance
    * @param page the paginated result
    * @return the result of the visit
    */
   R visitExist(Pagination<TYPE, PAGE> pagination, PAGE page);

   /**
    * Visits count pagination.
    *
    * @param pagination the pagination instance
    * @param page the paginated result
    * @return the result of the visit
    */
   R visitCount(Pagination<TYPE, PAGE> pagination, PAGE page);

   /**
    * Visits a first element pagination.
    *
    * @param pagination the pagination instance
    * @param page the paginated result
    * @return the result of the visit
    */
   R visitFirst(Pagination<TYPE, PAGE> pagination, PAGE page);

   /**
    * Visits a paged result pagination.
    *
    * @param pagination the pagination instance
    * @param page the paginated result
    * @return the result of the visit
    */
   R visitPaged(Pagination<TYPE, PAGE> pagination, PAGE page);

   /**
    * Visits sliced result pagination.
    *
    * @param pagination the pagination instance
    * @param page the paginated result
    * @return the result of the visit
    */
   R visitSliced(Pagination<TYPE, PAGE> pagination, PAGE page);
}
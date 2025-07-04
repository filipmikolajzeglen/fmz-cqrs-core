package com.filipmikolajzeglen.cqrs.core;

import java.util.List;
import java.util.Optional;

/**
 * Interface for handling various result strategies and result types.
 * <p>
 * Standard implementations:
 * <ul>
 *   <li>{@code single()} – Single result (throws if not exactly one result).</li>
 *   <li>{@code optional()} – Optional result (throws if more than one result).</li>
 *   <li>{@code all()} – List of results with optional sorting (see {@link OrderedResultStrategy}).</li>
 *   <li>{@code exist()} – Existence check (returns boolean).</li>
 *   <li>{@code count()} – Count of results (returns long).</li>
 *   <li>{@code first()} – First result as Optional with optional sorting (see {@link OrderedResultStrategy}).</li>
 *   <li>{@code paged(int page, int size, int totalCount)} – Page-based result with optional sorting (see {@link OrderedResultStrategy}).</li>
 *   <li>{@code sliced(int offset, int limit)} – Slice-based result with optional sorting (see {@link OrderedResultStrategy}).</li>
 * </ul>
 * <p>
 * Implementations supporting sorting implement {@link OrderedResultStrategy}.
 * </p>
 *
 * @param <DATA> the type of elements being processed
 * @param <RESULT> the type of the result
 */
public interface ResultStrategy<DATA, RESULT>
{
   /**
    * Expands a list of elements into the result according to the strategy.
    *
    * @param elements the list of elements to expand
    * @return the result
    */
   RESULT expand(List<DATA> elements);

   /**
    * Expands a single element into the result according to the strategy.
    *
    * @param element the element to expand
    * @return the result
    */
   RESULT expandSingle(DATA element);

   /**
    * Returns an empty result according to the strategy.
    *
    * @return the empty result
    */
   RESULT reduceEmpty();

   /**
    * Returns the type of result strategy.
    *
    * @return the result strategy type
    */
   ResultStrategyType getType();

   /**
    * Returns the current page number (if supported by the strategy).
    *
    * @return the page number
    * @throws UnsupportedOperationException if not supported by this strategy
    */
   default int getPage()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns the page size (if supported by the strategy).
    *
    * @return the page size
    * @throws UnsupportedOperationException if not supported by this strategy
    */
   default int getSize()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns the offset (if supported by the strategy).
    *
    * @return the offset
    * @throws UnsupportedOperationException if not supported by this strategy
    */
   default int getOffset()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns the limit (if supported by the strategy).
    *
    * @return the limit
    * @throws UnsupportedOperationException if not supported by this strategy
    */
   default int getLimit()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Accepts a visitor for this result strategy.
    *
    * @param visitor the visitor to accept
    * @param result    the result to visit
    * @param <R>     the return type of the visitor
    * @return the result from the visitor
    */
   <R> R accept(ResultStrategyVisitor<DATA, RESULT, R> visitor, RESULT result);

   /**
    * Returns a result strategy instance for a single element.
    *
    * @param <TYPE> the element type
    * @return the result strategy instance
    */
   static <TYPE> ResultStrategy<TYPE, TYPE> single()
   {
      return new SingleResultStrategy<>();
   }

   /**
    * Returns a result strategy instance for an optional element.
    *
    * @param <TYPE> the element type
    * @return the result strategy instance
    */
   static <TYPE> ResultStrategy<TYPE, Optional<TYPE>> optional()
   {
      return new OptionalResultStrategy<>();
   }

   /**
    * Returns a result strategy instance for all elements as a list.
    *
    * @param <TYPE> the element type
    * @return the result strategy instance
    */
   static <TYPE> OrderedResultStrategy<TYPE, List<TYPE>> all()
   {
      return new ListResultStrategy<>();
   }

   /**
    * Returns a result strategy instance for checking existence.
    *
    * @param <TYPE> the element type
    * @return the result strategy instance
    */
   static <TYPE> ResultStrategy<TYPE, Boolean> exist()
   {
      return new ExistResultStrategy<>();
   }

   /**
    * Returns a result strategy instance for counting elements.
    *
    * @param <TYPE> the element type
    * @return the result strategy instance
    */
   static <TYPE> ResultStrategy<TYPE, Long> count()
   {
      return new CountResultStrategy<>();
   }

   /**
    * Returns a result strategy instance for the first element as optional.
    *
    * @param <TYPE> the element type
    * @return the result strategy instance
    */
   static <TYPE> OrderedResultStrategy<TYPE, Optional<TYPE>> first()
   {
      return new FirstResultStrategy<>();
   }

   /**
    * Returns a result strategy instance for paged results.
    *
    * @param <TYPE>     the element type
    * @param page       the page number
    * @param size       the page size
    * @param totalCount the total number of elements
    * @return the result strategy instance
    */
   static <TYPE> OrderedResultStrategy<TYPE, PagedResult<TYPE>> paged(int page, int size, int totalCount)
   {
      return new PagedResultStrategy<>(page, size, totalCount);
   }

   /**
    * Returns a result strategy instance for sliced results.
    *
    * @param <TYPE> the element type
    * @param offset the offset
    * @param limit  the limit
    * @return the result strategy instance
    */
   static <TYPE> OrderedResultStrategy<TYPE, SliceResult<TYPE>> sliced(int offset, int limit)
   {
      return new SliceResultStrategy<>(offset, limit);
   }

}
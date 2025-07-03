package com.filipmikolajzeglen.cqrs.core;

import java.util.List;
import java.util.Optional;

/**
 * Interface for handling various pagination strategies and result types.
 * <p>
 * Standard implementations:
 * <ul>
 *   <li>{@code single()} – Single result pagination (throws if not exactly one result).</li>
 *   <li>{@code optional()} – Optional result pagination (throws if more than one result).</li>
 *   <li>{@code all()} – List of results with optional sorting (see {@link SortablePagination}).</li>
 *   <li>{@code exist()} – Existence checks pagination (returns boolean).</li>
 *   <li>{@code count()} – Count of results pagination (returns long).</li>
 *   <li>{@code first()} – First result as Optional with optional sorting (see {@link SortablePagination}).</li>
 *   <li>{@code paged(int page, int size, int totalCount)} – Page-based pagination with optional sorting (see {@link SortablePagination}).</li>
 *   <li>{@code sliced(int offset, int limit)} – Slice-based pagination with optional sorting (see {@link SortablePagination}).</li>
 * </ul>
 * <p>
 * Implementations supporting sorting implement {@link SortablePagination}.
 * </p>
 *
 * @param <DATA> the type of elements being paginated
 * @param <PAGE> the type of the paginated result
 */
public interface Pagination<DATA, PAGE>
{
   /**
    * Expands a list of elements into the paginated result.
    *
    * @param elements the list of elements to expand
    * @return the paginated result
    */
   PAGE expand(List<DATA> elements);

   /**
    * Expands a single element into the paginated result.
    *
    * @param element the element to expand
    * @return the paginated result
    */
   PAGE expandSingle(DATA element);

   /**
    * Returns an empty paginated result.
    *
    * @return the empty paginated result
    */
   PAGE reduceEmpty();

   /**
    * Returns the type of pagination.
    *
    * @return the pagination type
    */
   PaginationType getType();

   /**
    * Returns the current page number (if supported).
    *
    * @return the page number
    * @throws UnsupportedOperationException if not supported
    */
   default int getPage()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns the page size (if supported).
    *
    * @return the page size
    * @throws UnsupportedOperationException if not supported
    */
   default int getSize()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns the offset (if supported).
    *
    * @return the offset
    * @throws UnsupportedOperationException if not supported
    */
   default int getOffset()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns the limit (if supported).
    *
    * @return the limit
    * @throws UnsupportedOperationException if not supported
    */
   default int getLimit()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Accepts a visitor for this pagination strategy.
    *
    * @param visitor the visitor to accept
    * @param page    the paginated result to visit
    * @param <R>     the return type of the visitor
    * @return the result from the visitor
    */
   <R> R accept(PaginationVisitor<DATA, PAGE, R> visitor, PAGE page);

   /**
    * Returns a pagination instance for a single element.
    *
    * @param <TYPE> the element type
    * @return the pagination instance
    */
   static <TYPE> Pagination<TYPE, TYPE> single()
   {
      return new SinglePagination<>();
   }

   /**
    * Returns a pagination instance for an optional element.
    *
    * @param <TYPE> the element type
    * @return the pagination instance
    */
   static <TYPE> Pagination<TYPE, Optional<TYPE>> optional()
   {
      return new OptionalPagination<>();
   }

   /**
    * Returns a pagination instance for all elements as a list.
    *
    * @param <TYPE> the element type
    * @return the pagination instance
    */
   static <TYPE> SortablePagination<TYPE, List<TYPE>> all()
   {
      return new ListPagination<>();
   }

   /**
    * Returns a pagination instance for checking existence.
    *
    * @param <TYPE> the element type
    * @return the pagination instance
    */
   static <TYPE> Pagination<TYPE, Boolean> exist()
   {
      return new ExistPagination<>();
   }

   /**
    * Returns a pagination instance for counting elements.
    *
    * @param <TYPE> the element type
    * @return the pagination instance
    */
   static <TYPE> Pagination<TYPE, Long> count()
   {
      return new CountPagination<>();
   }

   /**
    * Returns a pagination instance for the first element as optional.
    *
    * @param <TYPE> the element type
    * @return the pagination instance
    */
   static <TYPE> SortablePagination<TYPE, Optional<TYPE>> first()
   {
      return new FirstPagination<>();
   }

   /**
    * Returns a pagination instance for paged results.
    *
    * @param <TYPE>     the element type
    * @param page       the page number
    * @param size       the page size
    * @param totalCount the total number of elements
    * @return the pagination instance
    */
   static <TYPE> SortablePagination<TYPE, PagedResult<TYPE>> paged(int page, int size, int totalCount)
   {
      return new PagedPagination<>(page, size, totalCount);
   }

   /**
    * Returns a pagination instance for sliced results.
    *
    * @param <TYPE> the element type
    * @param offset the offset
    * @param limit  the limit
    * @return the pagination instance
    */
   static <TYPE> SortablePagination<TYPE, SliceResult<TYPE>> sliced(int offset, int limit)
   {
      return new SlicePagination<>(offset, limit);
   }

}
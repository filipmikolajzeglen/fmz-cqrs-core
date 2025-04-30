package com.filipmikolajzeglen.cqrs.common;

import java.util.List;
import java.util.Optional;

public interface Pagination<DATA, PAGE>
{
   PAGE expand(List<DATA> elements);

   PAGE expandSingle(DATA element);

   PAGE reduceEmpty();

   static <TYPE> Pagination<TYPE, TYPE> single()
   {
      return new SinglePagination<>();
   }

   static <TYPE> Pagination<TYPE, Optional<TYPE>> optional()
   {
      return new OptionalPagination<>();
   }

   static <TYPE> Pagination<TYPE, List<TYPE>> all()
   {
      return new ListPagination<>();
   }

   static <TYPE> Pagination<TYPE, Boolean> exist()
   {
      return new ExistPagination<>();
   }

   static <TYPE> Pagination<TYPE, Long> count()
   {
      return new CountPagination<>();
   }

   static <TYPE> Pagination<TYPE, Optional<TYPE>> first()
   {
      return new FirstPagination<>();
   }

   static <TYPE> Pagination<TYPE, PagedResult<TYPE>> paged(int page, int size, int totalCount)
   {
      return new PagedResultPagination<>(page, size, totalCount);
   }

   static <TYPE> Pagination<TYPE, SliceResult<TYPE>> sliced(int offset, int limit)
   {
      return new SliceResultPagination<>(offset, limit);
   }

   default Optional<Pageable> asPageable()
   {
      return Optional.empty();
   }

   interface Pageable
   {
      int offset();

      int limit();

      boolean requireTotalCount();
   }
}
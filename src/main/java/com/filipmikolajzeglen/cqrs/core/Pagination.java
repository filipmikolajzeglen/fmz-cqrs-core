package com.filipmikolajzeglen.cqrs.core;

import java.util.List;
import java.util.Optional;

public interface Pagination<DATA, PAGE>
{
   PAGE expand(List<DATA> elements);

   PAGE expandSingle(DATA element);

   PAGE reduceEmpty();

   PaginationType getType();

   default int getPage()
   {
      throw new UnsupportedOperationException();
   }

   default int getSize()
   {
      throw new UnsupportedOperationException();
   }

   default int getOffset()
   {
      throw new UnsupportedOperationException();
   }

   default int getLimit()
   {
      throw new UnsupportedOperationException();
   }

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
      return new PagedPagination<>(page, size, totalCount);
   }

   static <TYPE> Pagination<TYPE, SliceResult<TYPE>> sliced(int offset, int limit)
   {
      return new SlicePagination<>(offset, limit);
   }
}
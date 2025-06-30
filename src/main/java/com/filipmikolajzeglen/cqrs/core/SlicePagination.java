package com.filipmikolajzeglen.cqrs.core;

import java.util.List;

import lombok.Getter;

@Getter
public class SlicePagination<TYPE> implements Pagination<TYPE, SliceResult<TYPE>>
{
   private final int offset;
   private final int limit;

   public SlicePagination(int offset, int limit)
   {
      this.offset = offset;
      this.limit = limit;
   }

   @Override
   public SliceResult<TYPE> expand(List<TYPE> elements)
   {
      int fromIndex = Math.min(offset, elements.size());
      int toIndex = Math.min(fromIndex + limit, elements.size());
      boolean hasNext = elements.size() > toIndex;
      List<TYPE> content = elements.subList(fromIndex, toIndex);

      return SliceResult.<TYPE>builder()
            .withContent(content)
            .withOffset(offset)
            .withLimit(limit)
            .withHasNext(hasNext)
            .build();
   }

   @Override
   public SliceResult<TYPE> expandSingle(TYPE element)
   {
      if (offset != 0)
         throw new IllegalStateException("expandSingle can only be used with offset = 0");
      return expand(List.of(element));
   }

   @Override
   public SliceResult<TYPE> reduceEmpty()
   {
      return expand(List.of());
   }

   @Override
   public PaginationType getType()
   {
      return PaginationType.SLICED;
   }

   @Override
   public int getOffset() { return offset; }

   @Override
   public int getLimit() { return limit; }
}
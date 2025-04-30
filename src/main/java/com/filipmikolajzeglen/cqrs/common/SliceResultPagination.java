package com.filipmikolajzeglen.cqrs.common;

import java.util.List;
import java.util.Optional;

import lombok.Getter;

@Getter
public class SliceResultPagination<TYPE> implements Pagination<TYPE, SliceResult<TYPE>>
{
   private final int offset;
   private final int limit;

   public SliceResultPagination(int offset, int limit)
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
      {
         throw new IllegalStateException("expandSingle can only be used with offset = 0");
      }
      return expand(List.of(element));
   }

   @Override
   public SliceResult<TYPE> reduceEmpty()
   {
      return expand(List.of());
   }

   @Override
   public Optional<Pageable> asPageable()
   {
      return Optional.of(new Pageable()
      {
         @Override
         public int offset()
         {
            return offset;
         }

         @Override
         public int limit()
         {
            return limit;
         }

         @Override
         public boolean requireTotalCount()
         {
            return false;
         }
      });
   }
}
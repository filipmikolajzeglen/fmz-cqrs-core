package com.filipmikolajzeglen.cqrs.common;

import java.util.List;
import java.util.Optional;

import lombok.Getter;

@Getter
public class PagedResultPagination<TYPE> implements Pagination<TYPE, PagedResult<TYPE>>
{
   private final int page;
   private final int size;
   private final int totalCount;

   public PagedResultPagination(int page, int size, int totalCount)
   {
      this.page = page;
      this.size = size;
      this.totalCount = totalCount;
   }

   @Override
   public PagedResult<TYPE> expand(List<TYPE> elements)
   {
      int fromIndex = Math.min(page * size, elements.size());
      int toIndex = Math.min(fromIndex + size, elements.size());
      List<TYPE> content = elements.subList(fromIndex, toIndex);
      int totalPages = (int) Math.ceil((double) totalCount / size);

      return PagedResult.<TYPE>builder()
            .withContent(content)
            .withPage(page)
            .withSize(size)
            .withTotalElements(totalCount)
            .withTotalPages(totalPages)
            .build();
   }

   @Override
   public PagedResult<TYPE> expandSingle(TYPE element)
   {
      if (page != 0)
      {
         throw new IllegalStateException("expandSingle can only be used with page = 0");
      }
      return expand(List.of(element));
   }

   @Override
   public PagedResult<TYPE> reduceEmpty()
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
            return page * size;
         }

         @Override
         public int limit()
         {
            return size;
         }

         @Override
         public boolean requireTotalCount()
         {
            return true;
         }
      });
   }
}
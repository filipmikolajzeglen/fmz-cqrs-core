package com.filipmikolajzeglen.cqrs.core;

import java.util.List;

import lombok.Getter;

/**
 * Pagination strategy that returns a page of elements as a {@link PagedResult}.
 *
 * @param <TYPE> the element type
 */
@Getter
public class PagedPagination<TYPE> implements Pagination<TYPE, PagedResult<TYPE>>
{
   private final int page;
   private final int size;
   private final int totalCount;

   public PagedPagination(int page, int size, int totalCount)
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
         throw new IllegalStateException("expandSingle can only be used with page = 0");
      return expand(List.of(element));
   }

   @Override
   public PagedResult<TYPE> reduceEmpty()
   {
      return expand(List.of());
   }

   @Override
   public PaginationType getType()
   {
      return PaginationType.PAGED;
   }

   @Override
   public int getPage() { return page; }

   @Override
   public int getSize() { return size; }
}
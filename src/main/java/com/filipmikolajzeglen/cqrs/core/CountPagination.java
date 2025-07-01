package com.filipmikolajzeglen.cqrs.core;

import java.util.List;

/**
 * Pagination strategy that returns the count of elements as a {@link Long}.
 *
 * @param <TYPE> the element type
 */
public class CountPagination<TYPE> implements Pagination<TYPE, Long>
{
   @Override
   public Long expand(List<TYPE> elements)
   {
      return (long) elements.size();
   }

   @Override
   public Long expandSingle(TYPE element)
   {
      return element == null ? 0L : 1L;
   }

   @Override
   public Long reduceEmpty()
   {
      return 0L;
   }

   @Override
   public PaginationType getType()
   {
      return PaginationType.COUNT;
   }
}
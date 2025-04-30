package com.filipmikolajzeglen.cqrs.common;

import java.util.List;

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
}
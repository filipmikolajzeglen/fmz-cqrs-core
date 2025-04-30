package com.filipmikolajzeglen.cqrs.common;

import java.util.List;
import java.util.Optional;

public class FirstPagination<TYPE> implements Pagination<TYPE, Optional<TYPE>>
{
   @Override
   public Optional<TYPE> expand(List<TYPE> elements)
   {
      return elements.isEmpty() ? Optional.empty() : Optional.ofNullable(elements.get(0));
   }

   @Override
   public Optional<TYPE> expandSingle(TYPE element)
   {
      return Optional.ofNullable(element);
   }

   @Override
   public Optional<TYPE> reduceEmpty()
   {
      return Optional.empty();
   }

   @Override
   public Optional<Pageable> asPageable()
   {
      return Optional.of(new Pageable()
      {
         @Override
         public int offset()
         {
            return 0;
         }

         @Override
         public int limit()
         {
            return 1;
         }

         @Override
         public boolean requireTotalCount()
         {
            return false;
         }
      });
   }
}
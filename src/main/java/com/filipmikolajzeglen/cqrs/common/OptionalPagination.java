package com.filipmikolajzeglen.cqrs.common;

import java.util.List;
import java.util.Optional;

public class OptionalPagination<TYPE> implements Pagination<TYPE, Optional<TYPE>>
{
   @Override
   public Optional<TYPE> expand(List<TYPE> elements)
   {
      if (elements.isEmpty())
      {
         return Optional.empty();
      }
      if (elements.size() > 1)
      {
         throw new IllegalStateException("Expected at most one result but found multiple");
      }
      return Optional.of(elements.get(0));
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
   public PaginationType getType()
   {
      return PaginationType.OPTIONAL;
   }
}
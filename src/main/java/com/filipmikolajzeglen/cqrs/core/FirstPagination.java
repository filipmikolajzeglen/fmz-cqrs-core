package com.filipmikolajzeglen.cqrs.core;

import java.util.List;
import java.util.Optional;

/**
 * Pagination strategy that returns the first element as an {@link Optional}.
 *
 * @param <TYPE> the element type
 */
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
   public PaginationType getType()
   {
      return PaginationType.FIRST;
   }

   @Override
   public int getOffset() { return 0; }

   @Override
   public int getLimit() { return 1; }
}
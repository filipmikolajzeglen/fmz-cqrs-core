package com.filipmikolajzeglen.cqrs.core;

import java.util.List;

/**
 * Pagination strategy that returns all elements as a {@link List}.
 *
 * @param <TYPE> the element type
 */
public class ListPagination<TYPE> implements Pagination<TYPE, List<TYPE>>
{
   @Override
   public List<TYPE> expand(List<TYPE> elements)
   {
      return elements;
   }

   @Override
   public List<TYPE> expandSingle(TYPE element)
   {
      return List.of(element);
   }

   @Override
   public List<TYPE> reduceEmpty()
   {
      return List.of();
   }

   @Override
   public PaginationType getType()
   {
      return PaginationType.LIST;
   }
}

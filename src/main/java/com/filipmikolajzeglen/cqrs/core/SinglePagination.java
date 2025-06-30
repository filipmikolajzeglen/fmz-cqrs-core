package com.filipmikolajzeglen.cqrs.core;

import java.util.List;
import java.util.NoSuchElementException;

public class SinglePagination<TYPE> implements Pagination<TYPE, TYPE>
{
   @Override
   public TYPE expand(List<TYPE> elements)
   {
      if (elements.isEmpty())
      {
         throw new NoSuchElementException("No result found");
      }
      if (elements.size() > 1)
      {
         throw new IllegalStateException("Expected single result but found multiple");
      }
      return elements.get(0);
   }

   @Override
   public TYPE expandSingle(TYPE element)
   {
      return element;
   }

   @Override
   public TYPE reduceEmpty()
   {
      throw new NoSuchElementException("No result found");
   }

   @Override
   public PaginationType getType()
   {
      return PaginationType.SINGLE;
   }
}

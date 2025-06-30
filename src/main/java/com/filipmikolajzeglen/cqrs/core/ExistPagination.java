package com.filipmikolajzeglen.cqrs.core;

import java.util.List;

public class ExistPagination<TYPE> implements Pagination<TYPE, Boolean>
{
   @Override
   public Boolean expand(List<TYPE> elements)
   {
      return !elements.isEmpty();
   }

   @Override
   public Boolean expandSingle(TYPE element)
   {
      return element != null;
   }

   @Override
   public Boolean reduceEmpty()
   {
      return false;
   }

   @Override
   public PaginationType getType()
   {
      return PaginationType.EXIST;
   }
}
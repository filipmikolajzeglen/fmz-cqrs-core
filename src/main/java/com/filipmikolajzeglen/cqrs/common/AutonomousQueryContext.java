package com.filipmikolajzeglen.cqrs.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutonomousQueryContext
{
   private final Dispatcher dispatcher;

   public <TYPE, PAGE> PAGE perform(Query<TYPE> query, Pagination<TYPE, PAGE> pagination)
   {
      return dispatcher.perform(query, pagination);
   }
}
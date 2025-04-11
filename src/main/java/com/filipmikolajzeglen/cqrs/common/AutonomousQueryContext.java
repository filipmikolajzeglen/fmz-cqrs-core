package com.filipmikolajzeglen.cqrs.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutonomousQueryContext
{
   private final Dispatcher dispatcher;

   public <TYPE> TYPE perform(Query<TYPE> query)
   {
      return dispatcher.perform(query);
   }
}
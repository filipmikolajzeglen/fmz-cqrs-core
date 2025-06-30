package com.filipmikolajzeglen.cqrs.core;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutonomousCommandContext
{
   private final Dispatcher dispatcher;

   public  <TYPE> TYPE execute(Command<TYPE> command)
   {
      return dispatcher.execute(command);
   }

   public  <TYPE, PAGE> PAGE perform(Query<TYPE> query, Pagination<TYPE, PAGE> pagination)
   {
      return dispatcher.perform(query, pagination);
   }
}
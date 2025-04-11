package com.filipmikolajzeglen.cqrs.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutonomousCommandContext
{
   private final Dispatcher dispatcher;

   public <TYPE> TYPE execute(Command<TYPE> command)
   {
      return dispatcher.execute(command);
   }

   public <TYPE> TYPE perform(Query<TYPE> query)
   {
      return dispatcher.perform(query);
   }
}
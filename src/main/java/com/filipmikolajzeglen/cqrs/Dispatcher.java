package com.filipmikolajzeglen.cqrs;

public class Dispatcher
{
   public <COMMAND extends Command<TYPE>, TYPE> TYPE execute(COMMAND command)
   {
      return command.execute();
   }

   public <QUERY extends Query<TYPE>, TYPE> TYPE perform(QUERY query)
   {
      return query.perform();
   }
}
package com.filipmikolajzeglen.cqrs.core;

import lombok.ToString;

@ToString
class MyBaseCommandHandler implements CommandHandler<MyBaseCommand, String>
{
   @Override
   public String handle(MyBaseCommand command)
   {
      return "Handled:" + command.getClass().getSimpleName();
   }
}

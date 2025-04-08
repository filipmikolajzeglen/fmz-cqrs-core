package com.filipmikolajzeglen.cqrs.common;

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

package com.filipmikolajzeglen.cqrs.common;

class MyCommandHandler implements CommandHandler<MyCommand, String>
{
   @Override
   public String handle(MyCommand command)
   {
      return "Handled:" + command.getClass().getSimpleName();
   }
}

package com.filipmikolajzeglen.cqrs.common;

class OtherCommandHandler implements CommandHandler<MyCommand, String>
{
   @Override
   public String handle(MyCommand command)
   {
      return "Handled:" + command.getClass().getSimpleName();
   }
}

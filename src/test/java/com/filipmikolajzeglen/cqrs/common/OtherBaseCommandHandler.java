package com.filipmikolajzeglen.cqrs.common;

class OtherBaseCommandHandler implements CommandHandler<OtherBaseCommand, String>
{
   @Override
   public String handle(OtherBaseCommand command)
   {
      return "Handled:" + command.getClass().getSimpleName();
   }
}

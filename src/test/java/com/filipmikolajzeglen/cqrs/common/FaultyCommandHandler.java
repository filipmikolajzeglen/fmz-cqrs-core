package com.filipmikolajzeglen.cqrs.common;

@Handler
class FaultyCommandHandler implements CommandHandler<FaultyCommand, Entity>
{
   @Override
   public Entity handle(FaultyCommand command)
   {
      throw new RuntimeException("Simulated handler failure");
   }
}
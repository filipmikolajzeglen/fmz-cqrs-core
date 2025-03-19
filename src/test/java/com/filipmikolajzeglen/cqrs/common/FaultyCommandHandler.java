package com.filipmikolajzeglen.cqrs.common;

class FaultyCommandHandler implements CommandHandler<FaultyCommand, Entity>
{
   @Override
   public Entity handle(FaultyCommand command)
   {
      throw new RuntimeException("Simulated handler failure");
   }
}
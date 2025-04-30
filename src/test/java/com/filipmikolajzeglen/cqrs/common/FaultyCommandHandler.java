package com.filipmikolajzeglen.cqrs.common;

class FaultyCommandHandler implements CommandHandler<FaultyCommand, DummyEntity>
{
   @Override
   public DummyEntity handle(FaultyCommand command)
   {
      throw new RuntimeException("Simulated handler failure");
   }
}
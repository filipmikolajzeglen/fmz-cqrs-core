package com.filipmikolajzeglen.cqrs.core;

class FaultyCommandHandler implements CommandHandler<FaultyCommand, DummyEntity>
{
   @Override
   public DummyEntity handle(FaultyCommand command)
   {
      throw new RuntimeException("Simulated handler failure");
   }
}
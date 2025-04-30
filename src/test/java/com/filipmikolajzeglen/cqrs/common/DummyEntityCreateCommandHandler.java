package com.filipmikolajzeglen.cqrs.common;

class DummyEntityCreateCommandHandler implements CommandHandler<DummyEntityCreateCommand, DummyEntity>
{
   @Override
   public DummyEntity handle(DummyEntityCreateCommand command)
   {
      return DummyEntity.of(command.id, command.name, command.flag);
   }
}
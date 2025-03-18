package com.filipmikolajzeglen.cqrs.common;

@Handler
class EntityCreateCommandHandler implements CommandHandler<EntityCreateCommand, Entity>
{
   @Override
   public Entity handle(EntityCreateCommand command)
   {
      return Entity.of(command.name, command.flag);
   }
}
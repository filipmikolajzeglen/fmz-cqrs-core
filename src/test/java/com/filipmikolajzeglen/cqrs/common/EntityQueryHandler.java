package com.filipmikolajzeglen.cqrs.common;

import java.util.List;
import java.util.NoSuchElementException;

class EntityQueryHandler implements QueryHandler<EntityQuery, Entity>
{
   List<Entity> inMemoryEntities = List.of(
         Entity.of("Test Entity 1", true),
         Entity.of("Test Entity 2", false),
         Entity.of("Test Entity 3", true),
         Entity.of("Test Entity 4", false)
   );

   @Override
   public <PAGE> PAGE handle(EntityQuery query, Pagination<Entity, PAGE> pagination)
   {
      var entity = inMemoryEntities.stream()
            .filter(e -> e.name.equals(query.getName()))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Entity not found"));
      return pagination.expandSingle(entity);
   }
}

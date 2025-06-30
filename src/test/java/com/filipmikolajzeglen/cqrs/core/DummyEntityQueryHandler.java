package com.filipmikolajzeglen.cqrs.core;

import java.util.List;
import java.util.NoSuchElementException;

class DummyEntityQueryHandler implements QueryHandler<DummyEntityQuery, DummyEntity>
{
   List<DummyEntity> inMemoryEntities = List.of(
         DummyEntity.of(1L, "Test Entity 1", true),
         DummyEntity.of(2L, "Test Entity 2", false),
         DummyEntity.of(3L, "Test Entity 3", true),
         DummyEntity.of(4L, "Test Entity 4", false)
   );

   @Override
   public <PAGE> PAGE handle(DummyEntityQuery query, Pagination<DummyEntity, PAGE> pagination)
   {
      var entity = inMemoryEntities.stream()
            .filter(e -> e.name.equals(query.getName()))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Entity not found"));
      return pagination.expandSingle(entity);
   }
}

package com.filipmikolajzeglen.cqrs.common;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class EntityQuery extends Query<Entity>
{
   @NotNull String name;
}

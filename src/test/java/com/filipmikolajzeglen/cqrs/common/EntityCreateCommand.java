package com.filipmikolajzeglen.cqrs.common;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class EntityCreateCommand extends Command<Entity>
{
   @NotNull String name;
   boolean flag = true;
}
package com.filipmikolajzeglen.cqrs.core;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class DummyEntityCreateCommand extends Command<DummyEntity>
{
   @NotNull Long id;
   @NotNull String name;
   boolean flag = true;
}
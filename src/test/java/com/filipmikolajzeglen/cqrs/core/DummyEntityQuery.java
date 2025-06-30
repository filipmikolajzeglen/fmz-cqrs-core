package com.filipmikolajzeglen.cqrs.core;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class DummyEntityQuery extends Query<DummyEntity>
{
   @NotNull String name;
}
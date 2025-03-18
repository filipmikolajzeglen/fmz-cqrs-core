package com.filipmikolajzeglen.cqrs.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
class Entity
{
   String name;
   boolean flag;
}

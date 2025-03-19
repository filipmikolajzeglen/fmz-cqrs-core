package com.filipmikolajzeglen.cqrs.common;

public interface QueryHandler<QUERY extends Query<? extends TYPE>, TYPE>
{
   TYPE handle(QUERY query);
}

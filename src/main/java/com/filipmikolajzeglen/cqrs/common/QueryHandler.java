package com.filipmikolajzeglen.cqrs.common;

public interface QueryHandler<QUERY extends Query<TYPE>, TYPE>
{
   TYPE handle(QUERY query);
}

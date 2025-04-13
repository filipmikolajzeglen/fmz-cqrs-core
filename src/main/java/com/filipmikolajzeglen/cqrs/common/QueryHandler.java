package com.filipmikolajzeglen.cqrs.common;

public interface QueryHandler<QUERY extends Query<? extends TYPE>, TYPE>
{
   <PAGE> PAGE handle(QUERY query, Pagination<TYPE, PAGE> pagination);
}
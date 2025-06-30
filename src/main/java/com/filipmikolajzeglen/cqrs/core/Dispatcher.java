package com.filipmikolajzeglen.cqrs.core;

public interface Dispatcher
{
   <COMMAND extends Command<? extends TYPE>, TYPE> TYPE execute(COMMAND command);

   <QUERY extends Query<? extends TYPE>, TYPE, PAGE> PAGE perform(QUERY query, Pagination<TYPE, PAGE> pagination);
}
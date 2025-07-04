package com.filipmikolajzeglen.cqrs.core;

import java.util.List;

public interface OrderedResultStrategy<DATA, RESULT> extends ResultStrategy<DATA, RESULT>
{
   OrderedResultStrategy<DATA, RESULT> orderedByAsc(String property);

   OrderedResultStrategy<DATA, RESULT> orderedByDesc(String property);

   List<Order> getOrders();
}
package com.filipmikolajzeglen.cqrs.core;

import java.util.List;

public interface SortablePagination<DATA, PAGE> extends Pagination<DATA, PAGE>
{
   SortablePagination<DATA, PAGE> orderedByAsc(String property);

   SortablePagination<DATA, PAGE> orderedByDesc(String property);

   List<Sort> getSorts();
}
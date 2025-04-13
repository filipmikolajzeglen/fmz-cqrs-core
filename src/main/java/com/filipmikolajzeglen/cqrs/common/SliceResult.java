package com.filipmikolajzeglen.cqrs.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@SuppressWarnings("ClassCanBeRecord")
@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class SliceResult<TYPE>
{
   private final List<TYPE> content;
   private final int offset;
   private final int limit;
   private final boolean hasNext;
}

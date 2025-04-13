package com.filipmikolajzeglen.cqrs.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@SuppressWarnings("ClassCanBeRecord")
@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class PagedResult<TYPE>
{
   private final List<TYPE> content;
   private final int page;
   private final int size;
   private final long totalElements;
   private final int totalPages;
}
package com.filipmikolajzeglen.cqrs.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class SingleResultStrategy<TYPE> implements ResultStrategy<TYPE, TYPE>
{
   @Override
   public TYPE expand(List<TYPE> elements)
   {
      if (elements.isEmpty())
      {
         throw new NoSuchElementException("No result found");
      }
      if (elements.size() > 1)
      {
         throw new IllegalStateException("Expected single result but found multiple");
      }
      return elements.get(0);
   }

   @Override
   public TYPE expandSingle(TYPE element)
   {
      return element;
   }

   @Override
   public TYPE reduceEmpty()
   {
      throw new NoSuchElementException("No result found");
   }

   @Override
   public ResultStrategyType getType()
   {
      return ResultStrategyType.SINGLE;
   }

   @Override
   public <R> R accept(ResultStrategyVisitor<TYPE, TYPE, R> visitor, TYPE page)
   {
      return visitor.visitSingle(this, page);
   }
}

class OptionalResultStrategy<TYPE> implements ResultStrategy<TYPE, Optional<TYPE>>
{
   @Override
   public Optional<TYPE> expand(List<TYPE> elements)
   {
      if (elements.isEmpty())
      {
         return Optional.empty();
      }
      if (elements.size() > 1)
      {
         throw new IllegalStateException("Expected at most one result but found multiple");
      }
      return Optional.of(elements.get(0));
   }

   @Override
   public Optional<TYPE> expandSingle(TYPE element)
   {
      return Optional.ofNullable(element);
   }

   @Override
   public Optional<TYPE> reduceEmpty()
   {
      return Optional.empty();
   }

   @Override
   public ResultStrategyType getType()
   {
      return ResultStrategyType.OPTIONAL;
   }

   @Override
   public <R> R accept(ResultStrategyVisitor<TYPE, Optional<TYPE>, R> visitor, Optional<TYPE> page)
   {
      return visitor.visitOptional(this, page);
   }
}

class ExistResultStrategy<TYPE> implements ResultStrategy<TYPE, Boolean>
{
   @Override
   public Boolean expand(List<TYPE> elements)
   {
      return !elements.isEmpty();
   }

   @Override
   public Boolean expandSingle(TYPE element)
   {
      return element != null;
   }

   @Override
   public Boolean reduceEmpty()
   {
      return false;
   }

   @Override
   public ResultStrategyType getType()
   {
      return ResultStrategyType.EXIST;
   }

   @Override
   public <R> R accept(ResultStrategyVisitor<TYPE, Boolean, R> visitor, Boolean page)
   {
      return visitor.visitExist(this, page);
   }
}

class CountResultStrategy<TYPE> implements ResultStrategy<TYPE, Long>
{
   @Override
   public Long expand(List<TYPE> elements)
   {
      return (long) elements.size();
   }

   @Override
   public Long expandSingle(TYPE element)
   {
      return element == null ? 0L : 1L;
   }

   @Override
   public Long reduceEmpty()
   {
      return 0L;
   }

   @Override
   public ResultStrategyType getType()
   {
      return ResultStrategyType.COUNT;
   }

   @Override
   public <R> R accept(ResultStrategyVisitor<TYPE, Long, R> visitor, Long page)
   {
      return visitor.visitCount(this, page);
   }
}

class ListResultStrategy<TYPE> implements OrderedResultStrategy<TYPE, List<TYPE>>
{
   private final List<Order> sorts = new ArrayList<>();

   @Override
   public OrderedResultStrategy<TYPE, List<TYPE>> orderedByAsc(String property)
   {
      sorts.add(new Order(property, Order.Direction.ASC));
      return this;
   }

   @Override
   public OrderedResultStrategy<TYPE, List<TYPE>> orderedByDesc(String property)
   {
      sorts.add(new Order(property, Order.Direction.DESC));
      return this;
   }

   @Override
   public List<Order> getOrders()
   {
      return Collections.unmodifiableList(sorts);
   }

   @Override
   public List<TYPE> expand(List<TYPE> elements)
   {
      return elements;
   }

   @Override
   public List<TYPE> expandSingle(TYPE element)
   {
      return List.of(element);
   }

   @Override
   public List<TYPE> reduceEmpty()
   {
      return List.of();
   }

   @Override
   public ResultStrategyType getType()
   {
      return ResultStrategyType.LIST;
   }

   @Override
   public <R> R accept(ResultStrategyVisitor<TYPE, List<TYPE>, R> visitor, List<TYPE> page)
   {
      return visitor.visitList(this, page);
   }
}

class FirstResultStrategy<TYPE> implements OrderedResultStrategy<TYPE, Optional<TYPE>>
{
   private final List<Order> sorts = new ArrayList<>();

   @Override
   public OrderedResultStrategy<TYPE, Optional<TYPE>> orderedByAsc(String property)
   {
      sorts.add(new Order(property, Order.Direction.ASC));
      return this;
   }

   @Override
   public OrderedResultStrategy<TYPE, Optional<TYPE>> orderedByDesc(String property)
   {
      sorts.add(new Order(property, Order.Direction.DESC));
      return this;
   }

   @Override
   public List<Order> getOrders()
   {
      return Collections.unmodifiableList(sorts);
   }

   @Override
   public Optional<TYPE> expand(List<TYPE> elements)
   {
      return elements.isEmpty() ? Optional.empty() : Optional.ofNullable(elements.get(0));
   }

   @Override
   public Optional<TYPE> expandSingle(TYPE element)
   {
      return Optional.ofNullable(element);
   }

   @Override
   public Optional<TYPE> reduceEmpty()
   {
      return Optional.empty();
   }

   @Override
   public ResultStrategyType getType()
   {
      return ResultStrategyType.FIRST;
   }

   @Override
   public int getOffset()
   {
      return 0;
   }

   @Override
   public int getLimit()
   {
      return 1;
   }

   @Override
   public <R> R accept(ResultStrategyVisitor<TYPE, Optional<TYPE>, R> visitor, Optional<TYPE> page)
   {
      return visitor.visitFirst(this, page);
   }
}

class PagedResultStrategy<TYPE> implements OrderedResultStrategy<TYPE, PagedResult<TYPE>>
{
   private final List<Order> sorts = new ArrayList<>();
   private final int page;
   private final int size;
   private final int totalCount;

   public PagedResultStrategy(int page, int size, int totalCount)
   {
      this.page = page;
      this.size = size;
      this.totalCount = totalCount;
   }

   @Override
   public OrderedResultStrategy<TYPE, PagedResult<TYPE>> orderedByAsc(String property)
   {
      sorts.add(new Order(property, Order.Direction.ASC));
      return this;
   }

   @Override
   public OrderedResultStrategy<TYPE, PagedResult<TYPE>> orderedByDesc(String property)
   {
      sorts.add(new Order(property, Order.Direction.DESC));
      return this;
   }

   @Override
   public List<Order> getOrders()
   {
      return Collections.unmodifiableList(sorts);
   }

   @Override
   public PagedResult<TYPE> expand(List<TYPE> elements)
   {
      int fromIndex = Math.min(page * size, elements.size());
      int toIndex = Math.min(fromIndex + size, elements.size());
      List<TYPE> content = elements.subList(fromIndex, toIndex);
      int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalCount / size);

      return PagedResult.<TYPE>builder()
            .withContent(content)
            .withPage(page)
            .withSize(size)
            .withTotalElements(totalCount)
            .withTotalPages(totalPages)
            .build();
   }

   @Override
   public PagedResult<TYPE> expandSingle(TYPE element)
   {
      if (page != 0)
      {
         throw new IllegalStateException("expandSingle can only be used with page = 0");
      }
      return expand(List.of(element));
   }

   @Override
   public PagedResult<TYPE> reduceEmpty()
   {
      return expand(List.of());
   }

   @Override
   public ResultStrategyType getType()
   {
      return ResultStrategyType.PAGED;
   }

   @Override
   public int getPage()
   {
      return page;
   }

   @Override
   public int getSize()
   {
      return size;
   }

   @Override
   public <R> R accept(ResultStrategyVisitor<TYPE, PagedResult<TYPE>, R> visitor, PagedResult<TYPE> page)
   {
      return visitor.visitPaged(this, page);
   }
}

class SliceResultStrategy<TYPE> implements OrderedResultStrategy<TYPE, SliceResult<TYPE>>
{
   private final List<Order> sorts = new ArrayList<>();
   private final int offset;
   private final int limit;

   public SliceResultStrategy(int offset, int limit)
   {
      this.offset = offset;
      this.limit = limit;
   }

   @Override
   public OrderedResultStrategy<TYPE, SliceResult<TYPE>> orderedByAsc(String property)
   {
      sorts.add(new Order(property, Order.Direction.ASC));
      return this;
   }

   @Override
   public OrderedResultStrategy<TYPE, SliceResult<TYPE>> orderedByDesc(String property)
   {
      sorts.add(new Order(property, Order.Direction.DESC));
      return this;
   }

   @Override
   public List<Order> getOrders()
   {
      return Collections.unmodifiableList(sorts);
   }

   @Override
   public SliceResult<TYPE> expand(List<TYPE> elements)
   {
      int fromIndex = Math.min(offset, elements.size());
      int toIndex = Math.min(fromIndex + limit, elements.size());
      boolean hasNext = elements.size() > toIndex;
      List<TYPE> content = elements.subList(fromIndex, toIndex);

      return SliceResult.<TYPE>builder()
            .withContent(content)
            .withOffset(offset)
            .withLimit(limit)
            .withHasNext(hasNext)
            .build();
   }

   @Override
   public SliceResult<TYPE> expandSingle(TYPE element)
   {
      if (offset != 0)
      {
         throw new IllegalStateException("expandSingle can only be used with offset = 0");
      }
      return expand(List.of(element));
   }

   @Override
   public SliceResult<TYPE> reduceEmpty()
   {
      return expand(List.of());
   }

   @Override
   public ResultStrategyType getType()
   {
      return ResultStrategyType.SLICED;
   }

   @Override
   public int getOffset()
   {
      return offset;
   }

   @Override
   public int getLimit()
   {
      return limit;
   }

   @Override
   public <R> R accept(ResultStrategyVisitor<TYPE, SliceResult<TYPE>, R> visitor, SliceResult<TYPE> page)
   {
      return visitor.visitSliced(this, page);
   }
}

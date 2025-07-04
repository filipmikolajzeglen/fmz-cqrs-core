package com.filipmikolajzeglen.cqrs.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a single sort order (property and direction).
 */
@Getter
@RequiredArgsConstructor
public class Order
{
   private final String property;
   private final Direction direction;

   public enum Direction
   {
      ASC, DESC
   }
}
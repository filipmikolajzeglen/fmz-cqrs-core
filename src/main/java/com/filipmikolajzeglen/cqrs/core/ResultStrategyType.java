package com.filipmikolajzeglen.cqrs.core;

/**
 * Enum representing different types of result strategies.
 * <ul>
 *   <li>SINGLE - Single result.</li>
 *   <li>OPTIONAL - Optional result.</li>
 *   <li>LIST - List of results without pagination.</li>
 *   <li>EXIST - Existence check.</li>
 *   <li>COUNT - Count of results.</li>
 *   <li>FIRST - First result.</li>
 *   <li>PAGED - Standard page-based result.</li>
 *   <li>SLICED - Slice-based result.</li>
 * </ul>
 */
public enum ResultStrategyType
{
   SINGLE, OPTIONAL, LIST, EXIST, COUNT, FIRST, PAGED, SLICED
}
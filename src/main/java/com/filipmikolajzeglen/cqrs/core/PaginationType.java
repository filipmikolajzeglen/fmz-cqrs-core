package com.filipmikolajzeglen.cqrs.core;

/**
 * Enum representing different types of pagination strategies.
 * <ul>
 *   <li>SINGLE - Single result pagination.</li>
 *   <li>OPTIONAL - Optional result pagination.</li>
 *   <li>LIST - List of results without pagination.</li>
 *   <li>EXIST - Existence checks pagination.</li>
 *   <li>COUNT - Count of results pagination.</li>
 *   <li>FIRST - First result pagination.</li>
 *   <li>PAGED - Standard page-based pagination.</li>
 *   <li>SLICED - Slice-based pagination.</li>
 * </ul>
 */
public enum PaginationType
{
   SINGLE, OPTIONAL, LIST, EXIST, COUNT, FIRST, PAGED, SLICED
}
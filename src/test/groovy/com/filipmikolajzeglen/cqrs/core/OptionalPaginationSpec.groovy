package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class OptionalPaginationSpec extends Specification {

   def pagination = Pagination.optional()

   def "expandSingle wraps element in Optional"() {
      expect:
      pagination.expandSingle("X") == Optional.of("X")
   }

   def "expandSingle with null returns Optional.empty"() {
      expect:
      pagination.expandSingle(null) == Optional.empty()
   }

   def "expand with empty list returns Optional.empty"() {
      expect:
      pagination.expand([]) == Optional.empty()
   }

   def "expand with one element returns Optional.of"() {
      expect:
      pagination.expand(["X"]) == Optional.of("X")
   }

   def "expand with multiple elements throws IllegalStateException"() {
      when:
      pagination.expand(["A", "B"])

      then:
      thrown(IllegalStateException)
   }

   def "reduceEmpty returns Optional.empty"() {
      expect:
      pagination.reduceEmpty() == Optional.empty()
   }

   def "getType returns OPTIONAL"() {
      expect:
      pagination.getType() == PaginationType.OPTIONAL
   }

   def "accept calls visitor.visitOptional"() {
      given:
      def visitor = Mock(PaginationVisitor)
      def value = Optional.of("X")

      when:
      pagination.accept(visitor, value)

      then:
      1 * visitor.visitOptional(pagination, value)
   }
}
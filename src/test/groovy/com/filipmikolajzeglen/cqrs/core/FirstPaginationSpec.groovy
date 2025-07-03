package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class FirstPaginationSpec extends Specification {

   def pagination = Pagination.first()

   def "expand returns first element as Optional"() {
      expect:
      pagination.expand(["A", "B"]) == Optional.of("A")
   }

   def "expand returns Optional.empty for empty list"() {
      expect:
      pagination.expand([]) == Optional.empty()
   }

   def "expandSingle wraps element in Optional"() {
      expect:
      pagination.expandSingle("X") == Optional.of("X")
   }

   def "expandSingle with null returns Optional.empty"() {
      expect:
      pagination.expandSingle(null) == Optional.empty()
   }

   def "reduceEmpty returns Optional.empty"() {
      expect:
      pagination.reduceEmpty() == Optional.empty()
      pagination.offset == 0
      pagination.limit == 1
   }

   def "getType returns FIRST"() {
      expect:
      pagination.getType() == PaginationType.FIRST
   }

   def "accept calls visitor.visitFirst"() {
      given:
      def visitor = Mock(PaginationVisitor)
      def value = Optional.of("A")

      when:
      pagination.accept(visitor, value)

      then:
      1 * visitor.visitFirst(pagination, value)
   }
}
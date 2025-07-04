package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class FirstResultStrategySpec extends Specification {

   def pagination = ResultStrategy.first()

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
      pagination.getType() == ResultStrategyType.FIRST
   }

   def "accept calls visitor.visitFirst"() {
      given:
      def visitor = Mock(ResultStrategyVisitor)
      def value = Optional.of("A")

      when:
      pagination.accept(visitor, value)

      then:
      1 * visitor.visitFirst(pagination, value)
   }

   def "orderedByAsc and orderedByDesc add Orders"() {
      given:
      def pagination = ResultStrategy.first()
            .orderedByAsc("foo")
            .orderedByDesc("bar")

      expect:
      pagination.getOrders().size() == 2
      pagination.getOrders()[0].property == "foo"
      pagination.getOrders()[0].direction == Order.Direction.ASC
      pagination.getOrders()[1].property == "bar"
      pagination.getOrders()[1].direction == Order.Direction.DESC
   }
}
package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class ListResultStrategySpec extends Specification {

   def pagination = ResultStrategy.all()

   def "expandSingle wraps element in list"() {
      expect:
      pagination.expandSingle("X") == ["X"]
   }

   def "expand returns the same list"() {
      expect:
      pagination.expand(["A", "B"]) == ["A", "B"]
   }

   def "expand with empty list returns empty list"() {
      expect:
      pagination.expand([]) == []
   }

   def "reduceEmpty returns empty list"() {
      expect:
      pagination.reduceEmpty() == []
   }

   def "getType returns LIST"() {
      expect:
      pagination.getType() == ResultStrategyType.LIST
   }

   def "accept calls visitor.visitList"() {
      given:
      def visitor = Mock(ResultStrategyVisitor)
      def value = ["A", "B"]

      when:
      pagination.accept(visitor, value)

      then:
      1 * visitor.visitList(pagination, value)
   }

   def "orderedByAsc and orderedByDesc add Orders"() {
      given:
      def pagination = ResultStrategy.all()
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
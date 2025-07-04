package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class CountResultStrategySpec extends Specification {

   def pagination = ResultStrategy.count()

   def "expand returns size of list as Long"() {
      expect:
      pagination.expand(["A", "B", "C"]) == 3L
   }

   def "expandSingle returns 1 if element is not null"() {
      expect:
      pagination.expandSingle("X") == 1L
   }

   def "expandSingle returns 0 if element is null"() {
      expect:
      pagination.expandSingle(null) == 0L
   }

   def "reduceEmpty returns 0"() {
      expect:
      pagination.reduceEmpty() == 0L
   }

   def "getType returns COUNT"() {
      expect:
      pagination.getType() == ResultStrategyType.COUNT
   }

   def "accept calls visitor.visitCount"() {
      given:
      def visitor = Mock(ResultStrategyVisitor)
      def value = 5L

      when:
      pagination.accept(visitor, value)

      then:
      1 * visitor.visitCount(pagination, value)
   }
}
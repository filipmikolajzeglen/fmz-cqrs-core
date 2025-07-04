package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class PagedResultStrategySpec extends Specification {

   def elements = (1..7).collect { "Item-$it" }
   def pagination = ResultStrategy.paged(1, 3, 7)

   def "expand returns correct page content"() {
      expect:
      pagination.expand(elements).content == ["Item-4", "Item-5", "Item-6"]
   }

   def "expand sets correct totalElements"() {
      expect:
      pagination.expand(elements).totalElements == 7
   }

   def "expand sets correct totalPages"() {
      expect:
      pagination.expand(elements).totalPages == 3
   }

   def "reduceEmpty returns empty paged result"() {
      expect:
      pagination.reduceEmpty().content == []
   }

   def "expandSingle with page = 0 wraps element in first page"() {
      given:
      def pagination = new PagedResultStrategy<String>(0, 5, 1)

      when:
      def result = pagination.expandSingle("Only")

      then:
      with(result) {
         content == ["Only"]
         page == 0
         size == 5
         totalElements == 1
         totalPages == 1
      }
   }

   def "expandSingle throws when page > 0"() {
      given:
      def pagination = new PagedResultStrategy<String>(1, 5, 1)

      when:
      pagination.expandSingle("Only")

      then:
      def e = thrown(IllegalStateException)
      e.message == "expandSingle can only be used with page = 0"
   }

   def "expand with single element and page > 0 returns empty page"() {
      given:
      def pagination = new PagedResultStrategy<String>(1, 5, 1)

      when:
      def result = pagination.expand(["Only"])

      then:
      with(result) {
         content == []
         page == 1
         size == 5
         totalElements == 1
         totalPages == 1
      }
   }

   def "getType returns PAGED"() {
      expect:
      pagination.getType() == ResultStrategyType.PAGED
      pagination.page == 1
      pagination.size == 3
   }

   def "accept calls visitor.visitPaged"() {
      given:
      def visitor = Mock(ResultStrategyVisitor)
      def pagedResult = pagination.expand(elements)

      when:
      pagination.accept(visitor, pagedResult)

      then:
      1 * visitor.visitPaged(pagination, pagedResult)
   }

   def "orderedByAsc and orderedByDesc add Orders"() {
      given:
      def pagination = ResultStrategy.paged(0, 5, 10)
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
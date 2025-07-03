package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class ListPaginationSpec extends Specification {

   def pagination = Pagination.all()

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
      pagination.getType() == PaginationType.LIST
   }

   def "accept calls visitor.visitList"() {
      given:
      def visitor = Mock(PaginationVisitor)
      def value = ["A", "B"]

      when:
      pagination.accept(visitor, value)

      then:
      1 * visitor.visitList(pagination, value)
   }

   def "orderedByAsc and orderedByDesc add sorts"() {
      given:
      def pagination = Pagination.all()
            .orderedByAsc("foo")
            .orderedByDesc("bar")

      expect:
      pagination.getSorts().size() == 2
      pagination.getSorts()[0].property == "foo"
      pagination.getSorts()[0].direction == Sort.Direction.ASC
      pagination.getSorts()[1].property == "bar"
      pagination.getSorts()[1].direction == Sort.Direction.DESC
   }
}
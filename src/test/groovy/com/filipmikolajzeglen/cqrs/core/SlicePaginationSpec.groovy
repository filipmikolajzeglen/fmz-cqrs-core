package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class SlicePaginationSpec extends Specification {

   def elements = ["A", "B", "C", "D", "E"]
   def pagination = Pagination.sliced(1, 2)

   def "expand returns correct slice content"() {
      expect:
      pagination.expand(elements).content == ["B", "C"]
   }

   def "expand sets hasNext to true if more items available"() {
      expect:
      pagination.expand(elements).hasNext
   }

   def "expand sets hasNext to false when no more items"() {
      given:
      def p = Pagination.sliced(3, 3)

      expect:
      !p.expand(elements).hasNext
   }

   def "reduceEmpty returns empty slice result"() {
      expect:
      pagination.reduceEmpty().content == []
   }

   def "expandSingle with offset = 0 wraps element as slice"() {
      given:
      def pagination = new SlicePagination<String>(0, 5)

      when:
      def result = pagination.expandSingle("X")

      then:
      with(result) {
         content == ["X"]
         offset == 0
         limit == 5
         !hasNext
      }
   }

   def "expandSingle throws when offset > 0"() {
      given:
      def pagination = new SlicePagination<String>(1, 5)

      when:
      pagination.expandSingle("X")

      then:
      def e = thrown(IllegalStateException)
      e.message == "expandSingle can only be used with offset = 0"
   }

   def "expand with offset beyond list returns empty content"() {
      given:
      def pagination = new SlicePagination<String>(10, 5)

      when:
      def result = pagination.expand(elements)

      then:
      result.content == []
      !result.hasNext
   }

   def "getType returns SLICED"() {
      expect:
      pagination.getType() == PaginationType.SLICED
      pagination.offset == 1
      pagination.limit == 2
   }

   def "accept calls visitor.visitSliced"() {
      given:
      def visitor = Mock(PaginationVisitor)
      def sliceResult = pagination.expand(elements)

      when:
      pagination.accept(visitor, sliceResult)

      then:
      1 * visitor.visitSliced(pagination, sliceResult)
   }

   def "orderedByAsc and orderedByDesc add sorts"() {
      given:
      def pagination = Pagination.sliced(0, 2)
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
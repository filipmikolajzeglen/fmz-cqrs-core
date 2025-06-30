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
}
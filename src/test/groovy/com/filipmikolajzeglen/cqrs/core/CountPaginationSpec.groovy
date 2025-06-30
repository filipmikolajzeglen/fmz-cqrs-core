package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class CountPaginationSpec extends Specification {

   def pagination = Pagination.count()

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
}
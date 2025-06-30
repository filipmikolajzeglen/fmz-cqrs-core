package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class ExistPaginationSpec extends Specification {

   def pagination = Pagination.exist()

   def "expand returns true if list is not empty"() {
      expect:
      pagination.expand(["A"]) == true
   }

   def "expand returns false if list is empty"() {
      expect:
      pagination.expand([]) == false
   }

   def "expandSingle returns true if element is not null"() {
      expect:
      pagination.expandSingle("X") == true
   }

   def "expandSingle returns false if element is null"() {
      expect:
      pagination.expandSingle(null) == false
   }

   def "reduceEmpty returns false"() {
      expect:
      pagination.reduceEmpty() == false
   }
}
package com.filipmikolajzeglen.cqrs.common

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
   }

   def "asPageable returns correct Pageable"() {
      when:
      def pageable = pagination.asPageable().get()

      then:
      pageable.offset() == 0
      pageable.limit() == 1
      !pageable.requireTotalCount()
   }
}
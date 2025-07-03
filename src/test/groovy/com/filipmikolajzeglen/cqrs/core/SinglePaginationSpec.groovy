package com.filipmikolajzeglen.cqrs.core

import spock.lang.Specification

class SinglePaginationSpec extends Specification {

   def pagination = Pagination.single()

   def "expandSingle returns the same element"() {
      expect:
      pagination.expandSingle("X") == "X"
   }

   def "expand with one element returns that element"() {
      expect:
      pagination.expand(["X"]) == "X"
   }

   def "expand with empty list throws NoSuchElementException"() {
      when:
      pagination.expand([])

      then:
      thrown(NoSuchElementException)
   }

   def "expand with multiple elements throws IllegalStateException"() {
      when:
      pagination.expand(["A", "B"])

      then:
      thrown(IllegalStateException)
   }

   def "reduceEmpty throws NoSuchElementException"() {
      when:
      pagination.reduceEmpty()

      then:
      thrown(NoSuchElementException)
   }

   def "getType returns SINGLE"() {
      expect:
      pagination.getType() == PaginationType.SINGLE
   }

   def "accept calls visitor.visitSingle"() {
      given:
      def visitor = Mock(PaginationVisitor)
      def value = "X"

      when:
      pagination.accept(visitor, value)

      then:
      1 * visitor.visitSingle(pagination, value)
   }
}
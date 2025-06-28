package com.filipmikolajzeglen.cqrs.common

import spock.lang.Specification

class PaginationSpec extends Specification {

   def "default asPageable returns Optional.empty"() {
      given:
      def pagination = new Pagination<Object, Object>() {
         @Override
         Object expand(List<Object> elements) { null }
         @Override
         Object expandSingle(Object element) { null }
         @Override
         Object reduceEmpty() { null }
      }

      expect:
      !pagination.asPageable().isPresent()
   }
}
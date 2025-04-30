package com.filipmikolajzeglen.cqrs.common


import spock.lang.Specification

class QueryHandlerSpec extends Specification {

   def "should handle command and return expected result - #name"(String name, boolean flag) {
      given:
      def query = new DummyEntityQuery(name: name)
      def handler = new DummyEntityQueryHandler()

      when:
      def result = handler.handle(query, Pagination.single())

      then:
      result != null
      result.name == name
      result.flag == flag

      where:
      name     | flag
      'Test Entity 1' | true
      'Test Entity 4' | false
   }

   def "should throw NoSuchElementException"() {
      given:
      def query = new DummyEntityQuery(name: 'Test Entity 5')
      def handler = new DummyEntityQueryHandler()

      when:
      handler.handle(query, Pagination.single())

      then:
      thrown(NoSuchElementException)
   }

}

package com.filipmikolajzeglen.cqrs

import spock.lang.Specification

class QuerySpec extends Specification {

   def 'should execute query and return expected result'() {
      given:

      def query = new CustomQuery()

      when:
      def result = query.perform()

      then:
      result == 42
   }

   class CustomQuery extends Query<Integer> {
      @Override
      Integer perform() {
         return 42
      }
   }
}

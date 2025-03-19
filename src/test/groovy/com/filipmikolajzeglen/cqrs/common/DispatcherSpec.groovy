package com.filipmikolajzeglen.cqrs.common

import spock.lang.Specification

class DispatcherSpec extends Specification {

   def "should execute command and return expected result"() {
      given:
      def handler = new EntityCreateCommandHandler()
      def dispatcher = new Dispatcher([handler], [])

      def command = new EntityCreateCommand(name: "Test Entity")

      when:
      def result = dispatcher.execute(command)

      then:
      result != null
      result.name == "Test Entity"
      result.flag
   }

   def "should perform query and return expected result"() {
      given:
      def handler = new EntityQueryHandler()
      def dispatcher = new Dispatcher([], [handler])

      def query = new EntityQuery(name: "Test Entity 2")

      when:
      def result = dispatcher.perform(query)

      then:
      result != null
      result.name == "Test Entity 2"
      !result.flag
   }

   def "should throw RuntimeException due to lack of handler for command"() {
      given:
      def handler = new EntityCreateCommandHandler()
      def dispatcher = new Dispatcher([handler], [])

      def command = new EntityCommandWithoutHandler()

      when:
      dispatcher.execute(command)

      then:
      def e = thrown(RuntimeException)
      e.getMessage() == "No handler for command class ${command.getClass().name}"
   }

   def "should throw RuntimeException due to lack of handler for query"() {
      given:
      def handler = new EntityQueryHandler()
      def dispatcher = new Dispatcher([], [handler])

      def query = new EntityQueryWithoutHandler()

      when:
      dispatcher.perform(query)

      then:
      def e = thrown(RuntimeException)
      e.getMessage() == "No handler for query class ${query.getClass().name}"
   }
}
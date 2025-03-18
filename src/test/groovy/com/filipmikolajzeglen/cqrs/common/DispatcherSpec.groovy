package com.filipmikolajzeglen.cqrs.common

import spock.lang.Specification
import org.springframework.context.ApplicationContext

class DispatcherSpec extends Specification {

   def "should execute command and return expected result"() {
      given:
      def handler = new EntityCreateCommandHandler()
      def context = Mock(ApplicationContext) {
         getBeansWithAnnotation(Handler) >> ["entityCreateCommandHandler": handler]
      }
      def dispatcher = new Dispatcher(context)
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
      def context = Mock(ApplicationContext) {
         getBeansWithAnnotation(Handler) >> ["entityQueryHandler": handler]
      }
      def dispatcher = new Dispatcher(context)
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
      def context = Mock(ApplicationContext) {
         getBeansWithAnnotation(Handler) >> ["entityCreateHandler": handler]
      }
      def dispatcher = new Dispatcher(context)
      def command = new EntityCommandWithoutHandler()

      when:
      dispatcher.execute(command)

      then:
      def e = thrown(RuntimeException)
      e.getMessage() == 'No handler for command class com.filipmikolajzeglen.cqrs.common.EntityCommandWithoutHandler'
   }

   def "should throw RuntimeException due to lack of handler for query"() {
      given:
      def handler = new EntityQueryHandler()
      def context = Mock(ApplicationContext) {
         getBeansWithAnnotation(Handler) >> ["entityQueryHandler": handler]
      }
      def dispatcher = new Dispatcher(context)
      def query = new EntityQueryWithoutHandler()

      when:
      dispatcher.perform(query)

      then:
      def e = thrown(RuntimeException)
      e.getMessage() == 'No handler for query class com.filipmikolajzeglen.cqrs.common.EntityQueryWithoutHandler'
   }

   def "should throw RuntimeException when handler invocation fails"() {
      given:
      def faultyHandler = new FaultyCommandHandler()
      def context = Mock(ApplicationContext) {
         getBeansWithAnnotation(Handler) >> ["faultyHandler": faultyHandler]
      }
      def dispatcher = new Dispatcher(context)
      def command = new FaultyCommand()

      when:
      dispatcher.execute(command)

      then:
      def e = thrown(RuntimeException)
      e.message.contains("Handler execution failed")
   }
}
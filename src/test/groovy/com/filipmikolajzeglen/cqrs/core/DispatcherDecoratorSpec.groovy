package com.filipmikolajzeglen.cqrs.core

import nl.altindag.log.LogCaptor
import spock.lang.Specification

class DispatcherDecoratorSpec extends Specification {

   def "should log execution of command"() {
      given:
      def logCaptor = LogCaptor.forClass(LoggingCommandInterceptor)
      def handler = new DummyEntityCreateCommandHandler()
      def dispatcher = new DispatcherDecorator(new DispatcherRegistry([handler], []), [new LoggingCommandInterceptor()], [])
      def command = new DummyEntityCreateCommand(name: "Test Entity")

      when:
      dispatcher.execute(command)

      then:
      logCaptor.infoLogs.any {
         it.contains("Executing command 'class com.filipmikolajzeglen.cqrs.core.DummyEntityCreateCommand'")
      }
   }

   def "should log execution of query"() {
      given:
      def logCaptor = LogCaptor.forClass(LoggingQueryInterceptor)
      def handler = new DummyEntityQueryHandler()
      def dispatcher = new DispatcherDecorator(new DispatcherRegistry([], [handler]), [], [new LoggingQueryInterceptor()])
      def query = new DummyEntityQuery(name: "Test Entity 1")

      when:
      dispatcher.perform(query, ResultStrategy.single())

      then:
      logCaptor.infoLogs.any {
         it.contains("Performing query 'class com.filipmikolajzeglen.cqrs.core.DummyEntityQuery'")
      }
   }

   def "should invoke next command in chain"() {
      given:
      def interceptor = new TransactionalCommandInterceptor()
      def command = Stub(Command)
      def invocation = Mock(CommandInvocation)

      when:
      def result = interceptor.intercept(command, invocation)

      then:
      1 * invocation.invoke() >> "OK"
      result == "OK"
   }

   def "should invoke next query in chain"() {
      given:
      def interceptor = new TransactionalQueryInterceptor()
      def query = Stub(Query)
      def invocation = Mock(QueryInvocation)
      def pagination = ResultStrategy.single()

      when:
      def result = interceptor.intercept(query, pagination, invocation)

      then:
      1 * invocation.invoke(pagination) >> "OK"
      result == "OK"
   }

}
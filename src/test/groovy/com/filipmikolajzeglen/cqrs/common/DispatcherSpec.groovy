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
      def e = thrown(NoHandlerException)
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
      def e = thrown(NoHandlerException)
      e.getMessage() == "No handler for query class ${query.getClass().name}"
   }

   def "should execute autonomous command using AutonomousCommandContext"() {
      given:
      def dispatcher = new Dispatcher([], [])
      def command = new TestAutonomousCommand()

      when:
      def result = dispatcher.execute(command)

      then:
      result == "Executed Autonomous Command"
   }

   def "should perform autonomous query using AutonomousQueryContext"() {
      given:
      def dispatcher = new Dispatcher([], [])
      def query = new TestAutonomousQuery()

      when:
      def result = dispatcher.perform(query)

      then:
      result == "Performed Autonomous Query"
   }

   def "should chain autonomous commands and queries inside another autonomous command"() {
      given:
      def dispatcher = new Dispatcher([], [])
      def command = new ChainedAutonomousCommand()

      when:
      def result = dispatcher.execute(command)

      then:
      result == "Main -> SubCommand -> SubQuery"
   }

   def "should chain autonomous queries inside another autonomous query"() {
      given:
      def dispatcher = new Dispatcher([], [])
      def query = new ChainedAutonomousQuery()

      when:
      def result = dispatcher.perform(query)

      then:
      result == "MainQuery -> SubQuery"
   }

   class TestAutonomousCommand extends AutonomousCommand<String> {
      @Override
      protected String execute(AutonomousCommandContext context) {
         assert context != null
         return "Executed Autonomous Command"
      }
   }

   class TestAutonomousQuery extends AutonomousQuery<String> {
      @Override
      protected String perform(AutonomousQueryContext context) {
         assert context != null
         return "Performed Autonomous Query"
      }
   }

   class ChainedAutonomousCommand extends AutonomousCommand<String> {
      @Override
      protected String execute(AutonomousCommandContext context) {
         def subResult = context.execute(new SubAutonomousCommand())
         def queryResult = context.perform(new SubAutonomousQuery())
         return "Main -> ${subResult} -> ${queryResult}"
      }
   }

   class ChainedAutonomousQuery extends AutonomousQuery<String> {
      @Override
      protected String perform(AutonomousQueryContext context) {
         def result = context.perform(new SubAutonomousQuery())
         return "MainQuery -> ${result}"
      }
   }

   class SubAutonomousCommand extends AutonomousCommand<String> {
      @Override
      protected String execute(AutonomousCommandContext context) {
         return "SubCommand"
      }
   }

   class SubAutonomousQuery extends AutonomousQuery<String> {
      @Override
      protected String perform(AutonomousQueryContext context) {
         return "SubQuery"
      }
   }

}
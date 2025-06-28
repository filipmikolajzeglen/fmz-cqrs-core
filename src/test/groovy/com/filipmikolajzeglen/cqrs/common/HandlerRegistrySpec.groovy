package com.filipmikolajzeglen.cqrs.common

import spock.lang.Specification

class HandlerRegistrySpec extends Specification {

   def "should #testName"(String testName, Command command) {
      given:
      def handler = new MyBaseCommandHandler()
      def registry = new HandlerRegistry<Command, CommandHandler>([handler], Command)

      when:
      def resolved = registry.get(command)

      then:
      resolved != null
      resolved instanceof MyBaseCommandHandler

      where:
      testName                                                          | command
      'register and retrieve handler for exact command type'            | new MyBaseCommand()
      'fallback to handler for supertype when exact match is not found' | new MyCommand()
   }

   def "should return null for unregistered command type"() {
      given:
      def otherCommand = new OtherCommand()
      def handler = new MyBaseCommandHandler()
      def registry = new HandlerRegistry<Command, CommandHandler>([handler], Command)

      when:
      def resolved = registry.get(otherCommand)

      then:
      resolved == null
   }

   def "should throw exception if handler does not implement proper generic interface"() {
      given:
      def invalidHandler = new Object() {}

      when:
      new HandlerRegistry<Command<?>, Object>([invalidHandler], Command)

      then:
      def exception = thrown(IllegalArgumentException)
      exception.message =~ "Cannot determine handled type for handler"
   }

   def "should add handler after first retrieval"() {
      given:
      def myCommand = new MyCommand()
      def handler = new MyBaseCommandHandler()
      def registry = new HandlerRegistry<Command, CommandHandler>([handler], Command)

      expect:
      !registry.handlers.get(MyCommand)

      when:
      def firstRetrieval = registry.get(myCommand)

      then:
      def registeredHandler = registry.handlers.get(MyCommand)
      registeredHandler == firstRetrieval

      and:
      def secondRetrieval = registry.get(myCommand)
      registeredHandler == secondRetrieval
   }

   def "should return null for unregistered command type and not register it"() {
      given:
      def otherCommand = new OtherCommand()
      def handler = new MyBaseCommandHandler()
      def registry = new HandlerRegistry<Command, CommandHandler>([handler], Command)

      when:
      def resolved = registry.get(otherCommand)

      then:
      resolved == null
      !registry.handlers.containsKey(OtherCommand)
   }

   def "should register correctly handlers for different command types"() {
      given:
      def myCommand = new MyCommand()
      def otherCommand = new OtherCommand()
      def handler1 = new MyBaseCommandHandler()
      def handler2 = new OtherBaseCommandHandler()
      def registry = new HandlerRegistry<Command, CommandHandler>([handler1, handler2], Command)

      expect:
      !registry.handlers.containsKey(MyCommand)
      !registry.handlers.containsKey(OtherCommand)

      when:
      def handlerForMyCommand = registry.get(myCommand)
      def handlerForOtherCommand = registry.get(otherCommand)

      then:
      handlerForMyCommand != handlerForOtherCommand
      registry.handlers.containsKey(MyCommand)
      registry.handlers.containsKey(OtherCommand)
   }

   def "should extract handled type from parameterized interface"() {
      given:
      def handler = new ParameterizedCommandHandler()
      def registry = new HandlerRegistry<Command, CommandHandler>([handler], Command)

      when:
      def resolved = registry.get(new ParameterizedCommand())

      then:
      resolved == handler
   }

   static class ParameterizedCommand extends Command<String> {}
   static class ParameterizedCommandHandler implements CommandHandler<ParameterizedCommand, String> {
      @Override
      String handle(ParameterizedCommand command) { "ok" }
   }
}
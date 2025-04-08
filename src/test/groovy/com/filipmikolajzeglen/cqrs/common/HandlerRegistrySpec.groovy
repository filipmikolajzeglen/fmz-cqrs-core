package com.filipmikolajzeglen.cqrs.common

import spock.lang.Specification

class HandlerRegistrySpec extends Specification {

   def "should #testName"(String testName, Class command) {
      given:
      def handler = new MyBaseCommandHandler()
      def registry = new HandlerRegistry<Command<?>, CommandHandler<?, ?>>([handler], Command)

      when:
      def resolved = registry.getHandler(command)

      then:
      resolved != null
      resolved instanceof MyBaseCommandHandler

      where:
      testName                                                          | command
      'register and retrieve handler for exact command type'            | MyBaseCommand.class
      'fallback to handler for supertype when exact match is not found' | MyCommand.class
   }

   def "should return null for unregistered command type"() {
      given:
      def handler = new MyBaseCommandHandler()
      def registry = new HandlerRegistry<Command<?>, CommandHandler<?, ?>>([handler], Command)

      when:
      def resolved = registry.getHandler(OtherCommand.class)

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
      def handler = new MyBaseCommandHandler()
      def registry = new HandlerRegistry<Command<?>, CommandHandler<?, ?>>([handler], Command)

      expect:
      !registry.handlers.get(MyCommand)

      when:
      def firstRetrieval = registry.getHandler(MyCommand)

      then:
      def registeredHandler = registry.handlers.get(MyCommand)
      registeredHandler == firstRetrieval

      and:
      def secondRetrieval = registry.getHandler(MyCommand)
      registeredHandler == secondRetrieval
   }

   def "should return null for unregistered command type and not register it"() {
      given:
      def handler = new MyBaseCommandHandler()
      def registry = new HandlerRegistry<Command<?>, CommandHandler<?, ?>>([handler], Command)

      when:
      def resolved = registry.getHandler(OtherCommand.class)

      then:
      resolved == null
      !registry.handlers.containsKey(OtherCommand.class)
   }

   def "should register correctly handlers for different command types"() {
      given:
      def handler1 = new MyBaseCommandHandler()
      def handler2 = new OtherBaseCommandHandler()
      def registry = new HandlerRegistry<Command<?>, CommandHandler<?, ?>>([handler1, handler2], Command)

      expect:
      !registry.handlers.containsKey(MyCommand.class)
      !registry.handlers.containsKey(OtherCommand.class)

      when:
      def handlerForMyCommand = registry.getHandler(MyCommand.class)
      def handlerForOtherCommand = registry.getHandler(OtherCommand.class)

      then:
      handlerForMyCommand != handlerForOtherCommand
      registry.handlers.containsKey(MyCommand.class)
      registry.handlers.containsKey(OtherCommand.class)
   }
}
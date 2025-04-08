package com.filipmikolajzeglen.cqrs.common

import spock.lang.Specification

class HandlerRegistrySpec extends Specification {

   def "should #testName"(String testName, Class command) {
      given:
      def handler = new MyBaseCommandHandler()
      def registry = new HandlerRegistry<Command<?>, CommandHandler<?, ?>>(List.of(handler))

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
      def registry = new HandlerRegistry<Command<?>, CommandHandler<?, ?>>(List.of(handler))

      when:
      def resolved = registry.getHandler(OtherCommand.class)

      then:
      resolved == null
   }

   def "should throw exception if handler does not implement proper generic interface"() {
      given:
      def invalidHandler = new Object() {}

      when:
      new HandlerRegistry<Command<?>, Object>(List.of(invalidHandler))

      then:
      def exception = thrown(IllegalArgumentException)
      exception.message =~ "Cannot determine handled type for handler"
   }
}
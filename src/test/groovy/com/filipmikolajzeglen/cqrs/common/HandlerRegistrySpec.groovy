package com.filipmikolajzeglen.cqrs.common

import spock.lang.Specification

class HandlerRegistrySpec extends Specification {

   def "should register and retrieve handler for exact command type"() {
      given:
      def handler = new MyCommandHandler()
      def registry = new HandlerRegistry<Command<?>, CommandHandler<?, ?>>(List.of(handler))

      when:
      def resolved = registry.getHandler(MyCommand.class)

      then:
      resolved != null
      resolved instanceof MyCommandHandler
   }

   def "should return null for unregistered command type"() {
      given:
      def handler = new MyCommandHandler()
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
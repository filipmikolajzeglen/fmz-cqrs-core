package com.filipmikolajzeglen.cqrs.core


import spock.lang.Specification

class CommandHandlerSpec extends Specification {

   def "should handle command and return expected result"() {
      given:
      def command = new DummyEntityCreateCommand(name: "Test Entity", flag: true)
      def handler = new DummyEntityCreateCommandHandler()

      when:
      def result = handler.handle(command)

      then:
      result != null
      result.name == "Test Entity"
      result.flag
   }

}

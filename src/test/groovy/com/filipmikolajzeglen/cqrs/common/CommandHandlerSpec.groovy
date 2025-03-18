package com.filipmikolajzeglen.cqrs.common


import spock.lang.Specification

class CommandHandlerSpec extends Specification {

   def "should handle command and return expected result"() {
      given:
      def command = new EntityCreateCommand(name: "Test Entity", flag: true)
      def handler = new EntityCreateCommandHandler()

      when:
      def result = handler.handle(command)

      then:
      result != null
      result.name == "Test Entity"
      result.flag
   }

}

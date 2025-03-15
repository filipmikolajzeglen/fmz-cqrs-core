package com.filipmikolajzeglen.cqrs.common


import spock.lang.Specification

class CommandSpec extends Specification {

   def 'should execute command and return expected result'() {
      given:
      def command = new CustomCommand()

      when:
      def result = command.execute()

      then:
      result == "Executed"
   }

   private class CustomCommand extends Command<String> {
      @Override
      String execute() {
         return "Executed"
      }
   }
}

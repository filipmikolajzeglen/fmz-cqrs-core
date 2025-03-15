package com.filipmikolajzeglen.cqrs.common

import com.filipmikolajzeglen.cqrs.common.Command
import com.filipmikolajzeglen.cqrs.common.Dispatcher
import com.filipmikolajzeglen.cqrs.common.Query
import spock.lang.Specification

class DispatcherSpec extends Specification {

   def 'should execute command using dispatcher'() {
      given:
      def dispatcher = new Dispatcher()
      def command = new SimpleCommand()

      when:
      def result = dispatcher.execute(command)

      then:
      result == "Dispatched Execution"
   }

   def 'should execute query using dispatcher'() {
      given:
      def dispatcher = new Dispatcher()
      def query = new SimpleQuery()

      when:
      def result = dispatcher.perform(query)

      then:
      result == true
   }

   private class SimpleCommand extends Command<String> {
      @Override
      String execute() {
         return "Dispatched Execution"
      }
   }

   private class SimpleQuery extends Query<Boolean> {
      @Override
      Boolean perform() {
         return true
      }
   }
}
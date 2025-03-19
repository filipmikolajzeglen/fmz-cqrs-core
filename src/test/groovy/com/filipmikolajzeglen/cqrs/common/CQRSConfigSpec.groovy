package com.filipmikolajzeglen.cqrs.common

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spock.lang.Specification

class CQRSConfigSpec extends Specification {

   def "should create dispatcher bean"() {
      given: "A Spring application context with CQRSConfig and test handlers"
      ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig, CQRSConfig)

      when: "The dispatcher bean is retrieved from the context"
      def dispatcher = context.getBean(Dispatcher)

      then: "The dispatcher bean should not be null"
      dispatcher != null
   }

   @Configuration
   static class TestConfig {
      @Bean
      CommandHandler<TestCommand, String> testCommandHandler() {
         return new CommandHandler<TestCommand, String>() {
            @Override
            String handle(TestCommand command) {
               return "Handled: " + command.input
            }
         }
      }

      @Bean
      QueryHandler<TestQuery, String> testQueryHandler() {
         return new QueryHandler<TestQuery, String>() {
            @Override
            String handle(TestQuery query) {
               return "Result for: " + query.input
            }
         }
      }
   }

   static class TestCommand extends Command<String> {
      String input
      TestCommand(String input) {
         this.input = input
      }
   }

   static class TestQuery extends Query<String> {
      String input
      TestQuery(String input) {
         this.input = input
      }
   }
}

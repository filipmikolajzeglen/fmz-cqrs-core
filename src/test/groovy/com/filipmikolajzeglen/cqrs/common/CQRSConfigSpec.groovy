package com.filipmikolajzeglen.cqrs.common

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import spock.lang.Specification

class CQRSConfigSpec extends Specification {

   def "should create dispatcher bean"() {
      given: "A Spring application context with CQRSConfig"
      ApplicationContext context = new AnnotationConfigApplicationContext(CQRSConfig)

      when: "The dispatcher bean is retrieved from the context"
      def dispatcher = context.getBean(Dispatcher)

      then: "The dispatcher bean should not be null"
      dispatcher != null
   }
}
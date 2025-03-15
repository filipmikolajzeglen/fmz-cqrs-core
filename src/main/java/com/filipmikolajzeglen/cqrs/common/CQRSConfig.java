package com.filipmikolajzeglen.cqrs.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CQRSConfig
{
   @Bean
   public Dispatcher dispatcher()
   {
      return new Dispatcher();
   }
}
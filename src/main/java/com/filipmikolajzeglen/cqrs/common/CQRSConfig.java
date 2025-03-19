package com.filipmikolajzeglen.cqrs.common;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CQRSConfig
{
   @Bean
   public Dispatcher dispatcher(
         List<CommandHandler<?, ?>> commandHandlers,
         List<QueryHandler<?, ?>> queryHandlers)
   {
      return new Dispatcher(commandHandlers, queryHandlers);
   }
}

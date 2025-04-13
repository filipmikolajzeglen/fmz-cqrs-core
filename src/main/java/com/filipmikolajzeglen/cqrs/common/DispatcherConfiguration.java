package com.filipmikolajzeglen.cqrs.common;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DispatcherConfiguration
{
   @Bean
   public Dispatcher dispatcher(
         List<CommandHandler<?, ?>> commandHandlers,
         List<QueryHandler<?, ?>> queryHandlers)
   {
      Dispatcher dispatcher = new Dispatcher(commandHandlers, queryHandlers);
      return new DispatcherDecorator(
            dispatcher,
            List.of(
                  new LoggingCommandInterceptor(),
                  new TransactionalCommandInterceptor()
            ),
            List.of(
                  new LoggingQueryInterceptor(),
                  new TransactionalQueryInterceptor()
            )
      );
   }
}
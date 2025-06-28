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
   public DispatcherDecorator dispatcher(
         List<CommandHandler<?, ?>> commandHandlers,
         List<QueryHandler<?, ?>> queryHandlers)
   {
      return new DispatcherDecorator(
            new DispatcherImpl(commandHandlers, queryHandlers),
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
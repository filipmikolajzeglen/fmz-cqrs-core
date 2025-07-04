package com.filipmikolajzeglen.cqrs.core;

import java.util.List;

/**
 * Decorator for {@link Dispatcher} that applies command and query interceptors.
 */
public class DispatcherDecorator implements Dispatcher
{
   private final Dispatcher dispatcher;
   private final List<CommandInterceptor> commandInterceptors;
   private final List<QueryInterceptor> queryInterceptors;

   public DispatcherDecorator(Dispatcher dispatcher,
         List<CommandInterceptor> commandInterceptors,
         List<QueryInterceptor> queryInterceptors)
   {
      this.dispatcher = dispatcher;
      this.commandInterceptors = commandInterceptors;
      this.queryInterceptors = queryInterceptors;
   }

   /**
    * Executes the command, applying all registered command interceptors.
    */
   @SuppressWarnings("unchecked")
   @Override
   public <COMMAND extends Command<? extends TYPE>, TYPE> TYPE execute(COMMAND command)
   {
      return invokeCommandInterceptors((Command<TYPE>) command, 0);
   }

   private <TYPE> TYPE invokeCommandInterceptors(Command<TYPE> command, int index)
   {
      if (index >= commandInterceptors.size())
      {
         return dispatcher.execute(command);
      }
      CommandInterceptor interceptor = commandInterceptors.get(index);
      return interceptor.intercept(command, () -> invokeCommandInterceptors(command, index + 1));
   }

   /**
    * Performs the query, applying all registered query interceptors.
    */
   @SuppressWarnings("unchecked")
   @Override
   public <QUERY extends Query<? extends TYPE>, TYPE, RESULT> RESULT perform(QUERY query, ResultStrategy<TYPE, RESULT> resultStrategy)
   {
      return invokeQueryInterceptors((Query<TYPE>) query, resultStrategy, 0);
   }

   private <TYPE, RESULT> RESULT invokeQueryInterceptors(Query<TYPE> query, ResultStrategy<TYPE, RESULT> resultStrategy, int index)
   {
      if (index >= queryInterceptors.size())
      {
         return dispatcher.perform(query, resultStrategy);
      }
      QueryInterceptor interceptor = queryInterceptors.get(index);
      return interceptor.intercept(query, resultStrategy, p -> invokeQueryInterceptors(query, p, index + 1));
   }
}
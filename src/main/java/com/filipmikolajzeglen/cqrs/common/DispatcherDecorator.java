package com.filipmikolajzeglen.cqrs.common;

import java.util.List;

public class DispatcherDecorator extends Dispatcher
{
   private final Dispatcher dispatcher;
   private final List<CommandInterceptor> commandInterceptors;
   private final List<QueryInterceptor> queryInterceptors;

   public DispatcherDecorator(Dispatcher dispatcher,
         List<CommandInterceptor> commandInterceptors,
         List<QueryInterceptor> queryInterceptors)
   {
      super(List.of(), List.of());
      this.dispatcher = dispatcher;
      this.commandInterceptors = commandInterceptors;
      this.queryInterceptors = queryInterceptors;
   }

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

   @SuppressWarnings("unchecked")
   @Override
   public <QUERY extends Query<? extends TYPE>, TYPE> TYPE perform(QUERY query)
   {
      return invokeQueryInterceptors((Query<TYPE>) query, 0);
   }

   private <TYPE> TYPE invokeQueryInterceptors(Query<TYPE> query, int index)
   {
      if (index >= queryInterceptors.size())
      {
         return dispatcher.perform(query);
      }
      QueryInterceptor interceptor = queryInterceptors.get(index);
      return interceptor.intercept(query, () -> invokeQueryInterceptors(query, index + 1));
   }
}

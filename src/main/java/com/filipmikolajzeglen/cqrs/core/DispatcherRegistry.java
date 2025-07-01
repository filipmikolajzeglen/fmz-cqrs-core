package com.filipmikolajzeglen.cqrs.core;

import java.util.List;

@SuppressWarnings("unchecked")
public class DispatcherRegistry implements Dispatcher
{
   private final HandlerRegistry<Command<?>, CommandHandler<?, ?>> commandHandlers;
   private final HandlerRegistry<Query<?>, QueryHandler<?, ?>> queryHandlers;

   public DispatcherRegistry(List<CommandHandler<?, ?>> commandHandlers, List<QueryHandler<?, ?>> queryHandlers)
   {
      this.commandHandlers = new HandlerRegistry<>(commandHandlers, Command.class);
      this.queryHandlers = new HandlerRegistry<>(queryHandlers, Query.class);
   }

   @Override
   public <COMMAND extends Command<? extends TYPE>, TYPE> TYPE execute(COMMAND command)
   {
      if (command instanceof AutonomousCommand<?>)
      {
         return ((AutonomousCommand<TYPE>) command).execute(new AutonomousCommandContext(this));
      }
      CommandHandler<COMMAND, TYPE> handler = (CommandHandler<COMMAND, TYPE>) commandHandlers.get(command);
      if (handler == null)
      {
         throw NoHandlerException.of(command);
      }
      return handler.handle(command);
   }

   @Override
   public <QUERY extends Query<? extends TYPE>, TYPE, PAGE> PAGE perform(QUERY query, Pagination<TYPE, PAGE> pagination)
   {
      if (query instanceof AutonomousQuery<?>)
      {
         return ((AutonomousQuery<TYPE>) query).perform(new AutonomousQueryContext(this), pagination);
      }
      QueryHandler<QUERY, TYPE> handler = (QueryHandler<QUERY, TYPE>) queryHandlers.get(query);
      if (handler == null)
      {
         throw NoHandlerException.of(query);
      }
      return handler.handle(query, pagination);
   }
}
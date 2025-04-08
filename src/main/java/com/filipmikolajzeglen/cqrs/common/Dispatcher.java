package com.filipmikolajzeglen.cqrs.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("unchecked")
@Service
public class Dispatcher
{
   private final HandlerRegistry<Command<?>, CommandHandler<?, ?>> commandHandlers;
   private final HandlerRegistry<Query<?>, QueryHandler<?, ?>> queryHandlers;

   @Autowired
   public Dispatcher(List<CommandHandler<?, ?>> commandHandlers, List<QueryHandler<?, ?>> queryHandlers)
   {
      this.commandHandlers = new HandlerRegistry<>(commandHandlers, Command.class);
      this.queryHandlers = new HandlerRegistry<>(queryHandlers, Query.class);
   }

   public <COMMAND extends Command<? extends TYPE>, TYPE> TYPE execute(COMMAND command)
   {
      CommandHandler<COMMAND, TYPE> handler = (CommandHandler<COMMAND, TYPE>) commandHandlers.getHandler(
            (Class<? extends Command<?>>) command.getClass());

      if (handler == null)
      {
         throw new RuntimeException("No handler for command " + command.getClass());
      }
      return handler.handle(command);
   }

   public <QUERY extends Query<? extends TYPE>, TYPE> TYPE perform(QUERY query)
   {
      QueryHandler<QUERY, TYPE> handler = (QueryHandler<QUERY, TYPE>) queryHandlers.getHandler(
            (Class<? extends Query<?>>) query.getClass());

      if (handler == null)
      {
         throw new RuntimeException("No handler for query " + query.getClass());
      }
      return handler.handle(query);
   }
}
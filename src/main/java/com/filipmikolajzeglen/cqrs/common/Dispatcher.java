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

   public <QUERY extends Query<? extends TYPE>, TYPE> TYPE perform(QUERY query)
   {
      if (query instanceof AutonomousQuery<?>)
      {
         return ((AutonomousQuery<TYPE>) query).perform(new AutonomousQueryContext(this));
      }

      QueryHandler<QUERY, TYPE> handler = (QueryHandler<QUERY, TYPE>) queryHandlers.get(query);
      if (handler == null)
      {
         throw NoHandlerException.of(query);
      }
      return handler.handle(query);
   }
}
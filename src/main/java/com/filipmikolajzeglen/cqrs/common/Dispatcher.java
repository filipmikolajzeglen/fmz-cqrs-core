package com.filipmikolajzeglen.cqrs.common;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("unchecked")
@Service
@NoArgsConstructor
public class Dispatcher
{
   private final Map<Class<? extends Command<?>>, CommandHandler<?, ?>> commandHandlers = new ConcurrentHashMap<>();
   private final Map<Class<? extends Query<?>>, QueryHandler<?, ?>> queryHandlers = new ConcurrentHashMap<>();

   @Autowired
   public Dispatcher(List<CommandHandler<?, ?>> commandHandlers, List<QueryHandler<?, ?>> queryHandlers)
   {
      for (CommandHandler<?, ?> handler : commandHandlers)
      {
         Class<?> commandType = (Class<?>) ((ParameterizedType) handler.getClass()
               .getGenericInterfaces()[0]).getActualTypeArguments()[0];
         this.commandHandlers.put((Class<? extends Command<?>>) commandType, handler);
      }

      for (QueryHandler<?, ?> handler : queryHandlers)
      {
         Class<?> queryType = (Class<?>) ((ParameterizedType) handler.getClass()
               .getGenericInterfaces()[0]).getActualTypeArguments()[0];
         this.queryHandlers.put((Class<? extends Query<?>>) queryType, handler);
      }
   }

   public <COMMAND extends Command<TYPE>, TYPE> TYPE execute(COMMAND command)
   {
      CommandHandler<COMMAND, TYPE> handler = (CommandHandler<COMMAND, TYPE>) commandHandlers.get(command.getClass());
      if (handler == null)
      {
         throw new RuntimeException("No handler for command " + command.getClass());
      }
      return handler.handle(command);
   }

   public <QUERY extends Query<TYPE>, TYPE> TYPE perform(QUERY query)
   {
      QueryHandler<QUERY, TYPE> handler = (QueryHandler<QUERY, TYPE>) queryHandlers.get(query.getClass());
      if (handler == null)
      {
         throw new RuntimeException("No handler for query " + query.getClass());
      }
      return handler.handle(query);
   }
}
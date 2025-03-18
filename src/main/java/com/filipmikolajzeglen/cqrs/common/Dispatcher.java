package com.filipmikolajzeglen.cqrs.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class Dispatcher
{
   private final Map<Class<? extends Command<?>>, Function<Command<?>, ?>> commandHandlers = new ConcurrentHashMap<>();
   private final Map<Class<? extends Query<?>>, Function<Query<?>, ?>> queryHandlers = new ConcurrentHashMap<>();

   @Autowired
   public Dispatcher(ApplicationContext context)
   {
      Map<String, Object> handlerBeans = context.getBeansWithAnnotation(Handler.class);
      for (Object handler : handlerBeans.values())
      {
         for (var method : handler.getClass().getMethods())
         {
            if (method.getName().equals("handle") && method.getParameterCount() == 1)
            {
               Class<?> paramType = method.getParameterTypes()[0];
               if (Command.class.isAssignableFrom(paramType))
               {
                  commandHandlers.put(safeCast(paramType), command -> invokeHandler(method, handler, command));
               }
               else if (Query.class.isAssignableFrom(paramType))
               {
                  queryHandlers.put(safeCast(paramType), query -> invokeHandler(method, handler, query));
               }
            }
         }
      }
   }

   @SuppressWarnings("unchecked")
   private <TYPE> Class<? extends TYPE> safeCast(Class<?> clazz)
   {
      return (Class<? extends TYPE>) clazz;
   }

   private Object invokeHandler(java.lang.reflect.Method method, Object handler, Object param)
   {
      try
      {
         return method.invoke(handler, param);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Handler execution failed", e);
      }
   }

   @SuppressWarnings("unchecked")
   public <COMMAND extends Command<TYPE>, TYPE> TYPE execute(COMMAND command)
   {
      Function<Command<?>, ?> handler = commandHandlers.get(command.getClass());
      if (handler == null)
      {
         throw new RuntimeException("No handler for command " + command.getClass());
      }
      return (TYPE) handler.apply(command);
   }

   @SuppressWarnings("unchecked")
   public <QUERY extends Query<TYPE>, TYPE> TYPE perform(QUERY query)
   {
      Function<Query<?>, ?> handler = queryHandlers.get(query.getClass());
      if (handler == null)
      {
         throw new RuntimeException("No handler for query " + query.getClass());
      }
      return (TYPE) handler.apply(query);
   }
}
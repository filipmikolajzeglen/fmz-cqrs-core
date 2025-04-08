package com.filipmikolajzeglen.cqrs.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerRegistry<K, T>
{
   private final Map<Class<? extends K>, T> handlers = new ConcurrentHashMap<>();

   public HandlerRegistry(List<T> handlerList)
   {
      for (T handler : handlerList)
      {
         Class<? extends K> key = extractHandledType(handler);
         handlers.put(key, handler);
      }
   }

   public T getHandler(Class<? extends K> key)
   {
      T handler = handlers.get(key);
      if (handler != null)
      {
         return handler;
      }

      for (Map.Entry<Class<? extends K>, T> entry : handlers.entrySet())
      {
         if (entry.getKey().isAssignableFrom(key))
         {
            return entry.getValue();
         }
      }

      return null;
   }

   @SuppressWarnings("unchecked")
   private Class<? extends K> extractHandledType(T handler)
   {
      for (Type type : handler.getClass().getGenericInterfaces())
      {
         if (type instanceof ParameterizedType parameterized)
         {
            Type actualType = parameterized.getActualTypeArguments()[0];
            if (actualType instanceof Class<?> actualClass)
            {
               return (Class<? extends K>) actualClass;
            }
         }
      }
      throw new IllegalArgumentException("Cannot determine handled type for handler: " + handler);
   }
}
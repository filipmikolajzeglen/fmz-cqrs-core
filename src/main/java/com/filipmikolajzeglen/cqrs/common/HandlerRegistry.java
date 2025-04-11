package com.filipmikolajzeglen.cqrs.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public class HandlerRegistry<K, T>
{
   private final Map<Class<? extends K>, T> handlers = new ConcurrentHashMap<>();

   public HandlerRegistry(List<T> handlerList, Class<?> baseClass)
   {
      for (T handler : handlerList)
      {
         Class<? extends K> key = extractHandledType(handler, baseClass);
         handlers.put(key, handler);
      }
   }

   @SuppressWarnings("unchecked")
   public T get(K instance)
   {
      Class<? extends K> key = (Class<? extends K>) instance.getClass();
      T handler = handlers.get(key);
      if (handler != null)
      {
         return handler;
      }

      for (Map.Entry<Class<? extends K>, T> entry : handlers.entrySet())
      {
         if (entry.getKey().isAssignableFrom(key))
         {
            handlers.put(key, entry.getValue());
            return entry.getValue();
         }
      }

      return null;
   }

   @SuppressWarnings("unchecked")
   private Class<? extends K> extractHandledType(T handler, Class<?> baseClass)
   {
      for (Type type : handler.getClass().getGenericInterfaces())
      {
         if (type instanceof ParameterizedType parameterized)
         {
            Type actualType = parameterized.getActualTypeArguments()[0];
            if (baseClass.isAssignableFrom((Class<?>) actualType))
            {
               return (Class<? extends K>) actualType;
            }
         }
      }
      throw new IllegalArgumentException("Cannot determine handled type for handler: " + handler);
   }
}
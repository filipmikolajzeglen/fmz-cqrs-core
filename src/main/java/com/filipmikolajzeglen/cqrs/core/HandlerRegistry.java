package com.filipmikolajzeglen.cqrs.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
class HandlerRegistry<KEY, TYPE>
{
   private final Map<Class<? extends KEY>, TYPE> handlers = new ConcurrentHashMap<>();

   public HandlerRegistry(List<TYPE> handlerList, Class<?> baseClass)
   {
      for (TYPE handler : handlerList)
      {
         Class<? extends KEY> key = extractHandledType(handler, baseClass);
         handlers.put(key, handler);
      }
   }

   @SuppressWarnings("unchecked")
   public TYPE get(KEY instance)
   {
      Class<? extends KEY> key = (Class<? extends KEY>) instance.getClass();
      TYPE handler = handlers.get(key);
      if (handler != null)
      {
         return handler;
      }

      for (Map.Entry<Class<? extends KEY>, TYPE> entry : handlers.entrySet())
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
   private Class<? extends KEY> extractHandledType(TYPE handler, Class<?> baseClass)
   {
      for (Type type : handler.getClass().getGenericInterfaces())
      {
         if (type instanceof ParameterizedType parameterized)
         {
            Type actualType = parameterized.getActualTypeArguments()[0];
            if (actualType instanceof Class<?> actualClass && baseClass.isAssignableFrom(actualClass))
            {
               return (Class<? extends KEY>) actualClass;
            }

            if (actualType instanceof ParameterizedType pt && pt.getRawType() instanceof Class<?> rawClass
                  && baseClass.isAssignableFrom(rawClass))
            {
               return (Class<? extends KEY>) rawClass;
            }
         }
      }
      throw new IllegalArgumentException("Cannot determine handled type for handler: " + handler);
   }
}
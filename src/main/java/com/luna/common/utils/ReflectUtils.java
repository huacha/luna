/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <p>Date: 13-2-23 下午1:25
 * <p>Version: 1.0
 */
public class ReflectUtils {

    /**
     * 得到指定类型的指定位置的泛型实参
     *
     * @param clazz
     * @param index
     * @param <T>
     * @return
     */
    public static <T> Class<T> findParameterizedType(Class<?> clazz, int index) {
        Type parameterizedType = clazz.getGenericSuperclass();
        //CGLUB subclass target object(泛型在父类上)
        if (!(parameterizedType instanceof ParameterizedType)) {
            parameterizedType = clazz.getSuperclass().getGenericSuperclass();
        }
        if (!(parameterizedType instanceof  ParameterizedType)) {
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) parameterizedType).getActualTypeArguments();
        if (actualTypeArguments == null || actualTypeArguments.length == 0) {
            return null;
        }
        return (Class<T>) actualTypeArguments[0];
    }
    
    public static String getGetterMethodName(Object target, String fieldName)
            throws NoSuchMethodException {
        String methodName = "get" + StringUtils.capitalize(fieldName);

        try {
            target.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException ex) {
            methodName = "is" + StringUtils.capitalize(fieldName);

            target.getClass().getDeclaredMethod(methodName);
        }

        return methodName;
    }

    public static String getSetterMethodName(String fieldName) {
        return "set" + StringUtils.capitalize(fieldName);
    }

    /**
     * get method value by name.
     * 
     * @param target
     *            Object
     * @param methodName
     *            method name
     * @return object
     * @throws Exception
     *             ex
     */
    public static Object getMethodValue(Object target, String methodName)
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        Method method = target.getClass().getDeclaredMethod(methodName);

        return method.invoke(target);
    }

    /**
     * set method value by name.
     * 
     * @param target
     *            Object
     * @param methodName
     *            method name
     * @param methodValue
     *            method value
     * @throws Exception
     *             ex
     */
    public static void setMethodValue(Object target, String methodName,
            Object methodValue) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Method method = target.getClass().getDeclaredMethod(methodName,
                methodValue.getClass());

        method.invoke(target, methodValue);
    }

    public static Object getFieldValue(Object target, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        return getFieldValue(target, fieldName, true);
    }

    public static Object getFieldValue(Object target, String fieldName,
            boolean isForce) throws NoSuchFieldException,
            IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(isForce);

        return field.get(target);
    }

    /**
     * convert reflection exception to unchecked.
     * 
     * @param e
     *            Exception
     * @return RuntimeException;
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(
            Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else if (e instanceof IllegalAccessException
                || e instanceof NoSuchMethodException
                || e instanceof NoSuchFieldException) {
            return new IllegalArgumentException("Reflection Exception.", e);
        } else if (e instanceof InvocationTargetException) {
            Throwable targetException = ((InvocationTargetException) e)
                    .getTargetException();

            if (targetException instanceof RuntimeException) {
                return (RuntimeException) targetException;
            } else {
                return new RuntimeException("Reflection Exception.",
                        targetException);
            }
        }

        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    public static Class<?> getOriginalClass(Class<?> clz) {
        Class<?> superclass = clz;

        while (superclass.getName().indexOf("_$$_jvst") != -1) {
            superclass = superclass.getSuperclass();

            if (superclass == null) {
                return superclass;
            }
        }

        return superclass;
    }
}

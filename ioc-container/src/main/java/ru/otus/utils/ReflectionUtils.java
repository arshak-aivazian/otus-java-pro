package ru.otus.utils;

import ru.otus.exception.ReflectionException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static List<Method> getFilteredDeclaredMethods(Class<?> clazz, Predicate<Method> predicate) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(it -> predicate.test(it))
                .collect(Collectors.toList());
    }

    public static <T> T createNewInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static Object invokeMethod(Object object, Method method, Object... params) {
        try {
            method.setAccessible(true);
            return method.invoke(object, params);
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static Method findMethod(Class<?> configClass, Class<?> returnType) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(m -> m.getReturnType().equals(returnType))
                .findFirst()
                .orElseThrow(() -> new ReflectionException(String.format("method with return type [%s] not found", returnType.getName())));
    }
}

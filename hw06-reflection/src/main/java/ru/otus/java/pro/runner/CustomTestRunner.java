package ru.otus.java.pro.runner;

import ru.otus.java.pro.annotation.After;
import ru.otus.java.pro.annotation.Before;
import ru.otus.java.pro.annotation.Test;
import ru.otus.java.pro.exception.TooManyAnnotationsException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CustomTestRunner {

    public TestResult run(Class<?> clazz) {
        Map<Class<?>, List<Method>> annotatedMethods = getAnnotatedMethods(clazz);
        List<Method> beforeMethods = annotatedMethods.getOrDefault(Before.class, List.of());
        List<Method> testMethods = annotatedMethods.getOrDefault(Test.class, List.of());
        List<Method> afterMethods = annotatedMethods.getOrDefault(After.class, List.of());

        TestResult testResult = new TestResult();
        for (Method method : testMethods) {
            Object testObject = createNewInstance(clazz);
            testResult.incrementTestCount();
            if (testObject != null) {
                if (!silentInvoke(testObject, beforeMethods)) {
                    testResult.incrementFail();
                    continue;
                }

                if (!silentInvoke(testObject, method)) {
                    testResult.incrementFail();
                    continue;
                }

                if (!silentInvoke(testObject, afterMethods)) {
                    testResult.incrementFail();
                    continue;
                }

                testResult.incrementSuccess();
            }
        }

        return testResult;
    }

    private Object createNewInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean silentInvoke(Object testObject, List<Method> methods) {
        for (Method method : methods) {
            if (!silentInvoke(testObject, method)) {
                return false;
            }
        }
        return true;
    }

    private boolean silentInvoke(Object object, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(object);
            return true;
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
            return false;
        }
    }

    private Map<Class<?>, List<Method>> getAnnotatedMethods(Class<?> clazz) {
        Map<Class<?>, List<Method>> annotatedMethods = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            Class<?> customTestAnnotation = getCustomTestAnnotation(method);
            if (customTestAnnotation != null) {
                List<Method> methods = annotatedMethods.getOrDefault(customTestAnnotation, new ArrayList<>());
                methods.add(method);
                annotatedMethods.put(customTestAnnotation, methods);
            }
        }

        return annotatedMethods;
    }

    private Class<?> getCustomTestAnnotation(Method method) {
        List<Annotation> annotations = Arrays.stream(method.getAnnotations())
                .filter(it -> it.annotationType().equals(Test.class)
                        || it.annotationType().equals(Before.class)
                        || it.annotationType().equals(After.class))
                .collect(Collectors.toList());

        if (annotations.size() > 1) {
            throw new TooManyAnnotationsException("only one annotation expected");
        }

        return annotations.get(0).annotationType();
    }
}

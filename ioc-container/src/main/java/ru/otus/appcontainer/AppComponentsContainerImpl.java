package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.exception.IllegalNumberOfComponentException;
import ru.otus.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> components = appComponents.stream()
                .filter(it -> it.getClass().equals(componentClass)
                        || componentClass.isAssignableFrom(it.getClass()))
                .collect(Collectors.toList());

        if (components.size() == 1) {
            return (C) components.get(0);
        }

        throw new IllegalNumberOfComponentException(String.format("found %s components with type %s", components.size(), componentClass.getName()));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var result = findComponentByName(componentName);
        if (result == null) {
            throw new IllegalNumberOfComponentException("not found component with name " + componentName);
        }
        return (C) result;
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        checkNameDuplication(configClass);
        Object configObject = ReflectionUtils.createNewInstance(configClass);
        ReflectionUtils.getFilteredDeclaredMethods(configClass,
                        method -> method.getAnnotation(AppComponent.class) != null)
                .forEach(method -> createNewComponentByConfigMethod(configObject, method));
    }

    private Object createNewComponentByConfigMethod(Object configObject, Method method) {
        String componentName = getComponentName(method);
        var component = findComponentByName(componentName);
        if (component != null) {
            return component;
        }

        Parameter[] parameters = method.getParameters();
        Object[] paramObjects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object componentByParameter = findComponentByParameter(parameter);
            if (componentByParameter == null) {
                Method methodByParamName = ReflectionUtils.findMethod(configObject.getClass(), parameter.getType());
                componentByParameter = createNewComponentByConfigMethod(configObject, methodByParamName);
            }
            paramObjects[i] = componentByParameter;
        }
        component = ReflectionUtils.invokeMethod(configObject, method, paramObjects);
        putIntoContext(component, componentName);
        return component;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private void checkNameDuplication(Class<?> configClass) {
        Set<String> names = new HashSet<>();
        for (Method declaredMethod : configClass.getDeclaredMethods()) {
            String componentName = declaredMethod.getAnnotation(AppComponent.class).name();
            if (names.contains(componentName)) {
                throw new IllegalArgumentException();
            } else {
                names.add(componentName);
            }
        }
    }

    private Object findFirstComponent(Class<?> componentClass) {
        return appComponents.stream()
                .filter(it -> it.getClass().equals(componentClass)
                        || componentClass.isAssignableFrom(it.getClass()))
                .findFirst()
                .orElse(null);
    }

    private Object findComponentByName(String componentName) {
        return appComponentsByName.get(componentName);
    }

    private Object findComponentByParameter(Parameter parameter) {
        return findFirstComponent(parameter.getName(), parameter.getType());
    }

    private Object findFirstComponent(String name, Class<?> clazz) {
        var component = findComponentByName(name);
        return component == null
                ? findFirstComponent(clazz)
                : component;
    }

    private String getComponentName(Method method) {
        String nameByAnnotation = method.getAnnotation(AppComponent.class).name();
        return nameByAnnotation == null || nameByAnnotation.isEmpty()
                ? method.getName()
                : nameByAnnotation;
    }

    private void putIntoContext(Object component, String name) {
        appComponentsByName.put(name, component);
        appComponents.add(component);
    }
}

package io.github.alyphen.immaterial_realm.server.event;

import java.lang.reflect.Method;

import static java.lang.String.format;

public class InvalidEventHandlerException extends Exception {

    private Class<?> listenerClass;
    private Method method;

    public InvalidEventHandlerException(Class<?> listenerClass, Method method) {
        this(format("Invalid EventHandler in %s: %s", listenerClass.getCanonicalName(), method.getName()), listenerClass, method);
    }

    public InvalidEventHandlerException(String message, Class<?> listenerClass, Method method) {
        super(message);
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public InvalidEventHandlerException(String message, Throwable cause, Class<?> listenerClass, Method method) {
        super(message, cause);
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public InvalidEventHandlerException(Throwable cause, Class<?> listenerClass, Method method) {
        super(cause);
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public InvalidEventHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<?> listenerClass, Method method) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.listenerClass = listenerClass;
        this.method = method;
    }

    public Class<?> getListenerClass() {
        return listenerClass;
    }

    public Method getMethod() {
        return method;
    }

}

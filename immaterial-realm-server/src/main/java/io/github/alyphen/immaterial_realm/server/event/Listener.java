package io.github.alyphen.immaterial_realm.server.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Listener {

    private Object listener;
    private Method eventHandler;

    public Listener(Object listener, Method eventHandler) {
        this.listener = listener;
        this.eventHandler = eventHandler;
    }

    public void onEvent(Event event) throws InvocationTargetException, IllegalAccessException {
        eventHandler.invoke(listener, event);
    }

}

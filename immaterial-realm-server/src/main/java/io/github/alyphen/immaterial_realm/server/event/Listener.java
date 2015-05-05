package io.github.alyphen.immaterial_realm.server.event;

import io.github.alyphen.immaterial_realm.server.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Listener {

    private Plugin plugin;
    private Object listener;
    private Method eventHandler;

    public Listener(Plugin plugin, Object listener, Method eventHandler) {
        this.plugin = plugin;
        this.listener = listener;
        this.eventHandler = eventHandler;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Object getListener() {
        return listener;
    }

    public Method getEventHandler() {
        return eventHandler;
    }

    public void onEvent(Event event) throws InvocationTargetException, IllegalAccessException {
        eventHandler.invoke(listener, event);
    }

}

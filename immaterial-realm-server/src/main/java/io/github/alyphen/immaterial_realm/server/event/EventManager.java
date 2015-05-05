package io.github.alyphen.immaterial_realm.server.event;

import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import static io.github.alyphen.immaterial_realm.server.event.EventHandlerPriority.*;
import static io.github.alyphen.immaterial_realm.common.util.ReflectionUtils.isSubclassOf;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

public class EventManager {

    private ImmaterialRealmServer server;

    private final Map<Class<? extends Event>, Map<EventHandlerPriority, List<Listener>>> listeners;

    public EventManager(ImmaterialRealmServer server) {
        this.server = server;
        listeners = new ConcurrentHashMap<>();
    }

    public void addListener(Object listener) throws InvalidEventHandlerException {
        for (Method method : listener.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                if (method.getParameterCount() == 1) {
                    if (isSubclassOf(method.getParameterTypes()[0], Event.class)) {
                        EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                        Class<? extends Event> event = method.getParameterTypes()[0].asSubclass(Event.class);
                        if (!listeners.containsKey(event)) {
                            listeners.put(event, Collections.synchronizedMap(new EnumMap<>(EventHandlerPriority.class)));
                        }
                        EventHandlerPriority priority = eventHandler.priority();
                        if (!listeners.get(event).containsKey(priority)) {
                            listeners.get(event).put(priority, Collections.synchronizedList(new ArrayList<>()));
                        }
                        listeners.get(event).get(priority).add(new Listener(listener, method));
                    } else {
                        throw new InvalidEventHandlerException(format("Invalid EventHandler in %s: %s: Parameter is not a subclass of Event", listener.getClass().getCanonicalName(), method.getName()), listener.getClass(), method);
                    }
                } else {
                    throw new InvalidEventHandlerException(format("Invalid EventHandler in %s: %s: Parameter count is not equal to 1", listener.getClass().getCanonicalName(), method.getName()), listener.getClass(), method);
                }
            }
        }
    }

    public void onEvent(Event event) {
        synchronized (listeners) {
            Class<? extends Event> eventType = event.getClass();
            if (!listeners.containsKey(eventType)) return;
            synchronized (listeners.get(eventType)) {
                if (listeners.get(eventType).containsKey(VERY_LOW))
                    listeners.get(eventType).get(VERY_LOW).stream().forEach(listener -> {
                        try {
                            listener.onEvent(event);
                        } catch (InvocationTargetException | IllegalAccessException exception) {
                            server.getLogger().log(SEVERE, "Failed to invoke event handler", exception);
                        }
                    });
                if (listeners.get(eventType).containsKey(LOW))
                    listeners.get(eventType).get(LOW).stream().forEach(listener -> {
                        try {
                            listener.onEvent(event);
                        } catch (InvocationTargetException | IllegalAccessException exception) {
                            server.getLogger().log(SEVERE, "Failed to invoke event handler", exception);
                        }
                    });
                if (listeners.get(eventType).containsKey(NORMAL))
                    listeners.get(eventType).get(NORMAL).stream().forEach(listener -> {
                        try {
                            listener.onEvent(event);
                        } catch (InvocationTargetException | IllegalAccessException exception) {
                            server.getLogger().log(SEVERE, "Failed to invoke event handler", exception);
                        }
                    });
                if (listeners.get(eventType).containsKey(HIGH))
                    listeners.get(eventType).get(HIGH).stream().forEach(listener -> {
                        try {
                            listener.onEvent(event);
                        } catch (InvocationTargetException | IllegalAccessException exception) {
                            server.getLogger().log(Level.SEVERE, "Failed to invoke event handler", exception);
                        }
                    });
                if (listeners.get(eventType).containsKey(VERY_HIGH))
                    listeners.get(eventType).get(VERY_HIGH).stream().forEach(listener -> {
                        try {
                            listener.onEvent(event);
                        } catch (InvocationTargetException | IllegalAccessException exception) {
                            server.getLogger().log(Level.SEVERE, "Failed to invoke event handler", exception);
                        }
                    });
            }
        }
    }

}

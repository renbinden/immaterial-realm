package io.github.lucariatias.amethyst.common.object;

import java.util.HashMap;
import java.util.Map;

public class WorldObjectFactory {

    private static long nextId;
    private static Map<String, WorldObjectInitializer<?>> initializers;

    static {
        initializers = new HashMap<>();
    }

    public static <T extends WorldObject> void registerObjectInitializer(String type, WorldObjectInitializer<T> initializer) {
        initializers.put(type, initializer);
    }

    public static WorldObject createObject(String type) {
        if (initializers.containsKey(type))
            return initializers.get(type).initialize(nextId++);
        else
            return null;
    }

}

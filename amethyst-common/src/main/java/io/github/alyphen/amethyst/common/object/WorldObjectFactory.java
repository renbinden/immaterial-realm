package io.github.alyphen.amethyst.common.object;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WorldObjectFactory {

    private static long id;
    private static Map<String, WorldObjectInitializer> initializers;

    static {
        initializers = new HashMap<>();
    }

    public static void registerObjectInitializer(String type, WorldObjectInitializer initializer) {
        initializers.put(type, initializer);
    }

    public static WorldObject createObject(String type) {
        if (initializers.containsKey(type))
            return initializers.get(type).initialize(id++);
        else
            return null;
    }

    public static Collection<WorldObjectInitializer> getObjectInitializers() {
        return initializers.values();
    }
}

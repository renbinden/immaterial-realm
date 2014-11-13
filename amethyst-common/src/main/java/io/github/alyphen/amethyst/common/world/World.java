package io.github.alyphen.amethyst.common.world;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static io.github.alyphen.amethyst.common.util.FileUtils.loadMetadata;

public class World {

    private static Map<String, World> worlds;

    static {
        worlds = new HashMap<>();
    }

    public static World getWorld(String name) {
        return worlds.get(name);
    }

    public static Collection<World> getWorlds() {
        return worlds.values();
    }

    private String name;
    private Map<String, WorldArea> areas;

    private World(String name) {
        this.name = name;
        areas = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    private Map<String, WorldArea> getAreas() {
        return areas;
    }

    public WorldArea getArea(String name) {
        return getAreas().get(name);
    }

    public void addArea(WorldArea area) {
        getAreas().put(area.getName(), area);
    }

    public void removeArea(WorldArea area) {
        if (area == null) return;
        getAreas().remove(area.getName());
    }

    public static World load(File directory) throws IOException, ClassNotFoundException {
        File metadataFile = new File(directory, "world.json");
        Map<String, Object> metadata = loadMetadata(metadataFile);
        World world = new World((String) metadata.get("name"));
        File areaDirectory = new File(directory, "areas");
        for (File file : areaDirectory.listFiles(File::isDirectory)) {
            world.addArea(WorldArea.load(world, file));
        }
        worlds.put(world.getName(), world);
        return world;
    }

    public static World create(String name) {
        World world = new World(name);
        worlds.put(name, world);
        return world;
    }

}

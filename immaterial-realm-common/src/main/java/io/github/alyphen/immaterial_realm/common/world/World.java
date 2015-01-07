package io.github.alyphen.immaterial_realm.common.world;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static io.github.alyphen.immaterial_realm.common.util.FileUtils.loadMetadata;
import static io.github.alyphen.immaterial_realm.common.util.FileUtils.saveMetadata;

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

    public Collection<WorldArea> getAreas() {
        return areas.values();
    }

    public WorldArea getArea(String name) {
        return areas.get(name);
    }

    public void addArea(WorldArea area) {
        areas.put(area.getName(), area);
    }

    public void removeArea(WorldArea area) {
        if (area == null) return;
        areas.remove(area.getName());
    }

    public void onTick() {
        for (WorldArea area : getAreas()) {
            area.onTick();
        }
    }

    public void save(File directory) throws IOException {
        File metadataFile = new File(directory, "world.json");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", getName());
        File areaDirectory = new File(directory, "areas");
        for (Map.Entry<String, WorldArea> entry : areas.entrySet()) {
            entry.getValue().save(new File(areaDirectory, entry.getKey()));
        }
        saveMetadata(metadata, metadataFile);
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

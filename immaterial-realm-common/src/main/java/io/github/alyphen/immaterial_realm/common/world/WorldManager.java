package io.github.alyphen.immaterial_realm.common.world;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static io.github.alyphen.immaterial_realm.common.util.FileUtils.loadMetadata;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;

public class WorldManager {

    private ImmaterialRealm immaterialRealm;

    private Map<String, World> worlds;

    public WorldManager(ImmaterialRealm immaterialRealm) {
        this.immaterialRealm = immaterialRealm;
        worlds = new HashMap<>();
    }

    public void loadWorlds() throws IOException {
        File worldDirectory = new File("./worlds");
        for (File file : worldDirectory.listFiles(File::isDirectory)) {
            loadWorld(file);
        }
    }

    public World loadWorld(File directory) throws IOException {
        File metadataFile = new File(directory, "world.json");
        Map<String, Object> metadata = loadMetadata(metadataFile);
        World world = new World(immaterialRealm, (String) metadata.get("name"));
        File areaDirectory = new File(directory, "areas");
        for (File file : areaDirectory.listFiles(File::isDirectory)) {
            world.addArea(world.loadArea(file));
        }
        worlds.put(world.getName(), world);
        return world;
    }

    public World createWorld(String name) {
        World world = new World(immaterialRealm, name);
        worlds.put(name, world);
        return world;
    }

    public World getWorld(String name) {
        return worlds.get(name);
    }

    public Collection<World> getWorlds() {
        return worlds.values();
    }

    public void saveDefaultWorlds() throws IOException {
        File worldDirectory = new File("./worlds");
        File defaultWorldDirectory = new File(worldDirectory, "default");
        if (!worldDirectory.isDirectory()) {
            worldDirectory.delete();
        }
        if (!worldDirectory.exists()) {
            defaultWorldDirectory.mkdirs();
            copy(getClass().getResourceAsStream("/worlds/default/world.json"), get(new File(defaultWorldDirectory, "world.json").getPath()));
            File defaultWorldAreasDirectory = new File(defaultWorldDirectory, "areas");
            File defaultWorldDefaultAreaDirectory = new File(defaultWorldAreasDirectory, "default");
            defaultWorldDefaultAreaDirectory.mkdirs();
            copy(getClass().getResourceAsStream("/worlds/default/areas/default/area.json"), get(new File(defaultWorldDefaultAreaDirectory, "area.json").getPath()));
        }
    }
    
}

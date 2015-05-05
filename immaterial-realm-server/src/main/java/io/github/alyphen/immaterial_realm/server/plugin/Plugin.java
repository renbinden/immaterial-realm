package io.github.alyphen.immaterial_realm.server.plugin;

import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static io.github.alyphen.immaterial_realm.common.util.FileUtils.loadMetadata;
import static io.github.alyphen.immaterial_realm.common.util.FileUtils.saveMetadata;

public abstract class Plugin {

    private ImmaterialRealmServer server;

    private Logger logger;

    private String name;
    private String version;
    private File configFile;
    private Map<String, Object> config;
    private File dataFolder;

    public void initialise(ImmaterialRealmServer server, String name, String version) {
        this.server = server;
        logger = Logger.getLogger(getClass().getName());
        this.name = name;
        this.version = version;
    }

    public ImmaterialRealmServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public void onEnable() {}

    public void onDisable() {}

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, Object> getConfig() throws IOException {
        if (configFile == null) {
            File configDirectory = new File("./config");
            if (configDirectory.exists() && !configDirectory.isDirectory()) {
                configDirectory.delete();
            }
            if (!configDirectory.exists()) {
                configDirectory.mkdirs();
            }
            configFile = new File(configDirectory, getName() + ".json");
            if (!configFile.exists()) configFile.createNewFile();
        }
        if (config == null) config = loadMetadata(configFile);
        return config;
    }

    public void saveConfig() throws IOException {
        saveMetadata(config, configFile);
    }

    public Map<String, Object> getDefaultConfig() {
        return new HashMap<>();
    }

    public void saveDefaultConfig() throws IOException {
        config = getDefaultConfig();
        saveConfig();
    }

    public File getDataFolder() {
        if (dataFolder == null) dataFolder = new File("./data/" + getName());
        if (dataFolder.exists() && !dataFolder.isDirectory()) dataFolder.delete();
        if (!dataFolder.exists()) dataFolder.mkdirs();
        return dataFolder;
    }

}

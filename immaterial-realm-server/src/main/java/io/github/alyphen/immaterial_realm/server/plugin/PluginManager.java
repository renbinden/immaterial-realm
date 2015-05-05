package io.github.alyphen.immaterial_realm.server.plugin;

import io.github.alyphen.immaterial_realm.common.util.FileUtils;
import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;
import io.github.alyphen.immaterial_realm.server.event.plugin.PluginDisableEvent;
import io.github.alyphen.immaterial_realm.server.event.plugin.PluginEnableEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static java.util.logging.Level.WARNING;

public class PluginManager {
    
    private ImmaterialRealmServer server;

    private Map<String, Plugin> plugins;
    
    public PluginManager(ImmaterialRealmServer server) {
        this.server = server;
        plugins = new ConcurrentHashMap<>();
        try {
            loadPlugins();
        } catch (PluginAlreadyExistsException | InvalidPluginException exception) {
            server.getLogger().log(WARNING, "Failed to load plugin", exception);
        }
    }

    private void loadPlugins() throws PluginAlreadyExistsException, InvalidPluginException {
        File pluginsDirectory = new File("./plugins");
        if (pluginsDirectory.exists() && !pluginsDirectory.isDirectory()) pluginsDirectory.delete();
        if (!pluginsDirectory.exists()) pluginsDirectory.mkdirs();
        for (File file : pluginsDirectory.listFiles(pathname -> {
            return pathname.getName().endsWith(".jar");
        })) {
            loadPlugin(file);
        }
    }

    public void disablePlugins() {
        for (Map.Entry<String, Plugin> entry : plugins.entrySet()) {
            disablePlugin(entry.getValue());
        }
    }

    public Plugin loadPlugin(File file) throws PluginAlreadyExistsException, InvalidPluginException {
        try {
            JarFile jarFile;
            jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            String name = null;
            String version = null;
            String mainClass = null;
            while (entries.hasMoreElements()) {
                JarEntry element = entries.nextElement();
                if (element.getName().equalsIgnoreCase("plugin.json")) {
                    Map<String, Object> pluginMetadata = FileUtils.loadMetadata(jarFile.getInputStream(element));
                    name = pluginMetadata.containsKey("name") ? (String) pluginMetadata.get("name") : null;
                    version = pluginMetadata.containsKey("version") ? (String) pluginMetadata.get("version") : null;
                    mainClass = pluginMetadata.containsKey("main") ? (String) pluginMetadata.get("main") : null;
                    break;
                }
            }
            if (name != null && version != null && mainClass != null) {
                ClassLoader loader = URLClassLoader.newInstance(new URL[] {file.toURI().toURL()}, this.getClass().getClassLoader());
                Class<?> clazz = Class.forName(mainClass, true, loader);
                for (Class<?> subclazz : clazz.getClasses()) {
                    Class.forName(subclazz.getName(), true, loader);
                }
                Class<? extends Plugin> pluginClass = clazz.asSubclass(Plugin.class);
                Plugin plugin = pluginClass.newInstance();
                plugin.initialise(server, name, version);
                PluginEnableEvent event = new PluginEnableEvent(plugin);
                if (!event.isCancelled()) {
                    addPlugin(plugin);
                    plugin.onEnable();
                }
                return plugin;
            } else {
                throw new InvalidPluginException(file.getName() + " is not a valid plugin! Make sure name, version and main are specified in plugin.json");
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException exception) {
            throw new InvalidPluginException(file.getName() + " is not a valid plugin!", exception);
        }
    }

    public void addPlugin(Plugin plugin) throws PluginAlreadyExistsException {
        if (!plugins.containsKey(plugin.getName().toLowerCase())) {
            plugins.put(plugin.getName().toLowerCase(), plugin);
        } else {
            throw new PluginAlreadyExistsException();
        }
    }

    public void disablePlugin(Plugin plugin) {
        PluginDisableEvent event = new PluginDisableEvent(plugin);
        server.getEventManager().onEvent(event);
        plugin.onDisable();
        server.getEventManager().removeListeners(plugin);
        plugins.remove(plugin.getName().toLowerCase());
    }
    
}

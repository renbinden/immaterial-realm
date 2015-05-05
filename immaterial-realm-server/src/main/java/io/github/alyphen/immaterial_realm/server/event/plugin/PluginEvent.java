package io.github.alyphen.immaterial_realm.server.event.plugin;

import io.github.alyphen.immaterial_realm.server.event.Event;
import io.github.alyphen.immaterial_realm.server.plugin.Plugin;

public class PluginEvent extends Event {

    private Plugin plugin;

    public PluginEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}

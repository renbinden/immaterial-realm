package io.github.alyphen.immaterial_realm.server.event.plugin;

import io.github.alyphen.immaterial_realm.server.plugin.Plugin;

public class PluginDisableEvent extends PluginEvent {

    public PluginDisableEvent(Plugin plugin) {
        super(plugin);
    }

}

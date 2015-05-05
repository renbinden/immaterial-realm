package io.github.alyphen.immaterial_realm.server.event.plugin;

import io.github.alyphen.immaterial_realm.server.event.Cancellable;
import io.github.alyphen.immaterial_realm.server.plugin.Plugin;

public class PluginEnableEvent extends PluginEvent implements Cancellable {

    private boolean cancelled;

    public PluginEnableEvent(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}

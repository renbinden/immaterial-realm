package io.github.alyphen.immaterial_realm.server.event;

public interface Cancellable {

    public boolean isCancelled();

    public void setCancelled(boolean cancelled);

}

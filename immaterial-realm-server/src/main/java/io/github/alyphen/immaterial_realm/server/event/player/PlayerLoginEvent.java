package io.github.alyphen.immaterial_realm.server.event.player;

import io.github.alyphen.immaterial_realm.common.player.Player;

public class PlayerLoginEvent extends PlayerEvent {

    private boolean firstLogin;
    private boolean successful;
    private String failMessage;

    public PlayerLoginEvent(Player player, boolean firstLogin, boolean successful, String failMessage) {
        super(player);
        this.firstLogin = firstLogin;
        this.successful = successful;
        this.failMessage = failMessage;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }

}

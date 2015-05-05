package io.github.alyphen.immaterial_realm.server.event.chat;

import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannel;
import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.server.event.Cancellable;
import io.github.alyphen.immaterial_realm.server.event.Event;

public class ChatEvent extends Event implements Cancellable {

    private boolean cancelled;
    private Player player;
    private Character character;
    private ChatChannel channel;
    private String message;

    public ChatEvent(Player player, Character character, ChatChannel channel, String message) {
        this.player = player;
        this.character = character;
        this.channel = channel;
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public Character getCharacter() {
        return character;
    }

    public ChatChannel getChannel() {
        return channel;
    }

    public void setChannel(ChatChannel channel) {
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

package io.github.alyphen.amethyst.client.chat;

import io.github.alyphen.amethyst.client.AmethystClient;
import io.github.alyphen.amethyst.common.packet.serverbound.chat.PacketServerboundChatMessage;

import java.util.HashMap;
import java.util.Map;

public class ChatManager {

    private AmethystClient client;

    private Map<String, ChatChannel> channels;
    private ChatChannel currentChannel;

    public ChatManager(AmethystClient client) {
        this.client = client;
        channels = new HashMap<>();
    }

    public void addChannel(ChatChannel channel) {
        channels.put(channel.getName(), channel);
    }

    public void setChannel(ChatChannel channel) {
        if (!channels.containsValue(channel)) addChannel(channel);
        currentChannel = channel;
    }

    public ChatChannel getChannel(String name) {
        return channels.get(name);
    }

    public ChatChannel getChannel() {
        return currentChannel;
    }

    public void sendMessage(String message) {
        client.getNetworkManager().sendPacket(new PacketServerboundChatMessage(getChannel() != null ? getChannel().getName() : "", message));
    }

}

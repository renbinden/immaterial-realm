package io.github.alyphen.amethyst.client.chat;

import io.github.alyphen.amethyst.client.AmethystClient;
import io.github.alyphen.amethyst.common.chat.ChatChannel;
import io.github.alyphen.amethyst.common.packet.serverbound.chat.PacketServerboundGlobalChatMessage;
import io.github.alyphen.amethyst.common.packet.serverbound.chat.PacketServerboundLocalChatMessage;

import java.util.*;

public class ChatManager {

    private AmethystClient client;

    private Map<String, ChatChannel> channels;
    private List<ChatChannel> channelList;
    private ChatChannel currentChannel;

    public ChatManager(AmethystClient client) {
        this.client = client;
        channels = new HashMap<>();
        channelList = new ArrayList<>();
    }

    public void addChannel(ChatChannel channel) {
        channels.put(channel.getName(), channel);
        channelList.add(channel);
        channelList.sort(new Comparator<ChatChannel>() {
            @Override
            public int compare(ChatChannel o1, ChatChannel o2) {
                return o1.compareTo(o2);
            }
        });
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

    public List<ChatChannel> listChannels() {
        return channelList;
    }

    public void sendMessage(String message) {
        if (getChannel() != null)
            if (getChannel().getRadius() >= 0)
                client.getNetworkManager().sendPacket(new PacketServerboundLocalChatMessage(getChannel().getName(), message));
            else
                client.getNetworkManager().sendPacket(new PacketServerboundGlobalChatMessage(getChannel().getName(), message));
    }

}

package io.github.alyphen.immaterial_realm.client.chat;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannel;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.chat.PacketServerboundGlobalChatMessage;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.chat.PacketServerboundLocalChatMessage;

import java.util.*;

public class ChatManager {

    private ImmaterialRealmClient client;

    private Map<String, ChatChannel> channels;
    private List<ChatChannel> channelList;
    private ChatChannel currentChannel;

    public ChatManager(ImmaterialRealmClient client) {
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

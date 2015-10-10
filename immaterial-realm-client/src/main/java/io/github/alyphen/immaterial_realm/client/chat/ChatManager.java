package io.github.alyphen.immaterial_realm.client.chat;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannel;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.chat.PacketServerboundGlobalChatMessage;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.chat.PacketServerboundLocalChatMessage;

public class ChatManager {

    private ImmaterialRealmClient client;

    private ChatChannel currentChannel;

    public ChatManager(ImmaterialRealmClient client) {
        this.client = client;
    }

    public void setChannel(ChatChannel channel) {
        currentChannel = channel;
    }

    public ChatChannel getChannel() {
        return currentChannel;
    }

    public void sendMessage(String message) {
        if (getChannel() != null)
            if (getChannel().getRadius() >= 0)
                client.getNetworkManager().sendPacket(new PacketServerboundLocalChatMessage(getChannel().getName(), message));
            else
                client.getNetworkManager().sendPacket(new PacketServerboundGlobalChatMessage(getChannel().getName(), message));
    }

}

package io.github.alyphen.amethyst.common.packet.login;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketLoginStatus extends Packet {
    
    private boolean successful;

    public PacketLoginStatus(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

}

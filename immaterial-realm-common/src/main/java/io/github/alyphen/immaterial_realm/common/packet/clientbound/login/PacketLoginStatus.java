package io.github.alyphen.immaterial_realm.common.packet.clientbound.login;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketLoginStatus extends Packet {
    
    private boolean successful;

    public PacketLoginStatus(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

}

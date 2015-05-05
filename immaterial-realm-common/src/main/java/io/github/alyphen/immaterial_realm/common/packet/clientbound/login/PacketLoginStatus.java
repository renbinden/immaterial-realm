package io.github.alyphen.immaterial_realm.common.packet.clientbound.login;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketLoginStatus extends Packet {
    
    private boolean successful;
    private String failMessage;

    public PacketLoginStatus(boolean successful, String failMessage) {
        this.successful = successful;
        this.failMessage = failMessage;
    }

    public PacketLoginStatus(boolean successful) {
        this(successful, successful ? "" : "Login unsuccessful");
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

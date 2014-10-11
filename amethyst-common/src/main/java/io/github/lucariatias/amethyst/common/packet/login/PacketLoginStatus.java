package io.github.lucariatias.amethyst.common.packet.login;

public class PacketLoginStatus {
    
    private boolean successful;

    public PacketLoginStatus(boolean successful) {
        this.successful = successful;
    }

    public boolean isSuccessful() {
        return successful;
    }

}

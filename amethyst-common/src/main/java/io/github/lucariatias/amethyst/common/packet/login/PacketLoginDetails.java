package io.github.lucariatias.amethyst.common.packet.login;

import io.github.lucariatias.amethyst.common.packet.Packet;

public class PacketLoginDetails extends Packet {

    private String playerName;
    private byte[] encryptedPasswordHash;
    private boolean signUp;

    public PacketLoginDetails(String playerName, byte[] encryptedPasswordHash, boolean signUp) {
        this.playerName = playerName;
        this.encryptedPasswordHash = encryptedPasswordHash;
        this.signUp = signUp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public byte[] getEncryptedPasswordHash() {
        return encryptedPasswordHash;
    }

    public boolean isSignUp() {
        return signUp;
    }
}

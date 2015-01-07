package io.github.alyphen.immaterial_realm.common.packet.serverbound.login;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketLoginDetails extends Packet {

    private String playerName;
    private byte[] encryptedPassword;
    private boolean signUp;

    public PacketLoginDetails(String playerName, byte[] encryptedPassword, boolean signUp) {
        this.playerName = playerName;
        this.encryptedPassword = encryptedPassword;
        this.signUp = signUp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public byte[] getEncryptedPassword() {
        return encryptedPassword;
    }

    public boolean isSignUp() {
        return signUp;
    }
}

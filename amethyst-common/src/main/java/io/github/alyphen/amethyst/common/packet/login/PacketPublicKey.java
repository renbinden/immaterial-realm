package io.github.alyphen.amethyst.common.packet.login;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketPublicKey extends Packet {

    private byte[] encodedPublicKey;

    public PacketPublicKey(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }

    public byte[] getEncodedPublicKey() {
        return encodedPublicKey;
    }

}

package io.github.alyphen.amethyst.common.packet.serverbound.login;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketServerboundPublicKey extends Packet {

    private byte[] encodedPublicKey;

    public PacketServerboundPublicKey(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }

    public byte[] getEncodedPublicKey() {
        return encodedPublicKey;
    }

}

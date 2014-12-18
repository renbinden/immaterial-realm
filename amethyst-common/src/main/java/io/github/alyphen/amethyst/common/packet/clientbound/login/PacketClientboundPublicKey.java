package io.github.alyphen.amethyst.common.packet.clientbound.login;

import io.github.alyphen.amethyst.common.packet.Packet;

public class PacketClientboundPublicKey extends Packet {

    private byte[] encodedPublicKey;

    public PacketClientboundPublicKey(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }

    public byte[] getEncodedPublicKey() {
        return encodedPublicKey;
    }

}

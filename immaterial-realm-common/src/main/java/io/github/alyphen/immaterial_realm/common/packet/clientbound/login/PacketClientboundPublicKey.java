package io.github.alyphen.immaterial_realm.common.packet.clientbound.login;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketClientboundPublicKey extends Packet {

    private byte[] encodedPublicKey;

    public PacketClientboundPublicKey(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }

    public byte[] getEncodedPublicKey() {
        return encodedPublicKey;
    }

}

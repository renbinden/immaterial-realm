package io.github.alyphen.immaterial_realm.common.packet.clientbound.entity;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

public class PacketEntityDespawn extends Packet {

    private long entityId;

    public PacketEntityDespawn(long entityId) {
        this.entityId = entityId;
    }

    public long getEntityId() {
        return entityId;
    }

}

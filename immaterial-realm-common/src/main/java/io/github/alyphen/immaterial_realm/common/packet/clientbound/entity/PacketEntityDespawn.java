package io.github.alyphen.immaterial_realm.common.packet.clientbound.entity;

import io.github.alyphen.immaterial_realm.common.packet.Packet;

import java.util.UUID;

public class PacketEntityDespawn extends Packet {

    private UUID entityUUID;

    public PacketEntityDespawn(UUID entityUUID) {
        this.entityUUID = entityUUID;
    }

    public UUID getEntityUUID() {
        return entityUUID;
    }

}

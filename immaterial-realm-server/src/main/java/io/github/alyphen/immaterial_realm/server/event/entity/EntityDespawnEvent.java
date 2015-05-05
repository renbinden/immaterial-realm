package io.github.alyphen.immaterial_realm.server.event.entity;

import io.github.alyphen.immaterial_realm.common.entity.Entity;

public class EntityDespawnEvent extends EntityEvent {

    public EntityDespawnEvent(Entity entity) {
        super(entity);
    }

}

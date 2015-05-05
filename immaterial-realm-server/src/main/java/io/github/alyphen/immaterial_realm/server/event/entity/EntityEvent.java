package io.github.alyphen.immaterial_realm.server.event.entity;

import io.github.alyphen.immaterial_realm.common.entity.Entity;
import io.github.alyphen.immaterial_realm.server.event.Event;

public class EntityEvent extends Event {

    private Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

}

package io.github.alyphen.immaterial_realm.server.event.entity;

import io.github.alyphen.immaterial_realm.common.entity.EntityCharacter;

public class CharacterDespawnEvent extends EntityDespawnEvent {

    private EntityCharacter entity;

    public CharacterDespawnEvent(EntityCharacter entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public EntityCharacter getEntity() {
        return entity;
    }

}

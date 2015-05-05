package io.github.alyphen.immaterial_realm.server.event.entity;

import io.github.alyphen.immaterial_realm.common.entity.EntityCharacter;
import io.github.alyphen.immaterial_realm.common.world.World;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;

public class CharacterSpawnEvent extends EntitySpawnEvent {

    private EntityCharacter entity;

    public CharacterSpawnEvent(EntityCharacter entity, World world, WorldArea area, int x, int y) {
        super(entity, world, area, x, y);
        this.entity = entity;
    }

    @Override
    public EntityCharacter getEntity() {
        return entity;
    }
}

package io.github.alyphen.immaterial_realm.common.entity;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.entity.PacketEntitySpawn;
import io.github.alyphen.immaterial_realm.common.world.World;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static java.util.logging.Level.SEVERE;

public class EntityFactory {

    private ImmaterialRealm immaterialRealm;

    public EntityFactory(ImmaterialRealm immaterialRealm) {
        this.immaterialRealm = immaterialRealm;
    }

    public <T extends Entity> T spawn(Class<T> entityClass, WorldArea area, int x, int y) {
        try {
            T entity = entityClass.getConstructor(UUID.class).newInstance(UUID.randomUUID());
            entity.setX(x);
            entity.setY(y);
            area.addEntity(entity);
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            immaterialRealm.getLogger().log(SEVERE, "Failed to spawn entity", exception);
        }
        return null;
    }

    public <T extends Entity> T spawn(UUID uuid, Class<T> entityClass, WorldArea area, int x, int y) {
        try {
            T entity = entityClass.getConstructor(UUID.class).newInstance(uuid);
            entity.setX(x);
            entity.setY(y);
            area.addEntity(entity);
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            immaterialRealm.getLogger().log(SEVERE, "Failed to spawn entity", exception);
        }
        return null;
    }

    public Entity spawn(PacketEntitySpawn packet, World world) {
        return spawn(packet.getEntityUUID(), packet.getEntityClass(), world.getArea(packet.getAreaName()), packet.getX(), packet.getY());
    }

}

package io.github.alyphen.immaterial_realm.common.entity;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.entity.PacketEntitySpawn;
import io.github.alyphen.immaterial_realm.common.world.World;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;

import java.lang.reflect.InvocationTargetException;

import static java.util.logging.Level.SEVERE;

public class EntityFactory {

    private static int id;

    private EntityFactory() {}

    public static <T extends Entity> T spawn(Class<T> entityClass, WorldArea area, int x, int y) {
        try {
            T entity = entityClass.getConstructor(long.class).newInstance(id++);
            entity.setX(x);
            entity.setY(y);
            area.addEntity(entity);
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to spawn entity", exception);
        }
        return null;
    }

    public static <T extends Entity> T spawn(long id, Class<T> entityClass, WorldArea area, int x, int y) {
        try {
            T entity = entityClass.getConstructor(long.class).newInstance(id);
            entity.setX(x);
            entity.setY(y);
            area.addEntity(entity);
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to spawn entity", exception);
        }
        return null;
    }

    public static Entity spawn(PacketEntitySpawn packet, World world) {
        return spawn(packet.getId(), packet.getEntityClass(), world.getArea(packet.getAreaName()), packet.getX(), packet.getY());
    }

}

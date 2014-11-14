package io.github.alyphen.amethyst.common.entity;

import io.github.alyphen.amethyst.common.packet.entity.PacketEntitySpawn;
import io.github.alyphen.amethyst.common.world.World;
import io.github.alyphen.amethyst.common.world.WorldArea;

import java.lang.reflect.InvocationTargetException;

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
            exception.printStackTrace();
        }
        return null;
    }

    public static Entity spawn(PacketEntitySpawn packet, World world) {
        return spawn(packet.getEntityClass(), world.getArea(packet.getAreaName()), packet.getX(), packet.getY());
    }

}

package io.github.lucariatias.amethyst.common.entity;

import io.github.lucariatias.amethyst.common.packet.entity.PacketEntitySpawn;
import io.github.lucariatias.amethyst.common.world.World;
import io.github.lucariatias.amethyst.common.world.WorldArea;

import java.lang.reflect.InvocationTargetException;

public class EntityFactory {

    private int id;

    public <T extends Entity> T spawn(Class<T> entityClass, WorldArea area, int x, int y) {
        try {
            T entity = entityClass.getConstructor(int.class).newInstance(id++);
            entity.setX(x);
            entity.setY(y);
            area.addEntity(entity);
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Entity spawn(PacketEntitySpawn packet, World world) {
        return spawn(packet.getEntityClass(), world.getArea(packet.getAreaName()), packet.getX(), packet.getY());
    }

}

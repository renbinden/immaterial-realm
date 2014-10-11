package io.github.lucariatias.amethyst.common.entity;

public class EntityFactory {

    public Entity spawn(Class<? extends Entity> entityClass) {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}

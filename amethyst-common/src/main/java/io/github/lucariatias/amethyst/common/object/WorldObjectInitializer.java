package io.github.lucariatias.amethyst.common.object;

public interface WorldObjectInitializer<T extends WorldObject> {

    public T initialize(long id);

}

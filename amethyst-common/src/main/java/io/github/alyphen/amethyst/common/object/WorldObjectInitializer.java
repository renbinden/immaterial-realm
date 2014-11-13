package io.github.alyphen.amethyst.common.object;

public interface WorldObjectInitializer<T extends WorldObject> {

    public T initialize(long id);

}

package io.github.lucariatias.amethyst.common.player;

public class Player {

    private long id;
    private String name;

    public Player(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

package io.github.alyphen.immaterial_realm.common.player;

import io.github.alyphen.immaterial_realm.common.database.TableRow;

import java.util.UUID;

public class Player extends TableRow {

    private String name;

    public Player(UUID uuid, String name) {
        super(uuid);
        this.name = name;
    }

    public Player(String name) {
        this(UUID.randomUUID(), name);
    }

    public String getName() {
        return name;
    }

}

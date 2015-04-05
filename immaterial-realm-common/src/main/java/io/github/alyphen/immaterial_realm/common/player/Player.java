package io.github.alyphen.immaterial_realm.common.player;

import io.github.alyphen.immaterial_realm.common.database.TableRow;

public class Player extends TableRow {

    private String name;

    public Player(long id, String name) {
        super(id);
        this.name = name;
    }

    public Player(String name) {
        this(0, name);
    }

    public String getName() {
        return name;
    }

}

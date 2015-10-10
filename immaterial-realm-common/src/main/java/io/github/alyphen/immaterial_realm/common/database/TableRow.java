package io.github.alyphen.immaterial_realm.common.database;

import java.util.UUID;

public class TableRow {

    private UUID uuid;

    public TableRow(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

}

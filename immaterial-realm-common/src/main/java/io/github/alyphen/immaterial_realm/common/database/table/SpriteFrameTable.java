package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.database.Table;
import io.github.alyphen.immaterial_realm.common.sprite.SpriteFrame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.util.logging.Level.SEVERE;

public class SpriteFrameTable extends Table<SpriteFrame> {

    public SpriteFrameTable(ImmaterialRealm immaterialRealm) throws SQLException {
        super(immaterialRealm, SpriteFrame.class);
    }

    @Override
    public void create() throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS sprite_frame (" +
                        "uuid VARCHAR(36) PRIMARY KEY," +
                        "sprite_uuid VARCHAR(36)," +
                        "image_uuid VARCHAR(36)," +
                        "FOREIGN KEY(sprite_uuid) REFERENCES sprite(uuid)," +
                        "FOREIGN KEY(image_uuid) REFERENCES image(uuid)" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to create sprite frame table", exception);
        }
    }

    @Override
    public void insert(SpriteFrame spriteFrame) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO sprite_frame (uuid, sprite_uuid, image_uuid) VALUES(?, ?, ?)"
        )) {
            statement.setString(1, spriteFrame.getUUID().toString());
            statement.setString(2, spriteFrame.getSpriteUUID().toString());
            statement.setString(3, spriteFrame.getFrameUUID().toString());
            if (statement.executeUpdate() == 0)
                throw new SQLException("Failed to insert sprite frame");
        }
    }

    @Override
    public void update(SpriteFrame spriteFrame) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE sprite_frame SET sprite_uuid = ?, image_uuid = ? WHERE uuid = ?"
        )) {
            statement.setString(1, spriteFrame.getSpriteUUID().toString());
            statement.setString(2, spriteFrame.getFrameUUID().toString());
            statement.setString(3, spriteFrame.getUUID().toString());
            statement.executeUpdate();
        }
    }

    @Override
    public SpriteFrame get(UUID uuid) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM sprite_frame WHERE uuid = ?"
        )) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SpriteFrame(UUID.fromString(resultSet.getString("uuid")), UUID.fromString(resultSet.getString("sprite_uuid")), UUID.fromString(resultSet.getString("image_uuid")));
            }
        }
        return null;
    }

}

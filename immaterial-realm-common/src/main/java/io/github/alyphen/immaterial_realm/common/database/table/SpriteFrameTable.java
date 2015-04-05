package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.database.Database;
import io.github.alyphen.immaterial_realm.common.database.Table;
import io.github.alyphen.immaterial_realm.common.sprite.SpriteFrame;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SpriteFrameTable extends Table<SpriteFrame> {

    public SpriteFrameTable(Database database) throws SQLException {
        super(database, SpriteFrame.class);
    }

    @Override
    public void create() throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS sprite_frame (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "sprite_id INTEGER," +
                        "image_id INTEGER," +
                        "FOREIGN KEY(sprite_id) REFERENCES sprite(id)," +
                        "FOREIGN KEY(image_id) REFERENCES image(id)" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public long insert(SpriteFrame spriteFrame) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO sprite_frame (sprite_id, image_id) VALUES(?, ?)",
                RETURN_GENERATED_KEYS
        )) {
            statement.setLong(1, spriteFrame.getSpriteId());
            statement.setLong(2, spriteFrame.getFrameId());
            if (statement.executeUpdate() == 0) throw new SQLException("Failed to insert sprite frame");
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
        }
        throw new SQLException("Failed to insert sprite frame");
    }

    @Override
    public long update(SpriteFrame spriteFrame) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE sprite_frame SET sprite_id = ?, image_id = ? WHERE id = ?"
        )) {
            statement.setLong(1, spriteFrame.getSpriteId());
            statement.setLong(2, spriteFrame.getFrameId());
            statement.setLong(3, spriteFrame.getId());
            statement.executeUpdate();
            return spriteFrame.getId();
        }
    }

    @Override
    public SpriteFrame get(long id) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM sprite_frame WHERE id = ?"
        )) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SpriteFrame(resultSet.getLong("id"), resultSet.getLong("sprite_id"), resultSet.getLong("image_id"));
            }
        }
        return null;
    }

}

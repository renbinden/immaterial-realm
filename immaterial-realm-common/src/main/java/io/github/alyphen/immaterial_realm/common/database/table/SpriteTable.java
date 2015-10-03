package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.database.Database;
import io.github.alyphen.immaterial_realm.common.database.Table;
import io.github.alyphen.immaterial_realm.common.sprite.IndexedImage;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;
import io.github.alyphen.immaterial_realm.common.sprite.SpriteFrame;
import io.github.alyphen.immaterial_realm.common.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.util.logging.Level.SEVERE;

public class SpriteTable extends Table<Sprite> {

    public SpriteTable(Database database) throws SQLException {
        super(database, Sprite.class);
    }

    @Override
    public void create() {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS sprite (" +
                        "id INTEGER PRIMARY KEY," +
                        "name TEXT," +
                        "frame_delay INTEGER" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to create sprite table", exception);
        }
    }

    @Override
    public long insert(Sprite sprite) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO sprite (name, frame_delay) VALUES(?, ?)",
                RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, sprite.getName());
            statement.setInt(2, sprite.getFrameDelay());
            if (statement.executeUpdate() == 0) throw new SQLException("Failed to insert sprite");
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                sprite.setId(id);
                for (BufferedImage image : sprite.getFrames()) {
                    IndexedImage indexedImage = new IndexedImage(image);
                    getDatabase().getTable(IndexedImage.class).insert(indexedImage);
                    getDatabase().getTable(SpriteFrame.class).insert(new SpriteFrame(id, indexedImage.getId()));
                }
                return id;
            }
        }
        throw new SQLException("Failed to insert sprite");
    }

    @Override
    public long update(Sprite sprite) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE sprite SET name = ?, frame_delay = ? WHERE id = ?"
        )) {
            statement.setString(1, sprite.getName());
            statement.setInt(2, sprite.getFrameDelay());
            statement.setLong(3, sprite.getId());
            statement.executeUpdate();
            return sprite.getId();
        }
    }

    @Override
    public Sprite get(long id) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement frameStatement = connection.prepareStatement(
                "SELECT image.id AS image_id, image.image AS frame, sprite.id AS sprite_id, sprite.name AS sprite_name, sprite.frame_delay AS sprite_frame_delay " +
                    "FROM sprite INNER JOIN sprite_frame INNER JOIN image " +
                    "ON sprite.id = sprite_frame.sprite_id " +
                    "AND sprite_frame.image_id = image.id " +
                    "WHERE sprite.id = ?"
        );
        PreparedStatement spriteStatement = connection.prepareStatement(
                "SELECT * FROM sprite WHERE id = ?"
        )) {
            frameStatement.setLong(1, id);
            ResultSet frameResultSet = frameStatement.executeQuery();
            List<IndexedImage> indexedFrames = new ArrayList<>();
            while (frameResultSet.next()) {
                try {
                    indexedFrames.add(new IndexedImage(frameResultSet.getLong("image_id"), ImageUtils.fromByteArray(frameResultSet.getBytes("frame"))));
                } catch (IOException exception) {
                    // Hopefully this won't break too much.
                    // If we get a lot of reports of errors here, try moving the catch block outside the loop or adding declaration of error to the method.
                    ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to retrieve sprite frames", exception);
                }
            }
            BufferedImage[] frames = new BufferedImage[indexedFrames.size()];
            for (int i = 0; i < indexedFrames.size(); i++) {
                frames[i] = indexedFrames.get(i).getImage();
            }
            spriteStatement.setLong(1, id);
            ResultSet spriteResultSet = spriteStatement.executeQuery();
            if (spriteResultSet.next()) {
                return new Sprite(spriteResultSet.getLong("id"), spriteResultSet.getString("name"), spriteResultSet.getInt("frame_delay"), frames);
            } else {
                return null;
            }
        }
    }
}

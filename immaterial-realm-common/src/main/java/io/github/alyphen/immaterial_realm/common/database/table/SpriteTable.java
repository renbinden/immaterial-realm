package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
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
import java.util.UUID;

import static java.util.logging.Level.SEVERE;

public class SpriteTable extends Table<Sprite> {

    public SpriteTable(ImmaterialRealm immaterialRealm) throws SQLException {
        super(immaterialRealm, Sprite.class);
    }

    @Override
    public void create() {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS sprite (" +
                        "uuid VARCHAR(36) PRIMARY KEY," +
                        "name TEXT," +
                        "frame_delay INTEGER" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to create sprite table", exception);
        }
    }

    @Override
    public void insert(Sprite sprite) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO sprite (uuid, name, frame_delay) VALUES(?, ?, ?)"
        )) {
            statement.setString(1, sprite.getUUID().toString());
            statement.setString(2, sprite.getName());
            statement.setInt(3, sprite.getFrameDelay());
            if (statement.executeUpdate() == 0)
                throw new SQLException("Failed to insert sprite");
            for (BufferedImage image : sprite.getFrames()) {
                IndexedImage indexedImage = new IndexedImage(image);
                getImmaterialRealm().getDatabase().getTable(IndexedImage.class).insert(indexedImage);
                getImmaterialRealm().getDatabase().getTable(SpriteFrame.class).insert(new SpriteFrame(sprite.getUUID(), indexedImage.getUUID()));
            }
        }
    }

    @Override
    public void update(Sprite sprite) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE sprite SET name = ?, frame_delay = ? WHERE uuid = ?"
        )) {
            statement.setString(1, sprite.getName());
            statement.setInt(2, sprite.getFrameDelay());
            statement.setString(3, sprite.getUUID().toString());
            statement.executeUpdate();
        }
    }

    @Override
    public Sprite get(UUID uuid) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement frameStatement = connection.prepareStatement(
                "SELECT image.uuid AS image_uuid, image.image AS frame, sprite.uuid AS sprite_uuid, sprite.name AS sprite_name, sprite.frame_delay AS sprite_frame_delay " +
                    "FROM sprite INNER JOIN sprite_frame INNER JOIN image " +
                    "ON sprite.uuid = sprite_frame.sprite_uuid " +
                    "AND sprite_frame.image_uuid = image.uuid " +
                    "WHERE sprite.uuid = ?"
        );
        PreparedStatement spriteStatement = connection.prepareStatement(
                "SELECT * FROM sprite WHERE uuid = ?"
        )) {
            frameStatement.setString(1, uuid.toString());
            ResultSet frameResultSet = frameStatement.executeQuery();
            List<IndexedImage> indexedFrames = new ArrayList<>();
            while (frameResultSet.next()) {
                try {
                    indexedFrames.add(new IndexedImage(UUID.fromString(frameResultSet.getString("image_uuid")), ImageUtils.fromByteArray(frameResultSet.getBytes("frame"))));
                } catch (IOException exception) {
                    // Hopefully this won't break too much.
                    // If we get a lot of reports of errors here, try moving the catch block outside the loop or adding declaration of error to the method.
                    getImmaterialRealm().getLogger().log(SEVERE, "Failed to retrieve sprite frames", exception);
                }
            }
            BufferedImage[] frames = new BufferedImage[indexedFrames.size()];
            for (int i = 0; i < indexedFrames.size(); i++) {
                frames[i] = indexedFrames.get(i).getImage();
            }
            spriteStatement.setString(1, uuid.toString());
            ResultSet spriteResultSet = spriteStatement.executeQuery();
            if (spriteResultSet.next()) {
                return new Sprite(getImmaterialRealm(), UUID.fromString(spriteResultSet.getString("uuid")), spriteResultSet.getString("name"), spriteResultSet.getInt("frame_delay"), frames);
            } else {
                return null;
            }
        }
    }
}

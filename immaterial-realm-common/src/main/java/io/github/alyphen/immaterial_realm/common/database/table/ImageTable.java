package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.database.Table;
import io.github.alyphen.immaterial_realm.common.sprite.IndexedImage;
import io.github.alyphen.immaterial_realm.common.util.ImageUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static java.util.logging.Level.SEVERE;

public class ImageTable extends Table<IndexedImage> {

    public ImageTable(ImmaterialRealm immaterialRealm) throws SQLException {
        super(immaterialRealm, IndexedImage.class);
    }

    @Override
    public void create() throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS image (" +
                        "uuid VARCHAR(36) PRIMARY KEY," +
                        "image BLOB" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to create image table", exception);
        }
    }

    @Override
    public void insert(IndexedImage image) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO image (uuid, image) VALUES(?, ?)"
        )) {
            statement.setString(1, image.getUUID().toString());
            statement.setBytes(2, ImageUtils.toByteArray(image.getImage()));
            if (statement.executeUpdate() == 0)
                throw new SQLException("Failed to insert image");
        } catch (IOException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to insert image to database", exception);
        }
    }

    @Override
    public void update(IndexedImage image) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
            "UPDATE image SET image = ? WHERE uuid = ?"
        )) {
            statement.setBytes(1, ImageUtils.toByteArray(image.getImage()));
            statement.setString(2, image.getUUID().toString());
            statement.executeUpdate();
        } catch (IOException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to update image in database", exception);
        }
        throw new SQLException("Failed to update image");
    }

    @Override
    public IndexedImage get(UUID uuid) throws SQLException {
        Connection connection = getImmaterialRealm().getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM image WHERE uuid = ?"
        )) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new IndexedImage(UUID.fromString(resultSet.getString("uuid")), ImageUtils.fromByteArray(resultSet.getBytes("image")));
            }
        } catch (IOException exception) {
            getImmaterialRealm().getLogger().log(SEVERE, "Failed to load image from database", exception);
        }
        return null;
    }

}

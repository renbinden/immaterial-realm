package io.github.alyphen.immaterial_realm.common.database.table;

import io.github.alyphen.immaterial_realm.common.database.Database;
import io.github.alyphen.immaterial_realm.common.database.Table;
import io.github.alyphen.immaterial_realm.common.sprite.IndexedImage;
import io.github.alyphen.immaterial_realm.common.util.ImageUtils;

import java.io.IOException;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class ImageTable extends Table<IndexedImage> {

    public ImageTable(Database database) throws SQLException {
        super(database, IndexedImage.class);
    }

    @Override
    public void create() throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS image (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "image BLOB" +
                ")"
        )) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public long insert(IndexedImage image) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO image (image) VALUES(?)",
                RETURN_GENERATED_KEYS
        )) {
            statement.setBytes(1, ImageUtils.toByteArray(image.getImage()));
            if (statement.executeUpdate() == 0) throw new SQLException("Failed to insert image");
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                image.setId(id);
                return id;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        throw new SQLException("Failed to insert image");
    }

    @Override
    public long update(IndexedImage image) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
            "UPDATE image SET image = ? WHERE id = ?"
        )) {
            statement.setBytes(1, ImageUtils.toByteArray(image.getImage()));
            statement.setLong(2, image.getId());
            statement.executeUpdate();
            return image.getId();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        throw new SQLException("Failed to update image");
    }

    @Override
    public IndexedImage get(long id) throws SQLException {
        Connection connection = getDatabase().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM image WHERE id = ?"
        )) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new IndexedImage(resultSet.getLong("id"), ImageUtils.fromByteArray(resultSet.getBytes("image")));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}

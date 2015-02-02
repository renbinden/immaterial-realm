package io.github.alyphen.immaterial_realm.common.world;

import io.github.alyphen.immaterial_realm.common.entity.Entity;
import io.github.alyphen.immaterial_realm.common.object.WorldObjectFactory;
import io.github.alyphen.immaterial_realm.common.tile.TileSheet;
import io.github.alyphen.immaterial_realm.common.object.WorldObject;
import io.github.alyphen.immaterial_realm.common.packet.world.PacketSendArea;
import io.github.alyphen.immaterial_realm.common.tile.Tile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.alyphen.immaterial_realm.common.util.FileUtils.loadMetadata;
import static io.github.alyphen.immaterial_realm.common.util.FileUtils.saveMetadata;
import static java.lang.System.arraycopy;

public class WorldArea {

    private World world;
    private String name;
    private List<WorldObject> objects;
    private List<Entity> entities;
    private int rows;
    private int cols;
    private Tile[][] tiles;

    public WorldArea(World world, String name, int rows, int cols) {
        this.world = world;
        this.name = name;
        this.objects = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.rows = rows;
        this.cols = cols;
        this.tiles = new Tile[rows][cols];
    }

    public World getWorld() {
        return world;
    }

    public String getName() {
        return name;
    }

    public List<WorldObject> getObjects() {
        return objects;
    }

    public void addObject(WorldObject object) {
        getObjects().add(object);
    }

    public void removeObject(WorldObject object) {
        getObjects().remove(object);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        getEntities().add(entity);
    }

    public void removeEntity(Entity entity) {
        getEntities().remove(entity);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return cols;
    }

    public void resize(int cols, int rows) {
        addColumns(rows - getRows());
        addRows(cols - getColumns());
    }

    public void addColumns(int n) {
        if (n == 0) {
            return;
        }
        int oldCols = getTiles().length;
        int newCols = oldCols + n;
        int cols = getColumns();
        Tile[][] copy = new Tile[newCols][];
        arraycopy(getTiles(), 0, copy, 0, n > 0 ? oldCols : newCols);
        for (int i = oldCols; i < newCols; i++) {
            copy[i] = new Tile[cols];
        }
        tiles = copy;
        rows += n;
    }

    public void addRows(int n) {
        if (n == 0) {
            return;
        }
        int oldRows = getColumns();
        int newRows = oldRows + n;
        int height = getTiles().length;
        Tile[][] copy = new Tile[height][newRows];
        for (int i = 0; i < height; i++) {
            copy[i] = new Tile[newRows];
            arraycopy(getTiles()[i], 0, copy[i], 0, n > 0 ? oldRows : newRows);
        }
        tiles = copy;
        cols += n;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Tile getTileAt(int row, int col) {
        return getTiles()[row][col];
    }

    public void setTileAt(int row, int col, Tile tile) {
        if (row >= getTiles().length || row < 0) return;
        if (col >= getTiles()[row].length || col < 0) return;
        getTiles()[row][col] = tile;
    }

    public void onTick() {
        getObjects().stream().forEach(WorldObject::onTick);
        getEntities().stream().forEach(Entity::onTick);
    }

    public static WorldArea load(World world, File directory) throws IOException, ClassNotFoundException {
        File metadataFile = new File(directory, "area.json");
        Map<String, Object> metadata = loadMetadata(metadataFile);
        WorldArea area = new WorldArea(world, (String) metadata.get("name"), (int) ((double) metadata.get("rows")), (int) ((double) metadata.get("cols")));
        List<List<Map<String, Object>>> tiles = (List<List<Map<String, Object>>>) metadata.get("tiles");
        for (int row = 0; row < area.getRows(); row++) {
            for (int col = 0; col < area.getColumns(); col++) {
                Map<String, Object> tileMeta = tiles.get(row).get(col);
                Tile tile = TileSheet.getTileSheet((String) tileMeta.get("sheet")).getTile((int) ((double) tileMeta.get("row")), (int) ((double) tileMeta.get("col")));
                area.setTileAt(row, col, tile);
            }
        }
        List<Map<String, Object>> objects = (List<Map<String, Object>>) metadata.get("objects");
        for (Map<String, Object> objectMeta : objects) {
            WorldObject object = WorldObjectFactory.createObject((String) objectMeta.get("type"));
            object.setX((int) objectMeta.get("x"));
            object.setY((int) objectMeta.get("y"));
            area.addObject(object);
        }
        return area;
    }

    public static WorldArea load(PacketSendArea packet) {
        WorldArea area = new WorldArea(World.getWorld(packet.getWorld()), packet.getArea(), packet.getRows(), packet.getColumns());
        for (int row = 0; row < packet.getRows(); row++) {
            for (int col = 0; col < packet.getColumns(); col++) {
                area.setTileAt(row, col, packet.getTileAt(row, col));
            }
        }
        return area;
    }

    public void save(File directory) throws IOException {
        File metadataFile = new File(directory, "area.json");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", getName());
        metadata.put("rows", getRows());
        metadata.put("cols", getColumns());
        List<List<Map<String, Object>>> tiles = new ArrayList<>();
        for (int row = 0; row < getRows(); row++) {
            tiles.add(new ArrayList<>());
            for (int col = 0; col < getColumns(); col++) {
                Tile tile = getTileAt(row, col);
                Map<String, Object> tileMeta = new HashMap<>();
                tileMeta.put("sheet", tile.getSheet().getName());
                tileMeta.put("row", tile.getRow());
                tileMeta.put("col", tile.getColumn());
                tiles.get(row).add(tileMeta);
            }
        }
        metadata.put("tiles", tiles);
        List<Map<String, Object>> objects = new ArrayList<>();
        for (WorldObject object : getObjects()) {
            Map<String, Object> objectMeta = new HashMap<>();
            objectMeta.put("type", object.getClass());
            objectMeta.put("x", object.getX());
            objectMeta.put("y", object.getY());
            objects.add(objectMeta);
        }
        metadata.put("objects", objects);
        saveMetadata(metadata, metadataFile);
    }

}

package io.github.alyphen.immaterial_realm.common.world;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.object.WorldObject;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.world.PacketSendArea;
import io.github.alyphen.immaterial_realm.common.tile.Tile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.alyphen.immaterial_realm.common.util.FileUtils.loadMetadata;
import static io.github.alyphen.immaterial_realm.common.util.FileUtils.saveMetadata;

public class World {

    private ImmaterialRealm immaterialRealm;

    private String name;
    private Map<String, WorldArea> areas;

    public World(ImmaterialRealm immaterialRealm, String name) {
        this.immaterialRealm = immaterialRealm;
        this.name = name;
        areas = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Collection<WorldArea> getAreas() {
        return areas.values();
    }

    public WorldArea getArea(String name) {
        return areas.get(name);
    }

    public void addArea(WorldArea area) {
        areas.put(area.getName(), area);
    }

    public void removeArea(WorldArea area) {
        if (area == null) return;
        areas.remove(area.getName());
    }

    public WorldArea loadArea(File directory) throws IOException {
        File metadataFile = new File(directory, "area.json");
        Map<String, Object> metadata = loadMetadata(metadataFile);
        WorldArea area = new WorldArea(this, (String) metadata.get("name"), (int) ((double) metadata.get("rows")), (int) ((double) metadata.get("cols")));
        List<List<Map<String, Object>>> tiles = (List<List<Map<String, Object>>>) metadata.get("tiles");
        for (int row = 0; row < area.getRows(); row++) {
            for (int col = 0; col < area.getColumns(); col++) {
                Map<String, Object> tileMeta = tiles.get(row).get(col);
                Tile tile = immaterialRealm.getTileManager().getTile((String) tileMeta.get("name"));
                area.setTileAt(row, col, tile);
            }
        }
        List<Map<String, Object>> objects = (List<Map<String, Object>>) metadata.get("objects");
        for (Map<String, Object> objectMeta : objects) {
            WorldObject object = immaterialRealm.getWorldObjectTypeManager().getObjectType((String) objectMeta.get("type")).createObject();
            if (object != null) {
                object.setX((int) ((double) objectMeta.get("x")));
                object.setY((int) ((double) objectMeta.get("y")));
                area.addObject(object);
            }
        }
        addArea(area);
        return area;
    }

    public WorldArea loadArea(PacketSendArea packet) {
        WorldArea area = new WorldArea(immaterialRealm.getWorldManager().getWorld(packet.getWorld()), packet.getArea(), packet.getRows(), packet.getColumns());
        for (int row = 0; row < packet.getRows(); row++) {
            for (int col = 0; col < packet.getColumns(); col++) {
                area.setTileAt(row, col, packet.getTileAt(immaterialRealm.getTileManager(), row, col));
            }
        }
        return area;
    }

    public void onTick() {
        getAreas().forEach(WorldArea::onTick);
    }

    public void save(File directory) throws IOException {
        File metadataFile = new File(directory, "world.json");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", getName());
        File areaDirectory = new File(directory, "areas");
        for (Map.Entry<String, WorldArea> entry : areas.entrySet()) {
            entry.getValue().save(new File(areaDirectory, entry.getKey()));
        }
        saveMetadata(metadata, metadataFile);
    }

}

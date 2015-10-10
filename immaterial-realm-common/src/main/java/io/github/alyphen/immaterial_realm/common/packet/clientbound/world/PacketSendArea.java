package io.github.alyphen.immaterial_realm.common.packet.clientbound.world;

import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.tile.Tile;
import io.github.alyphen.immaterial_realm.common.tile.TileManager;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketSendArea extends Packet {

    private String world;
    private String area;
    private int rows;
    private int cols;
    private List<List<Map<String, Object>>> tiles;

    public PacketSendArea(WorldArea area) {
        this.world = area.getWorld().getName();
        this.area = area.getName();
        this.rows = area.getRows();
        this.cols = area.getColumns();
        tiles = new ArrayList<>();
        for (int row = 0; row < area.getRows(); row++) {
            tiles.add(new ArrayList<>());
            for (int col = 0; col < area.getColumns(); col++) {
                Tile tile = area.getTileAt(row, col);
                if (tile != null) {
                    Map<String, Object> tileMeta = new HashMap<>();
                    tileMeta.put("name", tile.getName());
                    tiles.get(row).add(col, tileMeta);
                } else {
                    tiles.get(row).add(col, null);
                }
            }
        }
    }

    public String getWorld() {
        return world;
    }

    public String getArea() {
        return area;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return cols;
    }

    public Tile getTileAt(TileManager tileManager, int row, int col) {
        Map<String, Object> tileMeta = tiles.get(row).get(col);
        return tileManager.getTile((String) tileMeta.get("name"));
    }

}

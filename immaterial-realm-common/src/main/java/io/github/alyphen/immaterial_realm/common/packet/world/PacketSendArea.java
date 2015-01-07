package io.github.alyphen.immaterial_realm.common.packet.world;

import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.tile.Tile;
import io.github.alyphen.immaterial_realm.common.tile.TileSheet;
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
                Map<String, Object> tileMeta = new HashMap<>();
                tileMeta.put("sheet", area.getTileAt(row, col).getSheet().getName());
                tileMeta.put("row", area.getTileAt(row, col).getRow());
                tileMeta.put("col", area.getTileAt(row, col).getColumn());
                tiles.get(row).add(tileMeta);
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

    public Tile getTileAt(int row, int col) {
        Map<String, Object> tileMeta = tiles.get(row).get(col);
        return TileSheet.getTileSheet((String) tileMeta.get("sheet")).getTile((int) tileMeta.get("row"), (int) tileMeta.get("col"));
    }

}

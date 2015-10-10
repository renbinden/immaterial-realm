package io.github.alyphen.immaterial_realm.common.tile;

import io.github.alyphen.immaterial_realm.common.packet.clientbound.tile.PacketSendTile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;

public class TileManager {

    private Map<String, Tile> tiles;

    {
        tiles = new HashMap<>();
    }

    public void loadTiles() throws IOException {
        File tilesDirectory =new File("./tiles");
        if (tilesDirectory.isDirectory()) {
            File[] tileDirectories = tilesDirectory.listFiles(File::isDirectory);
            if (tileDirectories != null) {
                for (File tileDirectory : tileDirectories) {
                    loadTile(tileDirectory);
                }
            }
        }
    }

    public void loadTile(File directory) throws IOException {
        tiles.put(directory.getName(), new Tile(directory));
    }

    public void loadTile(PacketSendTile packet) throws IOException {
        tiles.put(packet.getTileName(),
                new Tile(
                        packet.getTileName(), packet.getTileWidth(), packet.getTileHeight(), packet.getTileFrames(),
                        packet.getTileFrameDuration()
                )
        );
    }

    public Tile getTile(String name) {
        return tiles.get(name);
    }

    public Collection<Tile> getTiles() {
        return tiles.values();
    }

    public void saveDefaultTiles() throws IOException {
        File tilesDirectory = new File("./tiles");
        String[] defaultTiles = new String[] {
                "grass1",
                "grass2",
                "grass3",
                "grass4",
                "water"
        };
        for (String tileName : defaultTiles) {
            File tileDirectory = new File(tilesDirectory, tileName);
            if (!tileDirectory.isDirectory()) {
                tileDirectory.delete();
            }
            if (!tileDirectory.exists()) {
                tileDirectory.mkdirs();
                copy(getClass().getResourceAsStream("/tiles/" + tileName + "/tile.png"), get(new File(tileDirectory, "tile.png").getPath()));
                copy(getClass().getResourceAsStream("/tiles/" + tileName + "/tile.json"), get(new File(tileDirectory, "tile.json").getPath()));
            }
        }
    }
    
}

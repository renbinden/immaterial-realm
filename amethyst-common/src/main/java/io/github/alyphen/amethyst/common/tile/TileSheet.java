package io.github.alyphen.amethyst.common.tile;

import io.github.alyphen.amethyst.common.packet.clientbound.tile.PacketSendTileSheet;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static io.github.alyphen.amethyst.common.util.FileUtils.*;

public class TileSheet {

    private static Map<String, TileSheet> tileSheets;

    static {
        tileSheets = new HashMap<>();
        try {
            loadTileSheets();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void loadTileSheets() throws IOException {
        File tileSheetsDirectory = new File("./tilesheets");
        File[] fileList = tileSheetsDirectory.listFiles();
        if (fileList != null) {
            for (File directory : fileList) {
                tileSheets.put(directory.getName(), TileSheet.load(directory));
            }
        }
    }

    public static TileSheet getTileSheet(String name) {
        return tileSheets.get(name);
    }

    public static Collection<TileSheet> getTileSheets() {
        return tileSheets.values();
    }

    private String name;
    private BufferedImage sheet;
    private int tileWidth;
    private int tileHeight;
    private Tile[][] tiles;

    private TileSheet(String name, BufferedImage sheet, int tileWidth, int tileHeight) {
        this.name = name;
        this.sheet = sheet;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        tiles = new Tile[sheet.getHeight() / tileHeight][sheet.getWidth() / tileWidth];
    }

    public String getName() {
        return name;
    }

    public Tile getTile(int row, int col) {
        if (tiles[row][col] == null) {
            tiles[row][col] = new Tile(this, row, col);
        }
        return tiles[row][col];
    }

    public List<Tile> getTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (int x = 0; x < getWidth() / getTileHeight(); x++) {
            for (int y = 0; y < getHeight() / getTileHeight(); y ++) {
                tiles.add(getTile(x, y));
            }
        }
        return tiles;
    }

    public BufferedImage getSheet() {
        return sheet;
    }

    public int getWidth() {
        return sheet.getWidth();
    }

    public int getHeight() {
        return sheet.getHeight();
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public static TileSheet load(File directory) throws IOException {
        File metadataFile = new File(directory, "tilesheet.json");
        Map<String, Object> metadata = loadMetadata(metadataFile);
        File imageFile = new File(directory, "tilesheet.png");
        TileSheet sheet = new TileSheet(directory.getName(), ImageIO.read(imageFile), (int) ((double) metadata.get("tile_width")), (int) ((double) metadata.get("tile_height")));
        tileSheets.put(sheet.getName(), sheet);
        return sheet;
    }

    public static TileSheet load(PacketSendTileSheet packet) {
        TileSheet sheet = new TileSheet(packet.getName(), packet.getImage(), packet.getTileWidth(), packet.getTileHeight());
        tileSheets.put(sheet.getName(), sheet);
        return sheet;
    }

    public void save(File directory) throws IOException {
        if ((directory.exists() && deleteDirectory(directory) && directory.mkdir()) || directory.mkdir()) {
            File metadataFile = new File(directory, "tilesheet.json");
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("tile_width", tileWidth);
            metadata.put("tile_height", tileHeight);
            saveMetadata(metadata, metadataFile);
            File imageFile = new File(directory, "tilesheet.png");
            ImageIO.write(sheet, "png", imageFile);
        }
    }

}

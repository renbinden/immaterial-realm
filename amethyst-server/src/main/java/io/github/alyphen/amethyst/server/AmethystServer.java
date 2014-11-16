package io.github.alyphen.amethyst.server;

import io.github.alyphen.amethyst.common.database.DatabaseManager;
import io.github.alyphen.amethyst.common.encrypt.EncryptionManager;
import io.github.alyphen.amethyst.common.object.WorldObject;
import io.github.alyphen.amethyst.common.object.WorldObjectFactory;
import io.github.alyphen.amethyst.common.object.WorldObjectInitializer;
import io.github.alyphen.amethyst.common.packet.entity.PacketEntityMove;
import io.github.alyphen.amethyst.common.sprite.Sprite;
import io.github.alyphen.amethyst.common.tile.TileSheet;
import io.github.alyphen.amethyst.common.util.FileUtils;
import io.github.alyphen.amethyst.common.world.World;
import io.github.alyphen.amethyst.server.character.CharacterManager;
import io.github.alyphen.amethyst.server.network.NetworkManager;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static io.github.alyphen.amethyst.common.util.FileUtils.read;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;

public class AmethystServer {

    private CharacterManager characterManager;
    private DatabaseManager databaseManager;
    private EncryptionManager encryptionManager;
    private NetworkManager networkManager;
    private PlayerManager playerManager;
    private ScriptEngineManager scriptEngineManager;
    private boolean running;
    private static final long DELAY = 50L;

    public static void main(String[] args) {
        new AmethystServer(39752);
    }

    public AmethystServer(int port) {
        scriptEngineManager = new ScriptEngineManager();
        databaseManager = new DatabaseManager("server");
        characterManager = new CharacterManager(this);
        encryptionManager = new EncryptionManager();
        networkManager = new NetworkManager(this, port);
        try {
            saveDefaults();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            TileSheet.loadTileSheets();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        File objectsDirectory = new File("./objects");
        for (File objectDirectory : objectsDirectory.listFiles(File::isDirectory)) {
            try {
                File propertiesFile = new File(objectDirectory, "object.json");
                Map<String, Object> properties = FileUtils.loadMetadata(propertiesFile);
                WorldObjectFactory.registerObjectInitializer((String) properties.get("name"), new WorldObjectInitializer() {

                    @Override
                    public String getObjectName() {
                        return (String) properties.get("name");
                    }

                    @Override
                    public Sprite getObjectSprite() {
                        try {
                            return Sprite.load(new File(objectDirectory, "sprite"));
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public Rectangle getObjectBounds() {
                        return new Rectangle((int) ((double) properties.get("bounds_offset_x")), (int) ((double) properties.get("bounds_offset_y")), (int) ((double) properties.get("bounds_width")), (int) ((double) properties.get("bounds_height")));
                    }

                    @Override
                    public WorldObject initialize(long id) {
                        return new WorldObject(id, getObjectName(), getObjectSprite(), getObjectBounds()) {

                            {
                                File jsFile = new File(objectDirectory, "object.js");
                                File rbFile = new File(objectDirectory, "object.rb");
                                File pyFile = new File(objectDirectory, "object.py");
                                if (jsFile.exists()) {
                                    try {
                                        ScriptEngine engine = getScriptEngineManager().getEngineByExtension("js");
                                        engine.eval(read(jsFile));
                                        ((Invocable) engine).invokeFunction("create");
                                    } catch (ScriptException | FileNotFoundException exception) {
                                        exception.printStackTrace();
                                    } catch (NoSuchMethodException ignored) {}
                                } else if (rbFile.exists()) {
                                    try {
                                        ScriptEngine engine = getScriptEngineManager().getEngineByExtension("rb");
                                        engine.eval(read(rbFile));
                                        ((Invocable) engine).invokeFunction("create");
                                    } catch (ScriptException | FileNotFoundException exception) {
                                        exception.printStackTrace();
                                    } catch (NoSuchMethodException ignored) {}
                                } else if (pyFile.exists()) {
                                    try {
                                        ScriptEngine engine = getScriptEngineManager().getEngineByExtension("py");
                                        engine.eval(read(pyFile));
                                        ((Invocable) engine).invokeFunction("create");
                                    } catch (ScriptException | FileNotFoundException exception) {
                                        exception.printStackTrace();
                                    } catch (NoSuchMethodException ignored) {}
                                }
                            }

                            @Override
                            public void onInteract() {
                                File jsFile = new File(objectDirectory, "object.js");
                                File rbFile = new File(objectDirectory, "object.rb");
                                File pyFile = new File(objectDirectory, "object.py");
                                if (jsFile.exists()) {
                                    try {
                                        ScriptEngine engine = getScriptEngineManager().getEngineByExtension("js");
                                        engine.eval(read(jsFile));
                                        ((Invocable) engine).invokeFunction("interact");
                                    } catch (ScriptException | FileNotFoundException exception) {
                                        exception.printStackTrace();
                                    } catch (NoSuchMethodException ignored) {}
                                } else if (rbFile.exists()) {
                                    try {
                                        ScriptEngine engine = getScriptEngineManager().getEngineByExtension("rb");
                                        engine.eval(read(rbFile));
                                        ((Invocable) engine).invokeFunction("interact");
                                    } catch (ScriptException | FileNotFoundException exception) {
                                        exception.printStackTrace();
                                    } catch (NoSuchMethodException ignored) {}
                                } else if (pyFile.exists()) {
                                    try {
                                        ScriptEngine engine = getScriptEngineManager().getEngineByExtension("py");
                                        engine.eval(read(pyFile));
                                        ((Invocable) engine).invokeFunction("interact");
                                    } catch (ScriptException | FileNotFoundException exception) {
                                        exception.printStackTrace();
                                    } catch (NoSuchMethodException ignored) {}
                                }
                            }

                            @Override
                            public void onTick() {
                                super.onTick();
                                File jsFile = new File(objectDirectory, "object.js");
                                File rbFile = new File(objectDirectory, "object.rb");
                                File pyFile = new File(objectDirectory, "object.py");
                                if (jsFile.exists()) {
                                    try {
                                        ScriptEngine engine = getScriptEngineManager().getEngineByExtension("js");
                                        engine.eval(read(jsFile));
                                        ((Invocable) engine).invokeFunction("tick");
                                    } catch (ScriptException | FileNotFoundException exception) {
                                        exception.printStackTrace();
                                    } catch (NoSuchMethodException ignored) {}
                                } else if (rbFile.exists()) {
                                    try {
                                        ScriptEngine engine = getScriptEngineManager().getEngineByExtension("rb");
                                        engine.eval(read(rbFile));
                                        ((Invocable) engine).invokeFunction("tick");
                                    } catch (ScriptException | FileNotFoundException exception) {
                                        exception.printStackTrace();
                                    } catch (NoSuchMethodException ignored) {}
                                } else if (pyFile.exists()) {
                                    try {
                                        ScriptEngine engine = getScriptEngineManager().getEngineByExtension("py");
                                        engine.eval(read(pyFile));
                                        ((Invocable) engine).invokeFunction("tick");
                                    } catch (ScriptException | FileNotFoundException exception) {
                                        exception.printStackTrace();
                                    } catch (NoSuchMethodException ignored) {}
                                }
                            }

                        };
                    }

                });
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
        File worldDirectory = new File("./worlds");
        for (File file : worldDirectory.listFiles(File::isDirectory)) {
            try {
                World.load(file);
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
        new Thread(networkManager::start).start();
        playerManager = new PlayerManager(this);
    }

    public CharacterManager getCharacterManager() {
        return characterManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public EncryptionManager getEncryptionManager() {
        return encryptionManager;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ScriptEngineManager getScriptEngineManager() {
        return scriptEngineManager;
    }

    private void saveDefaults() throws IOException {
        saveDefaultConfiguration();
        saveDefaultTileSheets();
        saveDefaultObjectTypes();
        try {
            saveDefaultWorlds();
        } catch (URISyntaxException exception) {
            exception.printStackTrace();
        }
    }

    private void saveDefaultConfiguration() throws IOException {
        File configFile = new File("./server.json");
        if (!configFile.exists()) {
            copy(getClass().getResourceAsStream("/server.json"), get(configFile.getPath()));
        }
    }

    private void saveDefaultTileSheets() throws IOException {
        File tileSheetDirectory = new File("./tilesheets");
        File defaultTileSheetDirectory = new File(tileSheetDirectory, "default");
        if (!tileSheetDirectory.isDirectory()) {
            tileSheetDirectory.delete();
        }
        if (!tileSheetDirectory.exists()) {
            defaultTileSheetDirectory.mkdirs();
            copy(getClass().getResourceAsStream("/tilesheets/default/tilesheet.png"), get(new File(defaultTileSheetDirectory, "tilesheet.png").getPath()));
            copy(getClass().getResourceAsStream("/tilesheets/default/tilesheet.json"), get(new File(defaultTileSheetDirectory, "tilesheet.json").getPath()));
        }
    }

    private void saveDefaultObjectTypes() throws IOException {
        File objectsDirectory = new File("./objects");
        if (!objectsDirectory.isDirectory()) {
            objectsDirectory.delete();
        }
        if (!objectsDirectory.exists()) {
            objectsDirectory.mkdirs();
        }
    }

    private void saveDefaultWorlds() throws IOException, URISyntaxException {
        File worldDirectory = new File("./worlds");
        File defaultWorldDirectory = new File(worldDirectory, "default");
        if (!worldDirectory.isDirectory()) {
            worldDirectory.delete();
        }
        if (!worldDirectory.exists()) {
            defaultWorldDirectory.mkdirs();
            copy(getClass().getResourceAsStream("/worlds/default/world.json"), get(new File(defaultWorldDirectory, "world.json").getPath()));
            File defaultWorldAreasDirectory = new File(defaultWorldDirectory, "areas");
            File defaultWorldDefaultAreaDirectory = new File(defaultWorldAreasDirectory, "default");
            defaultWorldDefaultAreaDirectory.mkdirs();
            copy(getClass().getResourceAsStream("/worlds/default/areas/default/area.json"), get(new File(defaultWorldDefaultAreaDirectory, "area.json").getPath()));
        }
    }

    public void run() {
        setRunning(true);
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (isRunning()) {
            doTick();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            beforeTime = System.currentTimeMillis();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void doTick() {
        World.getWorlds().stream().forEach(world -> {
            world.onTick();
            world.getAreas().stream().forEach(area -> area.getEntities().stream().filter(entity -> entity.getHorizontalSpeed() != 0 || entity.getVerticalSpeed() != 0).forEach(entity -> getNetworkManager().broadcastPacket(new PacketEntityMove(entity.getId(), entity.getDirectionFacing(), area.getName(), entity.getX(), entity.getY()))));
        });
    }

}

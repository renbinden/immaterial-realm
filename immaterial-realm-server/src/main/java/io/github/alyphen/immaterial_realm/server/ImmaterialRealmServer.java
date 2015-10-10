package io.github.alyphen.immaterial_realm.server;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannelManager;
import io.github.alyphen.immaterial_realm.common.encrypt.EncryptionManager;
import io.github.alyphen.immaterial_realm.common.entity.Entity;
import io.github.alyphen.immaterial_realm.common.entity.EntityFactory;
import io.github.alyphen.immaterial_realm.common.log.FileWriterHandler;
import io.github.alyphen.immaterial_realm.common.object.WorldObjectTypeManager;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.entity.PacketEntityMove;
import io.github.alyphen.immaterial_realm.common.sprite.SpriteManager;
import io.github.alyphen.immaterial_realm.common.tile.TileManager;
import io.github.alyphen.immaterial_realm.common.world.WorldManager;
import io.github.alyphen.immaterial_realm.server.admin.tpsmonitor.TPSMonitorFrame;
import io.github.alyphen.immaterial_realm.server.character.CharacterComponentManager;
import io.github.alyphen.immaterial_realm.server.character.CharacterManager;
import io.github.alyphen.immaterial_realm.server.database.DatabaseManager;
import io.github.alyphen.immaterial_realm.server.event.EventManager;
import io.github.alyphen.immaterial_realm.server.event.entity.EntityMoveEvent;
import io.github.alyphen.immaterial_realm.server.hud.HUDManager;
import io.github.alyphen.immaterial_realm.server.network.NetworkManager;
import io.github.alyphen.immaterial_realm.server.plugin.PluginManager;

import javax.script.ScriptEngineManager;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Logger;

import static io.github.alyphen.immaterial_realm.common.util.FileUtils.loadMetadata;
import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.util.logging.Level.SEVERE;

public class ImmaterialRealmServer {

    private CharacterComponentManager characterComponentManager;
    private CharacterManager characterManager;
    private ChatChannelManager chatChannelManager;
    private DatabaseManager databaseManager;
    private EncryptionManager encryptionManager;
    private EntityFactory entityFactory;
    private EventManager eventManager;
    private HUDManager hudManager;
    private NetworkManager networkManager;
    private PluginManager pluginManager;
    private ScriptEngineManager scriptEngineManager;
    private SpriteManager spriteManager;
    private TileManager tileManager;
    private WorldObjectTypeManager worldObjectTypeManager;
    private WorldManager worldManager;
    private Map<String, Object> configuration;
    private Logger logger;
    private boolean running;
    private int tps;
    private Deque<Integer> previousTPSValues;
    private TPSMonitorFrame tpsMonitorFrame;
    private ImmaterialRealm immaterialRealm;

    public static void main(String[] args) {
        new ImmaterialRealmServer(39752);
    }

    public ImmaterialRealmServer(int port) {
        immaterialRealm = new ImmaterialRealm();
        logger = Logger.getLogger(getClass().getName());
        logger.addHandler(new FileWriterHandler(getImmaterialRealm()));
        immaterialRealm.setLogger(logger);
        scriptEngineManager = new ScriptEngineManager();
        immaterialRealm.setScriptEngineManager(scriptEngineManager);
        try {
            databaseManager = new DatabaseManager(getImmaterialRealm());
        } catch (SQLException exception) {
            logger.log(SEVERE, "Failed to connect to database", exception);
        }
        tileManager = new TileManager();
        try {
            tileManager.loadTiles();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to load tiles", exception);
        }
        immaterialRealm.setTileManager(tileManager);
        spriteManager = new SpriteManager(immaterialRealm);
        try {
            spriteManager.loadSprites();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to load sprites", exception);
        }
        immaterialRealm.setSpriteManager(spriteManager);
        characterComponentManager = new CharacterComponentManager(this);
        characterManager = new CharacterManager(this);
        entityFactory = new EntityFactory(immaterialRealm);
        immaterialRealm.setEntityFactory(entityFactory);
        chatChannelManager = new ChatChannelManager(immaterialRealm);
        chatChannelManager.saveDefaultChatChannels();
        try {
            chatChannelManager.loadChatChannels();
        } catch (FileNotFoundException exception) {
            getLogger().log(SEVERE, "Failed to load chat channels", exception);
        }
        immaterialRealm.setChatChannelManager(chatChannelManager);
        worldObjectTypeManager = new WorldObjectTypeManager(immaterialRealm);
        try {
            worldObjectTypeManager.saveDefaultObjectTypes();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to save default object types", exception);
        }
        try {
            worldObjectTypeManager.loadObjectTypes();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to load object types", exception);
        }
        immaterialRealm.setWorldObjectTypeManager(worldObjectTypeManager);
        worldManager = new WorldManager(immaterialRealm);
        try {
            worldManager.saveDefaultWorlds();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to save default worlds", exception);
        }
        try {
            worldManager.loadWorlds();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to load worlds", exception);
        }
        immaterialRealm.setWorldManager(worldManager);
        encryptionManager = new EncryptionManager();
        networkManager = new NetworkManager(this, port);
        hudManager = new HUDManager(this);
        eventManager = new EventManager(this);
        pluginManager = new PluginManager(this);
        try {
            saveDefaults();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to save defaults", exception);
        }
        loadConfiguration();
        try {
            immaterialRealm.getTileManager().loadTiles();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to load tiles", exception);
        }
        try {
            immaterialRealm.getWorldObjectTypeManager().loadObjectTypes();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to load world object types", exception);
        }
        new Thread(networkManager::start).start();
        setupAdminTools();
    }

    private void setupAdminTools() {
        previousTPSValues = new ConcurrentLinkedDeque<>();
        if (!GraphicsEnvironment.isHeadless()) {
            setupTPSMonitor();
        }
    }

    private void setupTPSMonitor() {
        tpsMonitorFrame = new TPSMonitorFrame(this);
        EventQueue.invokeLater(() -> tpsMonitorFrame.setVisible(true));
    }

    public CharacterComponentManager getCharacterComponentManager() {
        return characterComponentManager;
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

    public EventManager getEventManager() {
        return eventManager;
    }

    public HUDManager getHUDManager() {
        return hudManager;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public ScriptEngineManager getScriptEngineManager() {
        return scriptEngineManager;
    }

    private void saveDefaults() throws IOException {
        saveDefaultConfiguration();
        getImmaterialRealm().getTileManager().saveDefaultTiles();
        getImmaterialRealm().getWorldObjectTypeManager().saveDefaultObjectTypes();
        getImmaterialRealm().getWorldManager().saveDefaultWorlds();
        getImmaterialRealm().getChatChannelManager().saveDefaultChatChannels();
    }

    private void saveDefaultConfiguration() throws IOException {
        File configDir = new File("./config");
        if (!configDir.exists()) configDir.mkdirs();
        File configFile = new File(configDir, "server.json");
        if (!configFile.exists()) {
            copy(getClass().getResourceAsStream("/server.json"), get(configFile.getPath()));
        }
    }

    private void loadConfiguration() {
        try {
            saveDefaultConfiguration();
        } catch (IOException exception) {
            getLogger().log(SEVERE, "Failed to save default configuration", exception);
        }
        File configDir = new File("./config");
        File configFile = new File(configDir, "server.json");
        if (configFile.exists()) {
            try {
                configuration = loadMetadata(configFile);
            } catch (IOException exception) {
                getLogger().log(SEVERE, "Failed to load configuration", exception);
                configuration = new HashMap<>();
            }
        } else {
            configuration = new HashMap<>();
        }
    }

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public void run() {
        setRunning(true);
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (isRunning()) {
            doTick();
            timeDiff = System.currentTimeMillis() - beforeTime;
            long tickDelay = 25L;
            sleep = tickDelay - timeDiff;
            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException exception) {
                getLogger().log(SEVERE, "Thread interrupted", exception);
            }
            tps = (int) (1000 / (System.currentTimeMillis() - beforeTime));
            if (previousTPSValues.size() > 640) previousTPSValues.removeFirst();
            previousTPSValues.addLast(tps);
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
        getImmaterialRealm().getWorldManager().getWorlds().stream().forEach(world -> {
            world.getAreas().stream().forEach(area -> {
                Set<Entity> entitiesToRemove = new HashSet<>();
                area.getEntities().stream().forEach(entity -> {
                    EntityMoveEvent event = new EntityMoveEvent(entity, area, entity.getX() + entity.getHorizontalSpeed(), entity.getY() + entity.getVerticalSpeed());
                    getEventManager().onEvent(event);
                    if (event.isCancelled()) {
                        entity.setMovementCancelled(true);
                    } else {
                        if (area != event.getNewArea()) {
                            entitiesToRemove.add(entity);
                            event.getNewArea().addEntity(entity);
                            entity.setForceUpdate(true);
                        }
                        if (entity.getX() + entity.getHorizontalSpeed() != event.getNewX()) {
                            entity.setX(event.getNewX() - entity.getHorizontalSpeed());
                            entity.setForceUpdate(true);
                        }
                        if (entity.getY() + entity.getVerticalSpeed() != event.getNewY()) {
                            entity.setY(event.getNewY() - entity.getVerticalSpeed());
                            entity.setForceUpdate(true);
                        }
                    }
                });
                entitiesToRemove.forEach(area::removeEntity);
            });
            world.onTick();
            world.getAreas().stream().forEach(area -> area.getEntities().stream().filter(entity -> entity.isSpeedChanged() || entity.isMovementCancelled() || entity.isForceUpdate()).forEach(entity -> getNetworkManager().broadcastPacket(new PacketEntityMove(entity.getUUID(), entity.getDirectionFacing(), area.getName(), entity.getX(), entity.getY(), entity.getHorizontalSpeed(), entity.getVerticalSpeed()))));
        });
        if (tpsMonitorFrame != null) {
            tpsMonitorFrame.repaint();
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public int getTPS() {
        return tps;
    }

    public Deque<Integer> getPreviousTPSValues() {
        return previousTPSValues;
    }

    public ImmaterialRealm getImmaterialRealm() {
        return immaterialRealm;
    }
}

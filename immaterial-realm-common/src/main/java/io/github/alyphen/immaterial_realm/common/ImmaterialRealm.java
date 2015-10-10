package io.github.alyphen.immaterial_realm.common;

import io.github.alyphen.immaterial_realm.common.chat.ChatChannelManager;
import io.github.alyphen.immaterial_realm.common.database.Database;
import io.github.alyphen.immaterial_realm.common.encrypt.EncryptionManager;
import io.github.alyphen.immaterial_realm.common.entity.EntityFactory;
import io.github.alyphen.immaterial_realm.common.object.WorldObjectTypeManager;
import io.github.alyphen.immaterial_realm.common.sprite.SpriteManager;
import io.github.alyphen.immaterial_realm.common.tile.TileManager;
import io.github.alyphen.immaterial_realm.common.world.WorldManager;

import javax.script.ScriptEngineManager;
import java.util.logging.Logger;

public class ImmaterialRealm {

    private ChatChannelManager chatChannelManager;
    private Database database;
    private EncryptionManager encryptionManager;
    private EntityFactory entityFactory;
    private ScriptEngineManager scriptEngineManager;
    private SpriteManager spriteManager;
    private TileManager tileManager;
    private WorldObjectTypeManager worldObjectTypeManager;
    private WorldManager worldManager;

    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    public ChatChannelManager getChatChannelManager() {
        return chatChannelManager;
    }

    public void setChatChannelManager(ChatChannelManager chatChannelManager) {
        this.chatChannelManager = chatChannelManager;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public EncryptionManager getEncryptionManager() {
        return encryptionManager;
    }

    public void setEncryptionManager(EncryptionManager encryptionManager) {
        this.encryptionManager = encryptionManager;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void setEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public void setScriptEngineManager(ScriptEngineManager scriptEngineManager) {
        this.scriptEngineManager = scriptEngineManager;
    }

    public ScriptEngineManager getScriptEngineManager() {
        return scriptEngineManager;
    }

    public SpriteManager getSpriteManager() {
        return spriteManager;
    }

    public void setSpriteManager(SpriteManager spriteManager) {
        this.spriteManager = spriteManager;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public void setTileManager(TileManager tileManager) {
        this.tileManager = tileManager;
    }

    public WorldObjectTypeManager getWorldObjectTypeManager() {
        return worldObjectTypeManager;
    }

    public void setWorldObjectTypeManager(WorldObjectTypeManager worldObjectTypeManager) {
        this.worldObjectTypeManager = worldObjectTypeManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public void setWorldManager(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

}

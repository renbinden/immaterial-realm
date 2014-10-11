package io.github.lucariatias.amethyst.server;

import io.github.lucariatias.amethyst.common.database.DatabaseManager;
import io.github.lucariatias.amethyst.common.encrypt.EncryptionManager;
import io.github.lucariatias.amethyst.server.network.NetworkManager;

public class AmethystServer {

    private DatabaseManager databaseManager;
    private EncryptionManager encryptionManager;
    private NetworkManager networkManager;
    private PlayerManager playerManager;

    public static void main(String[] args) {
        new AmethystServer(39752);
    }

    public AmethystServer(int port) {
        databaseManager = new DatabaseManager("server");
        encryptionManager = new EncryptionManager();
        networkManager = new NetworkManager(this, port);
        new Thread(networkManager::start).start();
        playerManager = new PlayerManager(this);
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
}

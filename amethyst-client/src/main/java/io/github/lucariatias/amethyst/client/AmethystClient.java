package io.github.lucariatias.amethyst.client;

import io.github.lucariatias.amethyst.client.character.CharacterManager;
import io.github.lucariatias.amethyst.client.network.NetworkManager;
import io.github.lucariatias.amethyst.client.panel.ConnectionPanel;
import io.github.lucariatias.amethyst.client.panel.WorldPanel;
import io.github.lucariatias.amethyst.client.panel.LoginPanel;
import io.github.lucariatias.amethyst.common.database.DatabaseManager;
import io.github.lucariatias.amethyst.common.encrypt.EncryptionManager;
import io.github.lucariatias.amethyst.common.entity.EntityFactory;

import javax.swing.*;
import java.awt.*;

public class AmethystClient extends JPanel {

    private static final long DELAY = 25L;

    private DatabaseManager databaseManager;
    private EncryptionManager encryptionManager;
    private NetworkManager networkManager;
    private PlayerManager playerManager;

    private EntityFactory entityFactory;

    private boolean running;
    private String playerName;
    private String passwordHash;

    private ConnectionPanel connectionPanel;
    private LoginPanel loginPanel;
    private WorldPanel worldPanel;

    private boolean newAccount;
    private CharacterManager characterManager;

    public AmethystClient() {
        databaseManager = new DatabaseManager("client");
        encryptionManager = new EncryptionManager();
        networkManager = new NetworkManager(this);
        playerManager = new PlayerManager(this);

        setLayout(new CardLayout());
        connectionPanel = new ConnectionPanel(this);
        add(connectionPanel, "connect");
        loginPanel = new LoginPanel(this);
        add(loginPanel, "login");
        worldPanel = new WorldPanel(this);
        add(worldPanel, "world");
    }

    public void run() {
        setRunning(true);
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (isRunning()) {
            doTick();
            repaint();
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

    private void doTick() {
        if (worldPanel.isActive()) worldPanel.onTick();
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

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }

    public void showPanel(String panel) {
        CardLayout layout = (CardLayout) getLayout();
        layout.show(this, panel);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isNewAccount() {
        return newAccount;
    }

    public void setNewAccount(boolean newAccount) {
        this.newAccount = newAccount;
    }

    public ConnectionPanel getConnectionPanel() {
        return connectionPanel;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public WorldPanel getWorldPanel() {
        return worldPanel;
    }

    public CharacterManager getCharacterManager() {
        return characterManager;
    }
}

package io.github.lucariatias.amethyst.client;

import io.github.lucariatias.amethyst.client.network.NetworkManager;
import io.github.lucariatias.amethyst.client.panel.ConnectionPanel;
import io.github.lucariatias.amethyst.client.panel.WorldPanel;
import io.github.lucariatias.amethyst.client.panel.LoginPanel;
import io.github.lucariatias.amethyst.common.database.DatabaseManager;
import io.github.lucariatias.amethyst.common.encrypt.EncryptionManager;

import javax.swing.*;
import java.awt.*;

public class AmethystClient extends JPanel {

    private static final long DELAY = 25L;

    private DatabaseManager databaseManager;
    private EncryptionManager encryptionManager;
    private NetworkManager networkManager;
    private PlayerManager playerManager;

    private boolean running;
    private String playerName;
    private String passwordHash;
    private WorldPanel worldPanel;

    private boolean newAccount;

    public AmethystClient() {
        databaseManager = new DatabaseManager("client");
        encryptionManager = new EncryptionManager();
        networkManager = new NetworkManager(this);
        playerManager = new PlayerManager(this);

        setLayout(new CardLayout());
        add(new ConnectionPanel(this), "connect");
        add(new LoginPanel(this), "login");
        worldPanel = new WorldPanel();
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

}

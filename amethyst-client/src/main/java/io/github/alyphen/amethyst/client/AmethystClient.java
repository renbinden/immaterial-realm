package io.github.alyphen.amethyst.client;

import io.github.alyphen.amethyst.client.character.CharacterManager;
import io.github.alyphen.amethyst.client.chat.ChatManager;
import io.github.alyphen.amethyst.client.network.NetworkManager;
import io.github.alyphen.amethyst.client.panel.ConnectionPanel;
import io.github.alyphen.amethyst.client.panel.LoginPanel;
import io.github.alyphen.amethyst.client.panel.WorldPanel;
import io.github.alyphen.amethyst.common.control.Control;
import io.github.alyphen.amethyst.common.database.DatabaseManager;
import io.github.alyphen.amethyst.common.encrypt.EncryptionManager;
import io.github.alyphen.amethyst.common.packet.serverbound.control.PacketControlPressed;
import io.github.alyphen.amethyst.common.packet.serverbound.control.PacketControlReleased;

import javax.swing.*;
import java.awt.*;

public class AmethystClient extends JPanel {

    private static final long DELAY = 25L;

    private AmethystClientFrame frame;

    private ChatManager chatManager;
    private DatabaseManager databaseManager;
    private EncryptionManager encryptionManager;
    private InputManager inputManager;
    private LoginManager loginManager;
    private NetworkManager networkManager;
    private PlayerManager playerManager;

    private boolean running;
    private String playerName;
    private String passwordHash;

    private ConnectionPanel connectionPanel;
    private LoginPanel loginPanel;
    private WorldPanel worldPanel;

    private boolean newAccount;
    private CharacterManager characterManager;

    public AmethystClient(AmethystClientFrame frame) {
        this.frame = frame;
        chatManager = new ChatManager(this);
        databaseManager = new DatabaseManager("client");
        encryptionManager = new EncryptionManager();
        networkManager = new NetworkManager(this);
        characterManager = new CharacterManager(this);
        loginManager = new LoginManager(this);
        playerManager = new PlayerManager(this);
        inputManager = new InputManager(this);
        frame.addKeyListener(inputManager);

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
            EventQueue.invokeLater(this::repaint);
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

    public AmethystClientFrame getFrame() {
        return frame;
    }

    public CharacterManager getCharacterManager() {
        return characterManager;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public EncryptionManager getEncryptionManager() {
        return encryptionManager;
    }

    public LoginManager getLoginManager() {
        return loginManager;
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

    public ConnectionPanel getConnectionPanel() {
        return connectionPanel;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public WorldPanel getWorldPanel() {
        return worldPanel;
    }

    public void onControlPressed(Control control) {
        getNetworkManager().sendPacket(new PacketControlPressed(control));
    }

    public void onControlReleased(Control control) {
        getNetworkManager().sendPacket(new PacketControlReleased(control));
    }

}

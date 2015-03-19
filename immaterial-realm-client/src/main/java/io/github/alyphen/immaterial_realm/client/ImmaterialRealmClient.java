package io.github.alyphen.immaterial_realm.client;

import io.github.alyphen.immaterial_realm.client.character.CharacterManager;
import io.github.alyphen.immaterial_realm.client.chat.ChatManager;
import io.github.alyphen.immaterial_realm.client.network.NetworkManager;
import io.github.alyphen.immaterial_realm.client.panel.CharacterCreationPanel;
import io.github.alyphen.immaterial_realm.client.panel.ConnectionPanel;
import io.github.alyphen.immaterial_realm.client.panel.LoginPanel;
import io.github.alyphen.immaterial_realm.client.panel.WorldPanel;
import io.github.alyphen.immaterial_realm.common.control.Control;
import io.github.alyphen.immaterial_realm.common.database.DatabaseManager;
import io.github.alyphen.immaterial_realm.common.encrypt.EncryptionManager;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.control.PacketControlPressed;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.control.PacketControlReleased;

import javax.swing.*;
import java.awt.*;

public class ImmaterialRealmClient extends JPanel {

    private static final long DELAY = 25L;

    private ImmaterialRealmClientFrame frame;

    private CharacterManager characterManager;
    private ChatManager chatManager;
    private DatabaseManager databaseManager;
    private EncryptionManager encryptionManager;
    private InputManager inputManager;
    private LoginManager loginManager;
    private NetworkManager networkManager;
    private PlayerManager playerManager;

    private boolean running;
    private String playerName;

    private ConnectionPanel connectionPanel;
    private LoginPanel loginPanel;
    private WorldPanel worldPanel;
    private CharacterCreationPanel characterCreationPanel;

    public ImmaterialRealmClient(ImmaterialRealmClientFrame frame) {
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
        characterCreationPanel = new CharacterCreationPanel(this);
        add(characterCreationPanel, "character creation");
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
        if (worldPanel.isVisible()) worldPanel.onTick();
        if (characterCreationPanel.isVisible()) characterCreationPanel.onTick();
    }

    public ImmaterialRealmClientFrame getFrame() {
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

    public ConnectionPanel getConnectionPanel() {
        return connectionPanel;
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public WorldPanel getWorldPanel() {
        return worldPanel;
    }

    public CharacterCreationPanel getCharacterCreationPanel() {
        return characterCreationPanel;
    }

    public void onControlPressed(Control control) {
        getNetworkManager().sendPacket(new PacketControlPressed(control));
    }

    public void onControlReleased(Control control) {
        getNetworkManager().sendPacket(new PacketControlReleased(control));
    }

}

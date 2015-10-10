package io.github.alyphen.immaterial_realm.client;

import com.alee.laf.WebLookAndFeel;
import io.github.alyphen.immaterial_realm.client.character.CharacterManager;
import io.github.alyphen.immaterial_realm.client.chat.ChatManager;
import io.github.alyphen.immaterial_realm.client.network.NetworkManager;
import io.github.alyphen.immaterial_realm.client.panel.CharacterCreationPanel;
import io.github.alyphen.immaterial_realm.client.panel.ConnectionPanel;
import io.github.alyphen.immaterial_realm.client.panel.LoginPanel;
import io.github.alyphen.immaterial_realm.client.panel.WorldPanel;
import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannelManager;
import io.github.alyphen.immaterial_realm.common.control.Control;
import io.github.alyphen.immaterial_realm.common.encrypt.EncryptionManager;
import io.github.alyphen.immaterial_realm.common.entity.EntityFactory;
import io.github.alyphen.immaterial_realm.common.log.FileWriterHandler;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.control.PacketControlPressed;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.control.PacketControlReleased;
import io.github.alyphen.immaterial_realm.common.sprite.SpriteManager;
import io.github.alyphen.immaterial_realm.common.tile.TileManager;
import io.github.alyphen.immaterial_realm.common.world.WorldManager;

import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.logging.Logger;

import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

public class ImmaterialRealmClient extends JPanel {

    private final long tickDelay = 25L;

    private JFrame frame;

    private ImmaterialRealm immaterialRealm;

    private Logger logger;

    private CharacterManager characterManager;
    private ChatManager chatManager;
    private ChatChannelManager chatChannelManager;
    private DatabaseManager databaseManager;
    private EncryptionManager encryptionManager;
    private EntityFactory entityFactory;
    private InputManager inputManager;
    private LoginManager loginManager;
    private NetworkManager networkManager;
    private PlayerManager playerManager;
    private ScriptEngineManager scriptEngineManager;
    private SpriteManager spriteManager;
    private TileManager tileManager;
    private WorldManager worldManager;

    private boolean running;
    private int fps;
    private String playerName;

    private ConnectionPanel connectionPanel;
    private LoginPanel loginPanel;
    private WorldPanel worldPanel;
    private CharacterCreationPanel characterCreationPanel;

    public static void main(String[] args) {
        new ImmaterialRealmClient();
    }

    public ImmaterialRealmClient() {
        immaterialRealm = new ImmaterialRealm();
        logger = Logger.getLogger(getClass().getName());
        logger.addHandler(new FileWriterHandler(immaterialRealm));
        immaterialRealm.setLogger(logger);
        try {
            UIManager.setLookAndFeel(new WebLookAndFeel());
        } catch (UnsupportedLookAndFeelException exception) {
            getLogger().log(WARNING, "Failed to set look and feel", exception);
        }
        frame = new JFrame();
        scriptEngineManager = new ScriptEngineManager();
        immaterialRealm.setScriptEngineManager(scriptEngineManager);
        spriteManager = new SpriteManager(immaterialRealm);
        immaterialRealm.setSpriteManager(spriteManager);
        entityFactory = new EntityFactory(immaterialRealm);
        immaterialRealm.setEntityFactory(entityFactory);
        chatChannelManager = new ChatChannelManager(immaterialRealm);
        immaterialRealm.setChatChannelManager(chatChannelManager);
        chatManager = new ChatManager(this);
        try {
            databaseManager = new DatabaseManager(immaterialRealm);
        } catch (SQLException exception) {
            getLogger().log(SEVERE, "Failed to connect to database", exception);
        }
        tileManager = new TileManager();
        immaterialRealm.setTileManager(tileManager);
        worldManager = new WorldManager(immaterialRealm);
        immaterialRealm.setWorldManager(worldManager);
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

        frame.setExtendedState(MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setTitle("ImmaterialRealm");
        frame.setFocusable(true);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                getNetworkManager().closeConnections();
                System.exit(0);
            }
        });
        EventQueue.invokeLater(() -> {
            frame.setVisible(true);
            frame.requestFocus();
        });
        run();
    }

    public void run() {
        setRunning(true);
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();
        while (isRunning()) {
            doTick();
            EventQueue.invokeLater(this::repaint);
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = tickDelay - timeDiff;
            if (sleep < 0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException exception) {
                getLogger().log(SEVERE, "Thread interrupted", exception);
            }
            fps = (int) (1000 / (System.currentTimeMillis() - beforeTime));
            beforeTime = System.currentTimeMillis();
        }
    }

    private void doTick() {
        if (connectionPanel.isVisible()) connectionPanel.onTick();
        if (loginPanel.isVisible()) loginPanel.onTick();
        if (worldPanel.isVisible()) worldPanel.onTick();
        if (characterCreationPanel.isVisible()) characterCreationPanel.onTick();
    }

    public JFrame getFrame() {
        return frame;
    }

    public Logger getLogger() {
        return logger;
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

    public ScriptEngineManager getScriptEngineManager() {
        return scriptEngineManager;
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

    public int getFPS() {
        return fps;
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

    public ImmaterialRealm getImmaterialRealm() {
        return immaterialRealm;
    }
}

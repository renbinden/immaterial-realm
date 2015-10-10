package io.github.alyphen.immaterial_realm.client.chat;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannel;
import io.github.alyphen.immaterial_realm.common.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.Color.WHITE;
import static java.awt.event.KeyEvent.*;
import static java.util.logging.Level.SEVERE;

public class ChatBox implements KeyListener {

    private ImmaterialRealmClient client;

    private JPanel panel;
    private String text;
    private boolean active;
    private boolean blink;
    private int blinkTick;
    private Map<ChatChannel, List<String>> globalMessages;

    public ChatBox(ImmaterialRealmClient client, JPanel panel) {
        this.client = client;
        this.panel = panel;
        text = "";
        globalMessages = new HashMap<>();
    }

    public void paint(Graphics graphics) {
        graphics.setColor(new Color(64, 64, 64, 128));
        int chatBoxHeight = 48;
        graphics.fillRoundRect(16, panel.getHeight() - chatBoxHeight, panel.getWidth() - 32, chatBoxHeight + 16, 16, 16);
        if (isActive()) {
            graphics.setColor(WHITE);
            graphics.drawRoundRect(24, panel.getHeight() - chatBoxHeight + 8, 64, graphics.getFontMetrics().getHeight() + 8, 4, 4);
            graphics.setColor(client.getChatManager().getChannel().getColour());
            graphics.drawString(client.getChatManager().getChannel().getName(), 28, panel.getHeight() - chatBoxHeight + 12 + graphics.getFontMetrics().getHeight());
            graphics.setColor(WHITE);
            graphics.drawRect(108, panel.getHeight() - chatBoxHeight + 8, panel.getWidth() - 136, graphics.getFontMetrics().getHeight() + 8);
            graphics.drawString(getText() + (blink ? "|" : ""), 112, panel.getHeight() - chatBoxHeight + 12 + graphics.getFontMetrics().getHeight());
            ChatChannel channel = client.getChatManager().getChannel();
            if (channel.getRadius() < 0) {
                graphics.setColor(new Color(64, 64, 64, 128));
                graphics.fillRoundRect(16, -16, panel.getWidth() - 32, panel.getHeight() - (chatBoxHeight + 16), 16, 16);
                graphics.setColor(WHITE);
                if (globalMessages.containsKey(channel)) {
                    List<String> messages = globalMessages.get(channel);
                    for (int i = messages.size() - 1; i >= 0; i--) {
                        graphics.drawString(messages.get(i), 24, panel.getHeight() - (chatBoxHeight + 24 + (graphics.getFontMetrics().getHeight() * (messages.size() - i))));
                    }
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) {
            blink = true;
            blinkTick = 0;
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void onTick() {
        if (isActive()) {
            blinkTick++;
            blinkTick = blinkTick >= 16 ? 0 : blinkTick;
            blink = blinkTick <= 7;
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
        if (event.getKeyChar() == VK_ENTER) {
            if (isActive()) {
                if (!getText().equals(""))
                    client.getChatManager().sendMessage(getText());
                setText("");
            }
            if (client.getChatManager().getChannel().getRadius() >= 0 || !isActive()) {
                setActive(!isActive());
            }
        } else if (isActive()) {
            if (event.getKeyChar() == VK_BACK_SPACE) {
                setText(getText().substring(0, Math.max(getText().length() - 1, 0)));
            } else if (event.getKeyChar() == VK_ESCAPE) {
                setText("");
                setActive(false);
            } else {
                text += event.getKeyChar();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (isActive()) {
            if (event.getKeyCode() == VK_DOWN) {
                ChatManager chatManager = client.getChatManager();
                if (chatManager.getChannel() == null) return;
                List<ChatChannel> chatChannelList = client.getImmaterialRealm().getChatChannelManager().getChatChannels().stream().collect(Collectors.toList());
                chatChannelList.sort(ChatChannel::compareTo);
                int channelIndex = chatChannelList.indexOf(chatManager.getChannel());
                if (channelIndex < chatChannelList.size() - 1)
                    chatManager.setChannel(chatChannelList.get(channelIndex + 1));
                else
                    chatManager.setChannel(chatChannelList.get(0));
            } else if (event.getKeyCode() == VK_UP) {
                ChatManager chatManager = client.getChatManager();
                if (chatManager.getChannel() == null) return;
                List<ChatChannel> chatChannelList = client.getImmaterialRealm().getChatChannelManager().getChatChannels().stream().collect(Collectors.toList());
                chatChannelList.sort(ChatChannel::compareTo);
                int channelIndex = chatChannelList.indexOf(chatManager.getChannel());
                if (channelIndex > 0)
                    chatManager.setChannel(chatChannelList.get(channelIndex - 1));
                else
                    chatManager.setChannel(chatChannelList.get(chatChannelList.size() - 1));
            }
        }
    }

    public void onGlobalMessage(UUID playerUUID, String channelName, String message) {
        try {
            Player player = client.getPlayerManager().getPlayer(playerUUID);
            ChatChannel channel = client.getImmaterialRealm().getChatChannelManager().getChatChannel(channelName);
            if (!globalMessages.containsKey(channel))
                globalMessages.put(channel, new ArrayList<>());
            globalMessages.get(channel).add(player.getName() + ": " + message);
            if (globalMessages.get(channel).size() > 50) {
                globalMessages.get(channel).remove(0);
            }
        } catch (SQLException exception) {
            client.getLogger().log(SEVERE, "Failed to get player for message", exception);
        }

    }

}

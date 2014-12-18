package io.github.alyphen.amethyst.client.chat;

import io.github.alyphen.amethyst.client.AmethystClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.Color.WHITE;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.lang.Character.isLetterOrDigit;

public class ChatBox implements KeyListener {

    private AmethystClient client;

    private JPanel panel;
    private static final int HEIGHT = 48;
    private String text;
    private boolean active;
    private boolean blink;
    private int blinkTick;

    public ChatBox(AmethystClient client, JPanel panel) {
        this.client = client;
        this.panel = panel;
        text = "";
    }

    public void render(Graphics graphics) {
        graphics.setColor(new Color(64, 64, 64, 128));
        graphics.fillRoundRect(16, panel.getHeight() - HEIGHT, panel.getWidth() - 32, HEIGHT + 16, 16, 16);
        if (isActive()) {
            graphics.setColor(WHITE);
            graphics.drawRect(24, panel.getHeight() - HEIGHT + 8, panel.getWidth() - 48, graphics.getFontMetrics().getHeight() + 8);
            graphics.drawString(getText() + (blink ? "|" : ""), 28, panel.getHeight() - HEIGHT + 12 + graphics.getFontMetrics().getHeight());
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
                client.getChatManager().sendMessage(getText());
                setText("");
            }
            setActive(!isActive());
        } else if (isActive()) {
            if (event.getKeyChar() == VK_BACK_SPACE) {
                setText(getText().substring(0, Math.max(getText().length() - 1, 0)));
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
    }

}

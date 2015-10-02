package io.github.alyphen.immaterial_realm.server.admin.tpsmonitor;

import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;

import javax.swing.*;
import java.awt.*;
import java.util.Deque;

import static java.awt.Color.BLACK;
import static java.awt.Color.RED;

public class TPSMonitorPanel extends JPanel {

    private ImmaterialRealmServer server;

    public TPSMonitorPanel(ImmaterialRealmServer server) {
        this.server = server;
        setPreferredSize(new Dimension(640, 256));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.setColor(BLACK);
        graphics.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        Deque<Integer> previousTPSValues = server.getPreviousTPSValues();
        Integer[] previousTPSValuesArray = new Integer[previousTPSValues.size()];
        previousTPSValues.toArray(previousTPSValuesArray);
        graphics.setColor(RED);
        for (int i = 1; i < previousTPSValuesArray.length; i++) {
            graphics.drawLine(i - 1, (getHeight() / 40) * (40 - previousTPSValuesArray[i - 1]), i, (getHeight() / 40) * (40 - previousTPSValuesArray[i]));
        }
    }
}

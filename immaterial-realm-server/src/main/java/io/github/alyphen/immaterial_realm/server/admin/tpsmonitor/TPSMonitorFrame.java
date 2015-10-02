package io.github.alyphen.immaterial_realm.server.admin.tpsmonitor;

import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;

import javax.swing.*;

public class TPSMonitorFrame extends JFrame {

    public TPSMonitorFrame(ImmaterialRealmServer server) {
        setTitle("TPS monitor");
        setResizable(false);
        add(new TPSMonitorPanel(server));
        pack();
        setLocationRelativeTo(null);
    }

}

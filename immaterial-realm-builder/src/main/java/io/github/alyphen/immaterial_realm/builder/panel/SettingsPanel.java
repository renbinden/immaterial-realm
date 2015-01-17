package io.github.alyphen.immaterial_realm.builder.panel;

import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;

public class SettingsPanel extends JPanel {

    public SettingsPanel(ImmaterialRealmBuilder application) {
        setLayout(new BorderLayout());
        add(new JLabel("There are currently no settings to set :(\nCheck back in later versions."), NORTH);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> application.showPanel("menu"));
        buttonsPanel.add(backButton);
        add(buttonsPanel, SOUTH);
    }

}

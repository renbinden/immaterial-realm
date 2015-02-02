package io.github.alyphen.immaterial_realm.builder.panel;

import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.JOptionPane.showMessageDialog;

public class SpritesPanel extends JPanel {

    public SpritesPanel(ImmaterialRealmBuilder application) {
        setLayout(new BorderLayout());
        JTable spritesTable = new JTable(new DefaultTableModel(0, 5) {
            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0: return "Name";
                    case 1: return "Image location";
                    case 2: return "Frame width";
                    case 3: return "Frame height";
                    case 4: return "Frame delay";
                    default: return super.getColumnName(column);
                }
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return String.class;
                    case 1: return File.class;
                    case 2: return Integer.class;
                    case 3: return Integer.class;
                    case 4: return Integer.class;
                    default: return super.getColumnClass(columnIndex);
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        });
        add(new JScrollPane(spritesTable), CENTER);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton btnAddSprite = new JButton("+");
        btnAddSprite.addActionListener(event -> ((DefaultTableModel) spritesTable.getModel()).addRow(new Object[] {"new_sprite", new File(""), 32, 32, 25}));
        buttonsPanel.add(btnAddSprite);
        JButton btnExportAll = new JButton("Export all...");
        btnExportAll.addActionListener(event -> {
            if (spritesTable.getRowCount() > 0) {
                for (int i = spritesTable.getRowCount() - 1; i >= 0; i--) {
                    String name = (String) spritesTable.getValueAt(i, 0);
                    File imageLocation = (File) spritesTable.getValueAt(i, 1);
                    BufferedImage image = null;
                    try {
                        image = ImageIO.read(imageLocation);
                    } catch (IOException exception) {
                        showMessageDialog(null, "Failed to load image: " + exception.getMessage());
                        exception.printStackTrace();
                    }
                    int frameWidth = (int) spritesTable.getValueAt(i, 2);
                    int frameHeight = (int) spritesTable.getValueAt(i, 3);
                    int frameDelay = (int) spritesTable.getValueAt(i, 4);
                    if (image != null) {
                        Sprite sprite = Sprite.fromImage(name, image, frameDelay, frameWidth, frameHeight);
                        try {
                            sprite.save(new File("./sprites/" + name));
                            ((DefaultTableModel) spritesTable.getModel()).removeRow(i);
                        } catch (IOException exception) {
                            showMessageDialog(null, "Failed to save sprite: " + exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                }
            }
        });
        buttonsPanel.add(btnExportAll);
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(event -> application.showPanel("menu"));
        buttonsPanel.add(btnBack);
        add(buttonsPanel, SOUTH);
    }

}

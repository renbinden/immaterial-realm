package io.github.alyphen.immaterial_realm.builder.panel;

import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;
import io.github.alyphen.immaterial_realm.common.tile.TileSheet;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.BorderLayout.*;
import static java.lang.Integer.MAX_VALUE;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JOptionPane.*;

public class TileSheetsPanel extends JPanel {

    private JTable existingTileSheetsTable;
    private TileSheet currentTileSheet;

    public TileSheetsPanel(ImmaterialRealmBuilder application) {
        TileSheet defaultTileSheet = TileSheet.getTileSheet("default");
        currentTileSheet = defaultTileSheet != null ? defaultTileSheet : TileSheet.create("default", new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), 16, 16);

        setLayout(new BorderLayout());
        JPanel tileSheetSelectionPanel = new JPanel();
        tileSheetSelectionPanel.setLayout(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel(0, 1) {
            @Override
            public String getColumnName(int column) {
                return "Tilesheets";
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        existingTileSheetsTable = new JTable(tableModel);
        refreshTileSheetList();
        JScrollPane scrollPane = new JScrollPane(existingTileSheetsTable);
        scrollPane.setPreferredSize(new Dimension(64, 480));
        tileSheetSelectionPanel.add(scrollPane, CENTER);
        JButton btnCreateTileSheet = new JButton("+");
        btnCreateTileSheet.addActionListener(event -> {
            try {
                String tileSheetName = showInputDialog(null, "Tilesheet name: ", "new_tilesheet");
                TileSheet.create(tileSheetName, new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), 16, 16).save(new File("./tilesheets/" + tileSheetName));
                refreshTileSheetList();
            } catch (IOException exception) {
                showMessageDialog(null, "Failed to create tilesheet: " + exception.getMessage(), "Failed to create tilesheet", ERROR_MESSAGE);
                exception.printStackTrace();
            }
        });
        tileSheetSelectionPanel.add(btnCreateTileSheet, SOUTH);
        add(tileSheetSelectionPanel, WEST);
        JPanel tileSheetEditPanel = new JPanel();
        tileSheetEditPanel.setLayout(new BoxLayout(tileSheetEditPanel, Y_AXIS));
        tileSheetEditPanel.add(Box.createGlue());
        JTextField nameField = new JTextField();
        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                currentTileSheet.setName(nameField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                currentTileSheet.setName(nameField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                currentTileSheet.setName(nameField.getText());
            }
        });
        nameField.setColumns(16);
        JComponent labelledNameField = labelComponent("Name: ", nameField);
        labelledNameField.setAlignmentX(CENTER_ALIGNMENT);
        tileSheetEditPanel.add(labelledNameField);
        JButton changeImageButton = new JButton("Browse...");
        changeImageButton.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().endsWith(".png");
                }

                @Override
                public String getDescription() {
                    return "Portable Network Graphics (*.png)";
                }
            });
            if (fileChooser.showSaveDialog(null) == APPROVE_OPTION) {
                try {
                    currentTileSheet.setSheetImage(ImageIO.read(fileChooser.getSelectedFile()));
                } catch (IOException exception) {
                    showMessageDialog(null, "Failed to load image: " + exception.getMessage(), "Failed to load image", ERROR_MESSAGE);
                    exception.printStackTrace();
                }
            }
        });
        JComponent labelledChangeImageButton = labelComponent("Change image: ", changeImageButton);
        labelledChangeImageButton.setAlignmentX(CENTER_ALIGNMENT);
        tileSheetEditPanel.add(labelledChangeImageButton);
        JSpinner tileWidthSpinner = new JSpinner(new SpinnerNumberModel(16, 1, MAX_VALUE, 1));
        tileWidthSpinner.addChangeListener(event -> currentTileSheet.setTileWidth((int) tileWidthSpinner.getValue()));
        JComponent labelledTileWidthSpinner = labelComponent("Tile width: ", tileWidthSpinner);
        labelledTileWidthSpinner.setAlignmentX(CENTER_ALIGNMENT);
        tileSheetEditPanel.add(labelledTileWidthSpinner);
        JSpinner tileHeightSpinner = new JSpinner(new SpinnerNumberModel(16, 1, MAX_VALUE, 1));
        tileHeightSpinner.addChangeListener(event -> currentTileSheet.setTileHeight((int) tileHeightSpinner.getValue()));
        tileSheetEditPanel.add(labelComponent("Tile height", tileHeightSpinner), CENTER_ALIGNMENT);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(event -> {
            try {
                currentTileSheet.save(new File("./tilesheets/" + currentTileSheet.getName()));
                refreshTileSheetList();
            } catch (IOException exception) {
                showMessageDialog(null, "Failed to save image: " + exception.getMessage(), "Failed to save image", ERROR_MESSAGE);
                exception.printStackTrace();
            }
        });
        tileSheetEditPanel.add(saveButton);
        tileSheetEditPanel.add(Box.createGlue());
        add(tileSheetEditPanel, CENTER);
        existingTileSheetsTable.getSelectionModel().addListSelectionListener(event -> {
            if (event.getFirstIndex() < existingTileSheetsTable.getRowCount()) {
                String tileSheetName = (String) existingTileSheetsTable.getValueAt(event.getFirstIndex(), 0);
                TileSheet tileSheet = TileSheet.getTileSheet(tileSheetName);
                currentTileSheet = tileSheet;
                nameField.setText(tileSheet.getName());
                tileWidthSpinner.setValue(tileSheet.getTileWidth());
                tileHeightSpinner.setValue(tileSheet.getTileHeight());
            }
        });
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(event -> application.showPanel("menu"));
        buttonsPanel.add(btnBack);
        add(buttonsPanel, SOUTH);
        nameField.setText(currentTileSheet.getName());
        tileWidthSpinner.setValue(currentTileSheet.getTileWidth());
        tileHeightSpinner.setValue(currentTileSheet.getTileHeight());
    }

    private JComponent labelComponent(String labelText, JComponent component) {
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout());
        JLabel label = new JLabel(labelText);
        label.setLabelFor(component);
        container.add(label);
        container.add(component);
        return container;
    }

    private void refreshTileSheetList() {
        if (existingTileSheetsTable.getRowCount() > 0) {
            for (int i = existingTileSheetsTable.getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) existingTileSheetsTable.getModel()).removeRow(i);
            }
        }
        for (TileSheet tileSheet : TileSheet.getTileSheets()) {
            ((DefaultTableModel) existingTileSheetsTable.getModel()).insertRow(existingTileSheetsTable.getRowCount(), new Object[] {tileSheet.getName()});
        }
    }

}

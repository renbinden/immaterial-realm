package io.github.alyphen.immaterial_realm.builder.panel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;
import io.github.alyphen.immaterial_realm.builder.celleditor.ColourChooserCellEditor;
import io.github.alyphen.immaterial_realm.builder.celleditor.SpinnerCellEditor;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.JOptionPane.showMessageDialog;

public class ChatDesignerPanel extends JPanel {

    private JTable channelTable;
    private JComboBox<String> defaultChannel;

    public ChatDesignerPanel(ImmaterialRealmBuilder application) {
        setLayout(new BorderLayout());
        channelTable = new JTable(new DefaultTableModel(new String[] {"Name", "Colour", "Radius"}, 0));
        channelTable.setRowHeight(32);
        channelTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel component = new JPanel();
                if (value instanceof Color) component.setBackground((Color) value);
                return component;
            }
        });
        channelTable.getColumnModel().getColumn(1).setCellEditor(new ColourChooserCellEditor());
        channelTable.getColumnModel().getColumn(2).setCellEditor(new SpinnerCellEditor());
        channelTable.getModel().addTableModelListener(event -> {
            defaultChannel.removeAllItems();
            for (int i = 0; i < channelTable.getRowCount(); i++) {
                defaultChannel.addItem((String) channelTable.getValueAt(i, 0));
            }
        });
        add(new JScrollPane(channelTable), CENTER);
        defaultChannel = new JComboBox<>(new String[] {});
        JPanel defaultChannelPanel = new JPanel();
        defaultChannelPanel.setLayout(new FlowLayout());
        JLabel defaultChannelLabel = new JLabel("Default channel: ");
        defaultChannelLabel.setLabelFor(defaultChannel);
        defaultChannelPanel.add(defaultChannelLabel);
        defaultChannelPanel.add(defaultChannel);
        add(defaultChannelPanel, NORTH);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        JButton btnAddChannel = new JButton("+");
        btnAddChannel.addActionListener(event -> {
            ((DefaultTableModel) channelTable.getModel()).insertRow(0, new Object[]{"channel", new Color(0, 0, 0), -1});
        });
        buttonsPanel.add(btnAddChannel);
        JButton btnRemoveChannel = new JButton("-");
        btnRemoveChannel.addActionListener(event -> {
            DefaultTableModel model = (DefaultTableModel) channelTable.getModel();
            while (channelTable.getSelectedRows().length > 0) {
                model.removeRow(channelTable.convertRowIndexToModel(channelTable.getSelectedRows()[0]));
            }
        });
        buttonsPanel.add(btnRemoveChannel);
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(event -> {
            File configDir = new File("./config");
            if (!configDir.exists()) configDir.mkdirs();
            File chatConfigFile = new File(configDir, "chat.json");
            Map<String, Object> chatConfig = new HashMap<>();
            Map<String, Object> channelsConfig = new HashMap<>();
            for (int i = 0; i < channelTable.getRowCount(); i++) {
                Map<String, Object> channelConfig = new HashMap<>();
                channelConfig.put("radius", channelTable.getValueAt(i, 2));
                Map<String, Object> channelColour = new HashMap<>();
                channelColour.put("red", ((Color) channelTable.getValueAt(i, 1)).getRed());
                channelColour.put("green", ((Color) channelTable.getValueAt(i, 1)).getGreen());
                channelColour.put("blue", ((Color) channelTable.getValueAt(i, 1)).getBlue());
                channelColour.put("alpha", ((Color) channelTable.getValueAt(i, 1)).getAlpha());
                channelConfig.put("colour", channelColour);
                channelsConfig.put((String) channelTable.getValueAt(i, 0), channelConfig);
            }
            chatConfig.put("channels", channelsConfig);
            chatConfig.put("default-channel", getDefaultChannel());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(chatConfig);
            try (FileWriter writer = new FileWriter(chatConfigFile)) {
                writer.write(json);
                showMessageDialog(null, "Saved to " + chatConfigFile.getAbsolutePath());
            } catch (IOException exception) {
                exception.printStackTrace();
                showMessageDialog(null, "Failed to save chat config: " + exception.getMessage());
            }
        });
        buttonsPanel.add(btnSave);
        JButton btnOpen = new JButton("Open");
        btnOpen.addActionListener(event -> {

        });
        buttonsPanel.add(btnOpen);
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(event -> application.showPanel("menu"));
        buttonsPanel.add(btnBack);
        add(buttonsPanel, SOUTH);
    }

    public String getDefaultChannel() {
        return (String) defaultChannel.getSelectedItem();
    }

}

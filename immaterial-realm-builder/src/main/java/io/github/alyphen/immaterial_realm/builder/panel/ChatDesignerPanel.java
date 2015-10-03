package io.github.alyphen.immaterial_realm.builder.panel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;
import io.github.alyphen.immaterial_realm.builder.celleditor.ColourChooserCellEditor;
import io.github.alyphen.immaterial_realm.builder.celleditor.SpinnerCellEditor;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.awt.BorderLayout.*;
import static java.util.logging.Level.SEVERE;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
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
                showMessageDialog(null, "Saved to " + chatConfigFile.getAbsolutePath(), "Chat config saved", INFORMATION_MESSAGE);
            } catch (IOException exception) {
                showMessageDialog(null, "Failed to save chat config: " + exception.getMessage(), "Failed to save chat config", ERROR_MESSAGE);
                application.getLogger().log(SEVERE, "Failed to save chat config", exception);
            }
        });
        buttonsPanel.add(btnSave);
        JButton btnOpen = new JButton("Open");
        btnOpen.addActionListener(event -> {
            File configDir = new File("./config");
            if (configDir.exists()) {
                File chatConfigFile = new File(configDir, "chat.json");
                if (chatConfigFile.exists()) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    StringBuilder jsonBuilder = new StringBuilder();
                    try {
                        Scanner scanner = new Scanner(new FileInputStream(chatConfigFile));
                        while (scanner.hasNextLine()) {
                            jsonBuilder.append(scanner.nextLine()).append('\n');
                        }
                    } catch (FileNotFoundException exception) {
                        showMessageDialog(null, "Failed to find chat config file: " + exception.getMessage(), "Failed to find chat config file", ERROR_MESSAGE);
                        application.getLogger().log(SEVERE, "Failed to find chat config file", exception);
                    }
                    Map<String, Object> chatConfig = gson.fromJson(jsonBuilder.toString(), new TypeToken<Map<String, Object>>(){}.getType());
                    Map<String, Object> channels = (Map<String, Object>) chatConfig.get("channels");
                    for (Map.Entry<String, Object> entry : channels.entrySet()) {
                        Map<String, Object> channelConfig = (Map<String, Object>) entry.getValue();
                        Map<String, Object> channelColour = (Map<String, Object>) channelConfig.get("colour");
                        ChatChannel channel = new ChatChannel(
                                entry.getKey(),
                                new Color(
                                        (int) ((double) channelColour.get("red")),
                                        (int) ((double) channelColour.get("green")),
                                        (int) ((double) channelColour.get("blue")),
                                        (int) ((double) channelColour.get("alpha"))
                                ),
                                (int) ((double) channelConfig.get("radius"))
                        );
                        ((DefaultTableModel) channelTable.getModel()).insertRow(channelTable.getRowCount(), new Object[] {channel.getName(), channel.getColour(), channel.getRadius()});
                    }
                    defaultChannel.setSelectedItem(chatConfig.get("default-channel"));
                }
            }
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

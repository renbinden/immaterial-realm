package io.github.alyphen.immaterial_realm.builder.panel.mapbuilder;

import io.github.alyphen.immaterial_realm.builder.panel.MapBuilderPanel;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.JOptionPane.showInputDialog;

public class AreaSelectPanel extends JPanel {

    private MapBuilderPanel mapBuilderPanel;
    private JTable areaTable;

    public AreaSelectPanel(MapBuilderPanel mapBuilderPanel) {
        this.mapBuilderPanel = mapBuilderPanel;
        setLayout(new BorderLayout());
        areaTable = new JTable(new DefaultTableModel(new String[] {"Areas"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        for (WorldArea area : mapBuilderPanel.getWorld().getAreas()) {
            ((DefaultTableModel) areaTable.getModel()).insertRow(areaTable.getRowCount(), new Object[] {area.getName()});
        }
        JScrollPane scrollPane = new JScrollPane(areaTable);
        scrollPane.setPreferredSize(new Dimension(64, 480));
        add(scrollPane, CENTER);
        JButton btnAddArea = new JButton("+");
        btnAddArea.addActionListener(event -> {
            mapBuilderPanel.getWorld().addArea(new WorldArea(mapBuilderPanel.getWorld(), showInputDialog("Name: "), 1, 1));
            refreshAreas();
        });
        add(btnAddArea, SOUTH);
    }

    public WorldArea getSelectedArea() {
        return areaTable.getSelectedRow() == -1 ? null : mapBuilderPanel.getWorld().getArea((String) areaTable.getValueAt(areaTable.getSelectedRow(), 0));
    }

    private void refreshAreas() {
        if (areaTable.getRowCount() > 0) {
            for (int i = areaTable.getRowCount() - 1; i >= 0; i--) {
                ((DefaultTableModel) areaTable.getModel()).removeRow(i);
            }
        }
        for (WorldArea area : mapBuilderPanel.getWorld().getAreas()) {
            ((DefaultTableModel) areaTable.getModel()).insertRow(areaTable.getRowCount(), new Object[] {area.getName()});
        }
    }

}

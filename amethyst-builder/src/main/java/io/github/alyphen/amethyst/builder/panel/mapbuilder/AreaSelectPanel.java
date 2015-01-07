package io.github.alyphen.amethyst.builder.panel.mapbuilder;

import io.github.alyphen.amethyst.builder.panel.MapBuilderPanel;
import io.github.alyphen.amethyst.common.world.WorldArea;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;

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
    }

    public WorldArea getSelectedArea() {
        return areaTable.getSelectedRow() == -1 ? null : mapBuilderPanel.getWorld().getArea((String) areaTable.getValueAt(areaTable.getSelectedRow(), 0));
    }

}

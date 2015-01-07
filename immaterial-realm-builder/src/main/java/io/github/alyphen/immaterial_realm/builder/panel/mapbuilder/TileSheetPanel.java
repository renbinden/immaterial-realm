package io.github.alyphen.immaterial_realm.builder.panel.mapbuilder;

import io.github.alyphen.immaterial_realm.common.tile.TileSheet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.BorderLayout.CENTER;
import static java.lang.Boolean.TRUE;

public class TileSheetPanel extends JPanel {

    private JTable tileSheetsTable;

    public TileSheetPanel() {
        setLayout(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel(0, 2) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Boolean.class;
                    case 1:
                    default:
                        return String.class;
                }
            }
        };
        tileSheetsTable = new JTable(tableModel);
        tileSheetsTable.getColumnModel().getColumn(0).setHeaderValue("Enabled");
        tileSheetsTable.getColumnModel().getColumn(1).setHeaderValue("Name");
        tileSheetsTable.getColumnModel().getColumn(0).setPreferredWidth(64);
        tileSheetsTable.getColumnModel().getColumn(1).setPreferredWidth(128);
        for (TileSheet sheet : TileSheet.getTileSheets()) {
            tableModel.insertRow(tableModel.getRowCount(), new Object[] {true, sheet.getName()});
        }
        JScrollPane scrollPane = new JScrollPane(tileSheetsTable);
        scrollPane.setPreferredSize(new Dimension(192, 240));
        add(scrollPane, CENTER);
    }

    public List<TileSheet> getSelectedTileSheets() {
        List<TileSheet> sheets = new ArrayList<>();
        for (int i = 0; i < tileSheetsTable.getRowCount(); i++) {
            if (tileSheetsTable.getValueAt(i, 0) == TRUE) {
                sheets.add(TileSheet.getTileSheet((String) tileSheetsTable.getValueAt(i, 1)));
            }
        }
        return sheets;
    }
}

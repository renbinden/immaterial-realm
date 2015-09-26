package io.github.alyphen.immaterial_realm.builder.panel.mapbuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ObjectsPanel extends JPanel {

    private JTable objectsTable;

    public ObjectsPanel() {
        DefaultTableModel tableModel = new DefaultTableModel() {

            @Override
            public String getColumnName(int column) {
                return "Object";
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public int getColumnCount() {
                return 1;
            }
        };
        objectsTable = new JTable(tableModel);
        List<String> objectTypeNames = new ArrayList<>();
        File objectsDirectory = new File("./objects");
        if (objectsDirectory.exists()) {
            for (File objectDirectory : objectsDirectory.listFiles(File::isDirectory)) {
                objectTypeNames.add(objectDirectory.getName());
            }
        }
        objectTypeNames.sort(null);
        objectTypeNames.forEach(objectTypeName -> ((DefaultTableModel) objectsTable.getModel()).addRow(new String[]{objectTypeName}));
        JScrollPane scrollPane = new JScrollPane(objectsTable);
        scrollPane.setPreferredSize(new Dimension(128, 600));
        add(scrollPane);
    }

    public String getSelectedObjectType() {
        return (String) objectsTable.getModel().getValueAt(objectsTable.getSelectedRow(), 0);
    }

}

package io.github.alyphen.immaterial_realm.builder.celleditor;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColourChooserCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    Color currentColor;
    JButton button;
    JColorChooser colourChooser;
    JDialog dialog;
    protected static final String EDIT = "Edit";

    public ColourChooserCellEditor() {
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);

        //Set up the dialog that the button brings up.
        colourChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button,
                "Pick a Color",
                true,  //modal
                colourChooser,
                this,  //OK button handler
                null); //no CANCEL button handler
    }

    public void actionPerformed(ActionEvent event) {
        if (EDIT.equals(event.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            button.setBackground(currentColor);
            colourChooser.setColor(currentColor);
            dialog.setVisible(true);
            fireEditingStopped(); //Make the renderer reappear.
        } else { //User pressed dialog's "OK" button.
            currentColor = colourChooser.getColor();
        }
    }

    //Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return currentColor;
    }

    //Implement the one method defined by TableCellEditor.
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentColor = (Color)value;
        return button;
    }
}

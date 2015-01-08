package io.github.alyphen.immaterial-realm.builder.celleditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.EventObject;

import static java.lang.String.valueOf;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.SwingUtilities.invokeLater;

public class SpinnerCellEditor extends DefaultCellEditor {

    JSpinner spinner;
    JSpinner.DefaultEditor editor;
    JTextField textField;
    boolean valueSet;

    public SpinnerCellEditor() {
        super(new JTextField());
        spinner = new JSpinner();
        editor = ((JSpinner.DefaultEditor) spinner.getEditor());
        textField = editor.getTextField();
        textField.addFocusListener( new FocusListener() {
            public void focusGained(FocusEvent event) {
                invokeLater(() -> {
                    if (valueSet) {
                        textField.setCaretPosition(1);
                    }
                });
            }
            public void focusLost(FocusEvent event) {
            }
        });
        textField.addActionListener(event -> stopCellEditing());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (!valueSet) {
            spinner.setValue(value);
        }
        invokeLater(textField::requestFocus);
        return spinner;
    }

    public boolean isCellEditable(EventObject eventObject) {
        if (eventObject instanceof KeyEvent) {
            KeyEvent event = (KeyEvent) eventObject;
            textField.setText(valueOf(event.getKeyChar()));
            valueSet = true;
        } else {
            valueSet = false;
        }
        return true;
    }

    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    public boolean stopCellEditing() {
        try {
            editor.commitEdit();
            spinner.commitEdit();
        } catch (ParseException exception) {
            showMessageDialog(null, "Invalid value, discarding.");
        }
        return super.stopCellEditing();
    }
}

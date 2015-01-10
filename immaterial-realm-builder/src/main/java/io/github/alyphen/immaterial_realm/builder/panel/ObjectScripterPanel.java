package io.github.alyphen.immaterial_realm.builder.panel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.List;

import static java.awt.BorderLayout.*;
import static java.lang.Integer.MAX_VALUE;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.fife.ui.rsyntaxtextarea.SyntaxConstants.*;

public class ObjectScripterPanel extends JPanel {

    private JComboBox<String> objectSelectionBox;
    private JComboBox<String> languageSelectionBox;
    private JTextField textFieldObjectName;
    private JSpinner xOffsetSpinner;
    private JSpinner yOffsetSpinner;
    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
    private RSyntaxTextArea editor;

    public ObjectScripterPanel(ImmaterialRealmBuilder application) {
        setLayout(new BorderLayout());
        editor = new RSyntaxTextArea();
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/editor/themes/dark.xml"));
            theme.apply(editor);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        editor.setSyntaxEditingStyle(SYNTAX_STYLE_JAVASCRIPT);
        editor.setText(
                "function create() {\n" +
                "    \n" +
                "}\n" +
                "\n" +
                "function interact() {\n" +
                "    \n" +
                "}\n" +
                "\n" +
                "function tick() {\n" +
                "    \n" +
                "}\n"
        );
        add(new RTextScrollPane(editor), CENTER);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JPanel objectSettingsPanel = new JPanel();
        objectSettingsPanel.setLayout(new FlowLayout());
        textFieldObjectName = new JTextField();
        textFieldObjectName.setPreferredSize(new Dimension(128, 24));
        JLabel nameLabel = new JLabel("Name: ");
        nameLabel.setLabelFor(textFieldObjectName);
        objectSettingsPanel.add(nameLabel);
        objectSettingsPanel.add(textFieldObjectName);
        xOffsetSpinner = new JSpinner(new SpinnerNumberModel(0, 0, MAX_VALUE, 1));
        JLabel xOffsetLabel = new JLabel("Bounds X Offset: ");
        xOffsetLabel.setLabelFor(xOffsetSpinner);
        objectSettingsPanel.add(xOffsetLabel);
        objectSettingsPanel.add(xOffsetSpinner);
        yOffsetSpinner = new JSpinner(new SpinnerNumberModel(0, 0, MAX_VALUE, 1));
        JLabel yOffsetLabel = new JLabel("Bounds Y Offset: ");
        yOffsetLabel.setLabelFor(yOffsetSpinner);
        objectSettingsPanel.add(yOffsetLabel);
        objectSettingsPanel.add(yOffsetSpinner);
        widthSpinner = new JSpinner(new SpinnerNumberModel(32, 0, MAX_VALUE, 1));
        JLabel widthLabel = new JLabel("Bounds Width: ");
        widthLabel.setLabelFor(widthSpinner);
        objectSettingsPanel.add(widthLabel);
        objectSettingsPanel.add(widthSpinner);
        heightSpinner = new JSpinner(new SpinnerNumberModel(32, 0, MAX_VALUE, 1));
        JLabel heightLabel = new JLabel("Bounds Height: ");
        heightLabel.setLabelFor(heightSpinner);
        objectSettingsPanel.add(heightLabel);
        objectSettingsPanel.add(heightSpinner);
        topPanel.add(objectSettingsPanel, SOUTH);
        JPanel objectSelectorPanel = new JPanel();
        objectSelectorPanel.setLayout(new FlowLayout());
        languageSelectionBox = new JComboBox<>(new String[] {SYNTAX_STYLE_JAVASCRIPT, SYNTAX_STYLE_PYTHON, SYNTAX_STYLE_RUBY});
        languageSelectionBox.addActionListener(event -> editor.setSyntaxEditingStyle((String) languageSelectionBox.getSelectedItem()));
        objectSelectionBox = new JComboBox<>();
        refreshObjectSelectionBox();
        objectSelectionBox.addActionListener(event -> {
            try {
                save();
            } catch (IOException exception) {
                showMessageDialog(null, "Saving object type failed: " + exception.getMessage());
                exception.printStackTrace();
            }
            try {
                load();
            } catch (FileNotFoundException exception) {
                showMessageDialog(null, "Loading object type failed: " + exception.getMessage());
                exception.printStackTrace();
            }
        });
        objectSelectorPanel.add(objectSelectionBox);
        JButton btnAddObjectType = new JButton("+");
        btnAddObjectType.addActionListener(event -> {
            String name = showInputDialog("Name: ");
            if (name != null) {
                textFieldObjectName.setText(name);
                try {
                    save();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                refreshObjectSelectionBox();
                objectSelectionBox.setSelectedItem(name);
                textFieldObjectName.setText(name);
                xOffsetSpinner.setValue(0);
                yOffsetSpinner.setValue(0);
                widthSpinner.setValue(32);
                heightSpinner.setValue(32);
                editor.setText(
                        "function create() {\n" +
                                "    \n" +
                                "}\n" +
                                "\n" +
                                "function interact() {\n" +
                                "    \n" +
                                "}\n" +
                                "\n" +
                                "function tick() {\n" +
                                "    \n" +
                                "}\n"
                );
                languageSelectionBox.setSelectedItem(SYNTAX_STYLE_JAVASCRIPT);
            }
        });
        objectSelectorPanel.add(btnAddObjectType);
        objectSelectorPanel.add(languageSelectionBox);
        topPanel.add(objectSelectorPanel, CENTER);
        add(topPanel, NORTH);
        JPanel buttonsPanel = new JPanel();
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(event -> application.showPanel("menu"));
        buttonsPanel.add(btnBack);
        add(buttonsPanel, SOUTH);
        application.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    save();
                } catch (IOException exception) {
                    showMessageDialog(null, "Saving object failed: " + exception.getMessage());
                    exception.printStackTrace();
                }
            }
        });
    }

    public void save() throws IOException {
        if (textFieldObjectName.getText().equals("")) return;
        File objectsDirectory = new File("./objects");
        File objectDirectory = new File(objectsDirectory, textFieldObjectName.getText());
        if (!objectDirectory.exists()) objectDirectory.mkdirs();
        File metadataFile = new File(objectDirectory, "object.json");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("name", textFieldObjectName.getText());
        metadata.put("bounds_offset_x", xOffsetSpinner.getValue());
        metadata.put("bounds_offset_y", yOffsetSpinner.getValue());
        metadata.put("bounds_width", widthSpinner.getValue());
        metadata.put("bounds_height", heightSpinner.getValue());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(metadata);
        FileWriter metadataWriter = new FileWriter(metadataFile);
        metadataWriter.write(json);
        metadataWriter.close();
        File scriptFile;
        File jsScriptFile = new File(objectDirectory, "object.js");
        File pyScriptFile = new File(objectDirectory, "object.py");
        File rbScriptFile = new File(objectDirectory, "object.rb");
        if (languageSelectionBox.getSelectedItem().equals(SYNTAX_STYLE_JAVASCRIPT)) {
            scriptFile = jsScriptFile;
            pyScriptFile.delete();
            rbScriptFile.delete();
        } else if (languageSelectionBox.getSelectedItem().equals(SYNTAX_STYLE_PYTHON)) {
            scriptFile = pyScriptFile;
            jsScriptFile.delete();
            rbScriptFile.delete();
        } else if (languageSelectionBox.getSelectedItem().equals(SYNTAX_STYLE_RUBY)) {
            scriptFile = rbScriptFile;
            jsScriptFile.delete();
            pyScriptFile.delete();
        } else {
            scriptFile = jsScriptFile;
            pyScriptFile.delete();
            rbScriptFile.delete();
        }
        FileWriter scriptWriter = new FileWriter(scriptFile);
        scriptWriter.write(editor.getText());
        scriptWriter.close();
        refreshObjectSelectionBox();
    }

    public void load() throws FileNotFoundException {
        if (objectSelectionBox.getSelectedItem() == null) return;
        File objectFile = new File("./objects/" + objectSelectionBox.getSelectedItem());
        File metadataFile = new File(objectFile, "object.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        StringBuilder jsonBuilder = new StringBuilder();
        Scanner metadataScanner = new Scanner(new FileInputStream(metadataFile));
        while (metadataScanner.hasNextLine()) jsonBuilder.append(metadataScanner.nextLine()).append("\n");
        metadataScanner.close();
        Map<String, Object> metadata = gson.fromJson(jsonBuilder.toString(), new TypeToken<Map<String, Object>>() {
        }.getType());
        if (metadata != null) {
            textFieldObjectName.setText((String) metadata.get("name"));
            xOffsetSpinner.setValue((int) ((double) metadata.get("bounds_offset_x")));
            yOffsetSpinner.setValue((int) ((double) metadata.get("bounds_offset_y")));
            widthSpinner.setValue((int) ((double) metadata.get("bounds_width")));
            heightSpinner.setValue((int) ((double) metadata.get("bounds_height")));
        } else {
            textFieldObjectName.setText(objectFile.getName());
            xOffsetSpinner.setValue(0);
            yOffsetSpinner.setValue(0);
            widthSpinner.setValue(32);
            heightSpinner.setValue(32);
        }
        File scriptFile = new File(objectFile, "object.js");
        if (scriptFile.exists()) {
            languageSelectionBox.setSelectedItem(SYNTAX_STYLE_JAVASCRIPT);
        } else {
            scriptFile = new File(objectFile, "object.py");
            if (scriptFile.exists()) {
                languageSelectionBox.setSelectedItem(SYNTAX_STYLE_PYTHON);
            } else {
                scriptFile = new File(objectFile, "object.rb");
                if (scriptFile.exists()) {
                    languageSelectionBox.setSelectedItem(SYNTAX_STYLE_RUBY);
                } else {
                    scriptFile = new File(objectFile, "object.js");
                }
            }
        }
        editor.setText("");
        if (scriptFile.exists()) {
            Scanner scriptScanner = new Scanner(new FileInputStream(scriptFile));
            while (scriptScanner.hasNextLine()) {
                editor.append(scriptScanner.nextLine() + "\n");
            }
            scriptScanner.close();
        }
        if (editor.getText().equals("")) {
            editor.setText(
                    "function create() {\n" +
                    "    \n" +
                    "}\n" +
                    "\n" +
                    "function interact() {\n" +
                    "    \n" +
                    "}\n" +
                    "\n" +
                    "function tick() {\n" +
                    "    \n" +
                    "}\n"
            );
        }
    }

    private void refreshObjectSelectionBox() {
        List<String> objectTypeNames = new ArrayList<>();
        File objectsDirectory = new File("./objects");
        if (objectsDirectory.exists()) {
            for (File objectDirectory : objectsDirectory.listFiles(File::isDirectory)) {
                objectTypeNames.add(objectDirectory.getName());
            }
        }
        objectTypeNames.sort(null);
        String currentSelection = (String) objectSelectionBox.getSelectedItem();
        objectSelectionBox.removeAllItems();
        objectTypeNames.forEach(objectSelectionBox::addItem);
        if (currentSelection != null) {
            objectSelectionBox.setSelectedItem(currentSelection);
        }
        if (objectSelectionBox.getSelectedItem() != null) {
            try {
                load();
            } catch (FileNotFoundException exception) {
                showMessageDialog(null, "Loading object failed: " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

}

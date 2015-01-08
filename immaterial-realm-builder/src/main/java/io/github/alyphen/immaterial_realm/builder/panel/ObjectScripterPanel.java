package io.github.alyphen.immaterial_realm.builder.panel;

import io.github.alyphen.immaterial_realm.common.object.WorldObjectFactory;
import io.github.alyphen.immaterial_realm.common.object.WorldObjectInitializer;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static org.fife.ui.rsyntaxtextarea.SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;

public class ObjectScripterPanel extends JPanel {

    private JComboBox<String> objectSelectionBox;
    private RSyntaxTextArea editor;

    public ObjectScripterPanel() {
        setLayout(new BorderLayout());
        editor = new RSyntaxTextArea();
        editor.setSyntaxEditingStyle(SYNTAX_STYLE_JAVASCRIPT);
        add(new RTextScrollPane(editor), CENTER);
        JPanel objectSelectorPanel = new JPanel();
        objectSelectorPanel.setLayout(new FlowLayout());
        List<String> objectTypeNames = WorldObjectFactory.getObjectInitializers().stream().map(WorldObjectInitializer::getObjectName).collect(Collectors.toList());
        objectTypeNames.sort(null);
        objectSelectionBox = new JComboBox<>(objectTypeNames.toArray(new String[objectTypeNames.size()]));
        objectSelectorPanel.add(objectSelectionBox);
        JButton btnAddObjectType = new JButton("+");
        objectSelectorPanel.add(btnAddObjectType);
        add(objectSelectorPanel, NORTH);
    }
}

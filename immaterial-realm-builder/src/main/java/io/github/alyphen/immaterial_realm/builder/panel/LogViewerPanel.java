package io.github.alyphen.immaterial_realm.builder.panel;

import io.github.alyphen.immaterial_realm.builder.ImmaterialRealmBuilder;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;

public class LogViewerPanel extends JPanel{

    private JTextPane textPane;
    private HTMLEditorKit kit;
    private HTMLDocument document;

    public LogViewerPanel(ImmaterialRealmBuilder application) {
        setLayout(new BorderLayout());
        textPane = new JTextPane();
        kit = new HTMLEditorKit();
        document = new HTMLDocument();
        textPane.setBackground(Color.DARK_GRAY);
        textPane.setEditorKit(kit);
        textPane.setDocument(document);
        loadLogs();
        add(new JScrollPane(textPane), CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> application.showPanel("menu"));
        buttonPanel.add(backButton);
        add(buttonPanel, SOUTH);
    }

    public void appendLine(String line) {
        try {
            kit.insertHTML(
                    document,
                    document.getLength(),
                    line.replace("(SEVERE)", "<span style='color: #FF0000'>(SEVERE)</span>")
                        .replace("(WARNING)", "<span style='color: #FF7F00'>(WARNING)</span>")
                        .replace("(INFO)", "<span style='color: #0000FF'>(INFO)</span>")
                        .replace("(FINE)", "<span style='color: #00FFFF'>(FINE)</span>")
                        .replace("(FINER)", "<span style='color: #00FF00'>(FINER)</span>")
                        .replace("(FINEST)", "<span style='color: #FFFF00'>(FINEST)</span>")
                        .replaceFirst("\\[", "<span style='color: #BFBFBF'>[")
                        .replaceFirst("\\]", "]</span><span style='color: #FFFFFF'>") +
                    "</span>\n",
                    0,
                    0,
                    null
            );
        } catch (BadLocationException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadLogs() {
        textPane.setEditable(false);
        File logDirectory = new File("./logs");
        if (logDirectory.exists() && logDirectory.isDirectory()) {
            for (File file : logDirectory.listFiles((dir, name) -> name.endsWith(".log"))) {
                loadLog(file);
            }
        }
    }

    private void loadLog(File file) {
        try {
            Scanner scanner = new Scanner(new FileInputStream(file));
            while (scanner.hasNextLine()) {
                appendLine(scanner.nextLine());
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

}

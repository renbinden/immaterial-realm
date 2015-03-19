package io.github.alyphen.immaterial_realm.client.panel;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;
import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.character.PacketSaveCharacter;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BoxLayout.Y_AXIS;

public class CharacterCreationPanel extends JPanel {

    private CharacterAppearancePanel characterAppearancePanel;

    private JTextField textFieldName;
    private JComboBox<String> comboBoxGender;
    private JComboBox<String> comboBoxRace;
    private JTextArea textAreaDescription;

    public CharacterCreationPanel(ImmaterialRealmClient client) {
        setLayout(new BoxLayout(this, Y_AXIS));
        characterAppearancePanel = new CharacterAppearancePanel();
        add(characterAppearancePanel);
        textFieldName = new JTextField();
        textFieldName.setColumns(10);
        textFieldName.setMaximumSize(new Dimension(1920, 24));
        JLabel lblName = new JLabel("Name: ");
        lblName.setAlignmentX(CENTER_ALIGNMENT);
        lblName.setLabelFor(textFieldName);
        add(lblName);
        add(textFieldName);
        comboBoxGender = new JComboBox<>();
        comboBoxGender.setMaximumSize(new Dimension(1920, 24));
        JLabel lblGender = new JLabel("Gender: ");
        lblGender.setAlignmentX(CENTER_ALIGNMENT);
        lblGender.setLabelFor(comboBoxGender);
        add(lblGender);
        add(comboBoxGender);
        comboBoxRace = new JComboBox<>();
        comboBoxRace.setMaximumSize(new Dimension(1920, 24));
        JLabel lblRace = new JLabel("Race: ");
        lblRace.setAlignmentX(CENTER_ALIGNMENT);
        lblRace.setLabelFor(comboBoxRace);
        add(lblRace);
        add(comboBoxRace);
        textAreaDescription = new JTextArea();
        textAreaDescription.setColumns(32);
        textAreaDescription.setRows(8);
        textAreaDescription.setLineWrap(true);
        JLabel lblDescription = new JLabel("Description: ");
        lblDescription.setAlignmentX(CENTER_ALIGNMENT);
        lblDescription.setLabelFor(textAreaDescription);
        add(lblDescription);
        add(textAreaDescription);
        JButton btnSave = new JButton("Save");
        btnSave.setAlignmentX(CENTER_ALIGNMENT);
        btnSave.addActionListener(event -> client.getNetworkManager().sendPacket(
                new PacketSaveCharacter(
                        characterAppearancePanel.getHairId(),
                        characterAppearancePanel.getFaceId(),
                        characterAppearancePanel.getTorsoId(),
                        characterAppearancePanel.getLegsId(),
                        characterAppearancePanel.getFeetId(),
                        textFieldName.getText(),
                        (String) comboBoxGender.getSelectedItem(),
                        (String) comboBoxRace.getSelectedItem(),
                        textAreaDescription.getText(),
                        false
                )
        ));
        add(btnSave);
    }

    public void addGender(String gender) {
        comboBoxGender.addItem(gender);
    }

    public void addRace(String race) {
        comboBoxRace.addItem(race);
    }

    public void addHairSprite(Sprite sprite) {
        characterAppearancePanel.addHairSprite(sprite);
    }

    public void addFaceSprite(Sprite sprite) {
        characterAppearancePanel.addFaceSprite(sprite);
    }

    public void addTorsoSprite(Sprite sprite) {
        characterAppearancePanel.addTorsoSprite(sprite);
    }

    public void addLegsSprite(Sprite sprite) {
        characterAppearancePanel.addLegsSprite(sprite);
    }

    public void addFeetSprite(Sprite sprite) {
        characterAppearancePanel.addFeetSprite(sprite);
    }

    public void onTick() {
        characterAppearancePanel.onTick();
    }

    public void updateFields(Character character) {
        textFieldName.setText(character.getName());
        comboBoxGender.setSelectedItem(character.getGender());
        comboBoxRace.setSelectedItem(character.getRace());
        textAreaDescription.setText(character.getDescription());
    }
}

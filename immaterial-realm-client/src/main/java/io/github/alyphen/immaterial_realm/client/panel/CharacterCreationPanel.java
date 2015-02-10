package io.github.alyphen.immaterial_realm.client.panel;

import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterCreationPanel extends JPanel {

    private List<Sprite> hairSprites;
    private int hairIndex;
    private List<Sprite> faceSprites;
    private int faceIndex;
    private List<Sprite> torsoSprites;
    private int torsoIndex;
    private List<Sprite> legsSprites;
    private int legsIndex;
    private List<Sprite> feetSprites;
    private int feetIndex;

    public CharacterCreationPanel() {
        hairSprites = new ArrayList<>();
        faceSprites = new ArrayList<>();
        torsoSprites = new ArrayList<>();
        legsSprites = new ArrayList<>();
        feetSprites = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(Color.WHITE);
        graphics.drawRect(16, 16, 48, 80);
        getSelectedHairSprite().paint(graphics, 32, 32);
        getSelectedLegsSprite().paint(graphics, 32, 32);
        getSelectedFeetSprite().paint(graphics, 32, 32);
        getSelectedTorsoSprite().paint(graphics, 32, 32);
        getSelectedFaceSprite().paint(graphics, 32, 32);

    }

    public List<Sprite> getHairSprites() {
        return hairSprites;
    }

    public Sprite getSelectedHairSprite() {
        return getHairSprites().get(hairIndex);
    }

    public void addHairSprite(Sprite sprite) {
        hairSprites.add(sprite);
    }

    public List<Sprite> getFaceSprites() {
        return faceSprites;
    }

    public Sprite getSelectedFaceSprite() {
        return getFaceSprites().get(faceIndex);
    }

    public void addFaceSprite(Sprite sprite) {
        faceSprites.add(sprite);
    }

    public List<Sprite> getTorsoSprites() {
        return torsoSprites;
    }

    public Sprite getSelectedTorsoSprite() {
        return getTorsoSprites().get(torsoIndex);
    }

    public void addTorsoSprite(Sprite sprite) {
        torsoSprites.add(sprite);
    }

    public List<Sprite> getLegsSprites() {
        return legsSprites;
    }

    public Sprite getSelectedLegsSprite() {
        return getLegsSprites().get(legsIndex);
    }

    public void addLegsSprite(Sprite sprite) {
        legsSprites.add(sprite);
    }

    public List<Sprite> getFeetSprites() {
        return feetSprites;
    }

    public Sprite getSelectedFeetSprite() {
        return getFeetSprites().get(feetIndex);
    }

    public void addFeetSprite(Sprite sprite) {
        feetSprites.add(sprite);
    }

}

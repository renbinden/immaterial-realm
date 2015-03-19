package io.github.alyphen.immaterial_realm.client.panel;

import io.github.alyphen.immaterial_realm.common.sprite.Sprite;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.LIGHT_GRAY;
import static java.awt.Color.WHITE;

public class CharacterAppearancePanel extends JPanel {

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

    public CharacterAppearancePanel() {
        setPreferredSize(new Dimension(80, 112));
        hairSprites = new ArrayList<>();
        faceSprites = new ArrayList<>();
        torsoSprites = new ArrayList<>();
        legsSprites = new ArrayList<>();
        feetSprites = new ArrayList<>();
        addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                int xOffset = getWidth() / 2;
                if (createLeftArrow(xOffset - 36, 16).contains(event.getX(), event.getY())) {
                    hairIndex = hairIndex > 0 ? hairIndex - 1 : hairSprites.size() - 1;
                } else if (createRightArrow(xOffset + 28, 16).contains(event.getX(), event.getY())) {
                    hairIndex = hairIndex < hairSprites.size() - 1 ? hairIndex + 1 : 0;
                } else if (createLeftArrow(xOffset - 36, 32).contains(event.getX(), event.getY())) {
                    faceIndex = faceIndex > 0 ? faceIndex - 1 : faceSprites.size() - 1;
                } else if (createRightArrow(xOffset + 28, 32).contains(event.getX(), event.getY())) {
                    faceIndex = faceIndex < faceSprites.size() - 1 ? faceIndex + 1 : 0;
                } else if (createLeftArrow(xOffset - 36, 48).contains(event.getX(), event.getY())) {
                    torsoIndex = torsoIndex > 0 ? torsoIndex - 1 : torsoSprites.size() - 1;
                } else if (createRightArrow(xOffset + 28, 48).contains(event.getX(), event.getY())) {
                    torsoIndex = torsoIndex < torsoSprites.size() - 1 ? torsoIndex + 1 : 0;
                } else if (createLeftArrow(xOffset - 36, 64).contains(event.getX(), event.getY())) {
                    legsIndex = legsIndex > 0 ? legsIndex - 1 : legsSprites.size() - 1;
                } else if (createRightArrow(xOffset + 28, 64).contains(event.getX(), event.getY())) {
                    legsIndex = legsIndex < legsSprites.size() - 1 ? legsIndex + 1 : 0;
                } else if (createLeftArrow(xOffset - 36, 80).contains(event.getX(), event.getY())) {
                    feetIndex = feetIndex > 0 ? feetIndex - 1 : feetSprites.size() - 1;
                } else if (createRightArrow(xOffset + 28, 80).contains(event.getX(), event.getY())) {
                    feetIndex = feetIndex < feetSprites.size() - 1 ? feetIndex + 1 : 0;
                }
            }
        });
    }

    public int getHairId() {
        return hairIndex;
    }

    public int getFaceId() {
        return faceIndex;
    }

    public int getTorsoId() {
        return torsoIndex;
    }

    public int getLegsId() {
        return legsIndex;
    }

    public int getFeetId() {
        return feetIndex;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        int xOffset = getWidth() / 2;
        graphics.setColor(LIGHT_GRAY);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(WHITE);
        graphics.drawRect(xOffset - 24, 16, 48, 80);
        if (getSelectedHairSprite() != null) getSelectedHairSprite().paint(graphics, xOffset - 16, 32);
        if (getSelectedLegsSprite() != null) getSelectedLegsSprite().paint(graphics, xOffset - 16, 32);
        if (getSelectedTorsoSprite() != null) getSelectedTorsoSprite().paint(graphics, xOffset - 16, 32);
        if (getSelectedFeetSprite() != null) getSelectedFeetSprite().paint(graphics, xOffset - 16, 32);
        if (getSelectedFaceSprite() != null) getSelectedFaceSprite().paint(graphics, xOffset - 16, 32);
        Graphics2D graphics2D = (Graphics2D) graphics;
        paintPreviousHairIndexButton(graphics2D);
        paintNextHairIndexButton(graphics2D);
        paintPreviousFaceIndexButton(graphics2D);
        paintNextFaceIndexButton(graphics2D);
        paintPreviousTorsoIndexButton(graphics2D);
        paintNextFaceTorsoIndexButton(graphics2D);
        paintPreviousLegsIndexButton(graphics2D);
        paintNextLegsIndexButton(graphics2D);
        paintPreviousFeetIndexButton(graphics2D);
        paintNextFeetIndexButton(graphics2D);
    }

    private void paintPreviousHairIndexButton(Graphics2D graphics) {
        paintLeftArrow(graphics, (getWidth() / 2) - 36, 16);
    }

    private void paintNextHairIndexButton(Graphics2D graphics) {
        paintRightArrow(graphics, (getWidth() / 2) + 28, 16);
    }

    private void paintPreviousFaceIndexButton(Graphics2D graphics) {
        paintLeftArrow(graphics, (getWidth() / 2) - 36, 32);
    }

    private void paintNextFaceIndexButton(Graphics2D graphics) {
        paintRightArrow(graphics, (getWidth() / 2) + 28, 32);
    }

    private void paintPreviousTorsoIndexButton(Graphics2D graphics) {
        paintLeftArrow(graphics, (getWidth() / 2) - 36, 48);
    }

    private void paintNextFaceTorsoIndexButton(Graphics2D graphics) {
        paintRightArrow(graphics, (getWidth() / 2) + 28, 48);
    }

    private void paintPreviousLegsIndexButton(Graphics2D graphics) {
        paintLeftArrow(graphics, (getWidth() / 2) - 36, 64);
    }

    private void paintNextLegsIndexButton(Graphics2D graphics) {
        paintRightArrow(graphics, (getWidth() / 2) + 28, 64);
    }

    private void paintPreviousFeetIndexButton(Graphics2D graphics) {
        paintLeftArrow(graphics, (getWidth() / 2) - 36, 80);
    }

    private void paintNextFeetIndexButton(Graphics2D graphics) {
        paintRightArrow(graphics, (getWidth() / 2) + 28, 80);
    }

    private GeneralPath createRightArrow(int x, int y) {
        GeneralPath path = new GeneralPath();
        path.moveTo(x, y);
        path.lineTo(x + 8, y + 8);
        path.lineTo(x, y + 16);
        path.closePath();
        return path;
    }

    private void paintRightArrow(Graphics2D graphics, int x, int y) {
        GeneralPath path = createRightArrow(x, y);
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation, this);
        double mouseX = mouseLocation.getX();
        double mouseY = mouseLocation.getY();
        graphics.setColor(path.contains(mouseX, mouseY) ? new Color(96, 96, 255) : new Color(32, 32, 32));
        graphics.fill(path);
        graphics.setColor(new Color(128, 128, 128));
        graphics.draw(path);
    }

    private GeneralPath createLeftArrow(int x, int y) {
        GeneralPath path = new GeneralPath();
        path.moveTo(x + 8, y);
        path.lineTo(x, y + 8);
        path.lineTo(x + 8, y + 16);
        path.closePath();
        return path;
    }

    private void paintLeftArrow(Graphics2D graphics, int x, int y) {
        GeneralPath path = createLeftArrow(x, y);
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation, this);
        double mouseX = mouseLocation.getX();
        double mouseY = mouseLocation.getY();
        graphics.setColor(path.contains(mouseX, mouseY) ? new Color(96, 96, 255) : new Color(32, 32, 32));
        graphics.fill(path);
        graphics.setColor(new Color(128, 128, 128));
        graphics.draw(path);
    }

    public List<Sprite> getHairSprites() {
        return hairSprites;
    }

    public Sprite getSelectedHairSprite() {
        return hairIndex < getHairSprites().size() && hairIndex >= 0 ? getHairSprites().get(hairIndex) : null;
    }

    public void addHairSprite(Sprite sprite) {
        hairSprites.add(sprite);
    }

    public List<Sprite> getFaceSprites() {
        return faceSprites;
    }

    public Sprite getSelectedFaceSprite() {
        return faceIndex < getFaceSprites().size() && faceIndex >= 0 ? getFaceSprites().get(faceIndex) : null;
    }

    public void addFaceSprite(Sprite sprite) {
        faceSprites.add(sprite);
    }

    public List<Sprite> getTorsoSprites() {
        return torsoSprites;
    }

    public Sprite getSelectedTorsoSprite() {
        return torsoIndex < getTorsoSprites().size() && torsoIndex >= 0 ? getTorsoSprites().get(torsoIndex) : null;
    }

    public void addTorsoSprite(Sprite sprite) {
        torsoSprites.add(sprite);
    }

    public List<Sprite> getLegsSprites() {
        return legsSprites;
    }

    public Sprite getSelectedLegsSprite() {
        return legsIndex < getLegsSprites().size() && legsIndex >= 0 ? getLegsSprites().get(legsIndex): null;
    }

    public void addLegsSprite(Sprite sprite) {
        legsSprites.add(sprite);
    }

    public List<Sprite> getFeetSprites() {
        return feetSprites;
    }

    public Sprite getSelectedFeetSprite() {
        return feetIndex < getFeetSprites().size() && feetIndex >= 0 ? getFeetSprites().get(feetIndex) : null;
    }
    public void addFeetSprite(Sprite sprite) {
        feetSprites.add(sprite);
    }

    public void onTick() {
        repaint();
    }
}

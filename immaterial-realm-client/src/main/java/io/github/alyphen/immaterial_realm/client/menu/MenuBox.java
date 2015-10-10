package io.github.alyphen.immaterial_realm.client.menu;

import io.github.alyphen.immaterial_realm.client.ImmaterialRealmClient;
import io.github.alyphen.immaterial_realm.common.entity.Entity;
import io.github.alyphen.immaterial_realm.common.entity.EntityCharacter;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import static java.awt.Color.*;
import static java.util.logging.Level.SEVERE;

public class MenuBox {

    private ImmaterialRealmClient client;
    private JPanel panel;

    private MenuItem[] menuItems;

    private boolean selected;

    public MenuBox(ImmaterialRealmClient client, JPanel panel) {
        this.client = client;
        this.panel = panel;
        menuItems = new MenuItem[] {
                new MenuItem() {
                    @Override
                    public String getName() {
                        return "Character card";
                    }

                    @Override
                    public void doSelect() {
                        for (Entity entity : client.getWorldPanel().getArea().getEntities()) {
                            if (entity instanceof EntityCharacter) {
                                try {
                                    if (((EntityCharacter) entity).getCharacter().getPlayerUUID().equals(client.getPlayerManager().getPlayerUUID(client.getPlayerName()))) {
                                        client.getCharacterCreationPanel().updateFields(((EntityCharacter) entity).getCharacter());
                                        break;
                                    }
                                } catch (SQLException exception) {
                                    client.getLogger().log(SEVERE, "Failed to get player ID", exception);
                                }
                            }
                        }
                        client.showPanel("character creation");
                    }
                },
                new MenuItem() {
                    @Override
                    public String getName() {
                        return "Exit";
                    }

                    @Override
                    public void doSelect() {
                        client.getNetworkManager().closeConnections();
                        System.exit(0);
                    }
                }
        };
        panel.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseReleased(MouseEvent event) {
                if (selected) {
                    int y = 24;
                    for (MenuItem menuItem : menuItems) {
                        Rectangle itemBounds = new Rectangle(panel.getWidth() - 128, y, 128, 24);
                        if (itemBounds.contains(event.getX(), event.getY())) {
                            selected = !selected;
                            menuItem.doSelect();
                            return;
                        }
                        y += 24;
                    }
                }
                if (new Rectangle(panel.getWidth() - 64, 0, 64, 24).contains(event.getX(), event.getY())) {
                    selected = !selected;
                }
            }
        });
    }

    public void paint(Graphics graphics) {
        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        Rectangle bounds = new Rectangle(panel.getWidth() - 64, 0, 64, 24);
        graphics.setColor(bounds.contains(mousePoint.getX() - panel.getLocationOnScreen().getX(), mousePoint.getY() - panel.getLocationOnScreen().getY()) || selected ? LIGHT_GRAY : DARK_GRAY);
        graphics.fillRect(panel.getWidth() - 65, 0, 64, 24);
        graphics.setColor(WHITE);
        graphics.drawRect(panel.getWidth() - 65, 0, 64, 24);
        graphics.drawString("Menu", panel.getWidth() - 56, graphics.getFontMetrics().getMaxAdvance());
        if (selected) {
            int y = 24;
            for (MenuItem menuItem : menuItems) {
                Rectangle itemBounds = new Rectangle(panel.getWidth() - 128, y, 128, 24);
                graphics.setColor(itemBounds.contains(mousePoint.getX() - panel.getLocationOnScreen().getX(), mousePoint.getY() - panel.getLocationOnScreen().getY()) ? LIGHT_GRAY : DARK_GRAY);
                graphics.fillRect(panel.getWidth() - 129, y, 128, 24);
                graphics.setColor(WHITE);
                graphics.drawRect(panel.getWidth() - 129, y, 128, 24);
                graphics.drawString(menuItem.getName(), panel.getWidth() - 124, y + graphics.getFontMetrics().getMaxAdvance());
                y += 24;
            }
        }
    }

}

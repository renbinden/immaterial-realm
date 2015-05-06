package io.github.alyphen.immaterial_realm.common.hud;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.awt.Color.WHITE;

public class HUD {

    private Map<String, HUDComponent> components;

    public HUD() {
        components = new ConcurrentHashMap<>();
    }
    
    public void paint(ScriptEngineManager scriptEngineManager, Graphics graphics, int screenWidth, int screenHeight) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        for (HUDComponent component : components.values()) {
            try {
                component.paint(scriptEngineManager, graphics, screenWidth, screenHeight);
            } catch (ScriptException exception) {
                errorMessageBuilder.append("HUD Component ").append(component.getName()).append(" contains script errors\n");
            }
        }
        if (errorMessageBuilder.length() != 0) {
            graphics.setColor(new Color(128, 128, 128, 128));
            String message = errorMessageBuilder.toString();
            String[] lines = message.split("\n");
            FontMetrics fontMetrics = graphics.getFontMetrics();
            graphics.fillRoundRect(
                    (screenWidth - fontMetrics.stringWidth(message)) / 2,
                    256,
                    fontMetrics.stringWidth(message),
                    (fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent() + fontMetrics.getLeading()) * lines.length,
                    8,
                    8
            );
            graphics.setColor(WHITE);
            int y = 256 + graphics.getFontMetrics().getMaxAscent();
            for (String line : lines) {
                graphics.drawString(
                        message,
                        (screenWidth / 2) - fontMetrics.stringWidth(line),
                        y
                );
                y += fontMetrics.getMaxAscent() + fontMetrics.getLeading() + fontMetrics.getMaxDescent();
            }
        }
    }

    public void addComponent(HUDComponent component) {
        components.put(component.getName(), component);
    }

    public HUDComponent getComponent(String name) {
        return components.get(name);
    }

    public Collection<HUDComponent> getComponents() {
        return components.values();
    }

}

package io.github.alyphen.amethyst.client;

import io.github.alyphen.amethyst.common.control.Control;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.alyphen.amethyst.common.control.Control.*;
import static java.awt.event.KeyEvent.*;

public class InputManager extends KeyAdapter {

    private AmethystClient client;
    private Map<Integer, Control> controls;
    private Set<Control> pressedControls;

    public InputManager(AmethystClient client) {
        this.client = client;
        controls = new ConcurrentHashMap<>();
        pressedControls = Collections.synchronizedSet(EnumSet.noneOf(Control.class));
        map((int) 'W', MOVE_UP);
        map((int) 'S', MOVE_DOWN);
        map((int) 'A', MOVE_LEFT);
        map((int) 'D', MOVE_RIGHT);
        map(VK_UP, MOVE_UP);
        map(VK_DOWN, MOVE_DOWN);
        map(VK_LEFT, MOVE_LEFT);
        map(VK_RIGHT, MOVE_RIGHT);
    }

    public void map(int keyCode, Control control) {
        controls.put(keyCode, control);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (client.getWorldPanel().getChatBox().isActive()) return;
        Control control = controls.get(event.getKeyCode());
        if (control != null) {
            if (!pressedControls.contains(control)) {
                pressedControls.add(control);
                client.onControlPressed(control);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (client.getWorldPanel().getChatBox().isActive()) return;
        Control control = controls.get(event.getKeyCode());
        if (control != null) {
            if (pressedControls.contains(control)) {
                pressedControls.remove(control);
                client.onControlReleased(control);
            }
        }
    }

}

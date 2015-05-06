package io.github.alyphen.immaterial_realm.server.hud;

import io.github.alyphen.immaterial_realm.common.hud.HUD;
import io.github.alyphen.immaterial_realm.common.hud.HUDComponent;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.hud.PacketSetHUDComponentVariable;
import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;
import io.github.alyphen.immaterial_realm.server.event.hud.HUDCreateEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HUDManager {

    private ImmaterialRealmServer server;

    private Map<Long, HUD> playerHUDs;

    public HUDManager(ImmaterialRealmServer server) {
        this.server = server;
        playerHUDs = new ConcurrentHashMap<>();
    }

    public HUD getPlayerHUD(Player player) {
        if (!playerHUDs.containsKey(player.getId())) {
            HUD hud = new HUD();
            HUDCreateEvent event = new HUDCreateEvent(hud, player);
            server.getEventManager().onEvent(event);
            playerHUDs.put(event.getPlayer().getId(), event.getHUD());
        }
        return playerHUDs.get(player.getId());
    }

    public void setComponentVariable(Player player, String componentName, String variable, Object value) {
        HUDComponent component = getPlayerHUD(player).getComponent(componentName);
        if (component != null) {
            component.setVariable(variable, value);
            server.getNetworkManager().sendPacket(player, new PacketSetHUDComponentVariable(componentName, variable, value));
        }
    }

}

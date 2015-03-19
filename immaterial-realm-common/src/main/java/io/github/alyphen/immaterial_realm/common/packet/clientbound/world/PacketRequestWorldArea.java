package io.github.alyphen.immaterial_realm.common.packet.clientbound.world;

public class PacketRequestWorldArea {

    private String world;
    private String area;

    public PacketRequestWorldArea(String world, String area) {
        this.world = world;
        this.area = area;
    }

    public String getWorld() {
        return world;
    }

    public String getArea() {
        return area;
    }

}

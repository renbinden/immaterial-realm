package io.github.alyphen.amethyst.common.packet.world;

public class PacketRequestObjects {

    private String world;
    private String area;

    public PacketRequestObjects(String world, String area) {
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

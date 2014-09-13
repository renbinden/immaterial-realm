package io.github.lucariatias.amethyst.common;

import java.io.Serializable;

public class PacketTest implements Serializable {

    private String message;

    public PacketTest(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PacketTest [ " + message + " ]";
    }

}

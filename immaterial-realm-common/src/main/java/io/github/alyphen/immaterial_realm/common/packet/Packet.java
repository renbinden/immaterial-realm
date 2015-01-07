package io.github.alyphen.immaterial_realm.common.packet;

import io.netty.channel.Channel;

import java.io.Serializable;

public abstract class Packet implements Serializable {

    public void send(Channel channel) {
        channel.writeAndFlush(this);
    }

}

package io.github.lucariatias.amethyst.client;

import io.github.lucariatias.amethyst.common.packet.PacketPing;
import io.github.lucariatias.amethyst.common.packet.login.PacketGameState;
import io.github.lucariatias.amethyst.common.packet.login.PacketLoginStatus;
import io.github.lucariatias.amethyst.common.packet.login.PacketPublicKey;
import io.github.lucariatias.amethyst.common.packet.login.PacketVersion;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

public class AmethystClientHandler extends ChannelHandlerAdapter {

    private AmethystClient client;
    private Timer timer;

    public AmethystClientHandler(AmethystClient client) {
        this.client = client;
        timer = new HashedWheelTimer();
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        client.showPanel("login");
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws GeneralSecurityException, UnsupportedEncodingException {
        System.out.println(msg.toString());
        if (msg instanceof PacketVersion) {
            context.writeAndFlush(new PacketPublicKey(client.getEncryptionManager().getKeyPair().getPublic().getEncoded()));
        } else if (msg instanceof PacketPublicKey) {
            PacketPublicKey packet = (PacketPublicKey) msg;
            client.getNetworkManager().setServerPublicKey(packet.getEncodedPublicKey());
        } else if (msg instanceof PacketLoginStatus) {
            client.showPanel("world");
        } else if (msg instanceof PacketGameState) {
            PacketGameState packet = (PacketGameState) msg;
            JOptionPane.showMessageDialog(null, packet.getMessage());
            context.writeAndFlush(new PacketPing());
        } else if (msg instanceof PacketPing) {
            timer.newTimeout(timeout -> context.writeAndFlush(new PacketPing()), 20, TimeUnit.SECONDS);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}

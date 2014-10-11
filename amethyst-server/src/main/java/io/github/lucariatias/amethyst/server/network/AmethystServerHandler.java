package io.github.lucariatias.amethyst.server.network;

import io.github.lucariatias.amethyst.common.packet.PacketPing;
import io.github.lucariatias.amethyst.common.packet.login.PacketLoginDetails;
import io.github.lucariatias.amethyst.common.packet.login.PacketLoginStatus;
import io.github.lucariatias.amethyst.common.packet.login.PacketPublicKey;
import io.github.lucariatias.amethyst.common.packet.login.PacketVersion;
import io.github.lucariatias.amethyst.common.player.Player;
import io.github.lucariatias.amethyst.server.AmethystServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AmethystServerHandler extends ChannelHandlerAdapter {

    private AmethystServer server;

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private Map<Channel, byte[]> encodedPublicKeys = new HashMap<>();
    private AttributeKey<Player> playerAttributeKey = AttributeKey.valueOf("player");

    public AmethystServerHandler(AmethystServer server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        channels.add(context.channel());
        context.writeAndFlush(new PacketVersion(getClass().getPackage().getImplementationVersion()));
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws GeneralSecurityException, SQLException, UnsupportedEncodingException {
        System.out.println(msg.toString());
        if (msg instanceof PacketPublicKey) {
            PacketPublicKey packet = (PacketPublicKey) msg;
            encodedPublicKeys.put(context.channel(), packet.getEncodedPublicKey());
            context.writeAndFlush(new PacketPublicKey(server.getEncryptionManager().getKeyPair().getPublic().getEncoded()));
        } else if (msg instanceof PacketLoginDetails) {
            PacketLoginDetails packet = (PacketLoginDetails) msg;
            if (packet.isSignUp()) {
                server.getPlayerManager().addPlayer(packet.getPlayerName(), server.getEncryptionManager().decrypt(packet.getEncryptedPasswordHash()));
            }
            Player player = server.getPlayerManager().getPlayer(packet.getPlayerName());
            if (player != null) {
                if (server.getPlayerManager().checkLogin(player, server.getEncryptionManager().decrypt(packet.getEncryptedPasswordHash()))) {
                    context.attr(playerAttributeKey).set(player);
                    context.writeAndFlush(new PacketLoginStatus(true));
                } else {
                    context.writeAndFlush(new PacketLoginStatus(false));
                }
            } else {
                context.writeAndFlush(new PacketLoginStatus(false));
            }
        } else if (msg instanceof PacketPing) {
            context.writeAndFlush(new PacketPing());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}

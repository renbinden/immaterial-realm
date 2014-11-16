package io.github.alyphen.amethyst.server.network;

import io.github.alyphen.amethyst.common.packet.Packet;
import io.github.alyphen.amethyst.server.AmethystServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NetworkManager {

    private AmethystServer server;
    private int port;
    private AmethystServerHandler handler;

    public NetworkManager(AmethystServer server, int port) {
        this.server = server;
        this.port = port;
    }

    public void start() {
        start(port);
    }

    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            handler = new AmethystServerHandler(server);
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    handler
                            );
                        }
                    });
            Channel channel = bootstrap.bind(port).sync().channel();
            server.run();
            channel.closeFuture().sync();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void broadcastPacket(Packet packet) {
        handler.broadcastPacket(packet);
    }

}

package io.github.lucariatias.amethyst.client.network;

import io.github.lucariatias.amethyst.client.AmethystClient;
import io.github.lucariatias.amethyst.common.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NetworkManager {

    private final AmethystClient client;
    private Channel channel;

    private String serverAddress;
    private int serverPort;
    private byte[] serverPublicKey;

    public NetworkManager(AmethystClient client) {
        this.client = client;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void connect() {
        connect(getServerAddress(), getServerPort());
    }

    public void connect(String host, int port) {
        setServerAddress(host);
        setServerPort(port);
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new AmethystClientHandler(client)
                            );
                        }
                    });
            channel = bootstrap.connect(host, port).sync().channel();
            client.run();
            channel.closeFuture().sync();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendPacket(Packet packet) {
        packet.send(channel);
    }

    public void setServerPublicKey(byte[] serverPublicKey) {
        this.serverPublicKey = serverPublicKey;
    }

    public byte[] getServerPublicKey() {
        return serverPublicKey;
    }
}

package io.github.lucariatias.amethyst.client;

import io.github.lucariatias.amethyst.common.PacketTest;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class AmethystClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext context) {
        context.writeAndFlush(new PacketTest("Test"));
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        context.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}

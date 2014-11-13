package io.github.lucariatias.amethyst.server.network;

import io.github.lucariatias.amethyst.common.character.Character;
import io.github.lucariatias.amethyst.common.packet.PacketPing;
import io.github.lucariatias.amethyst.common.packet.character.PacketRequestCharacterSprites;
import io.github.lucariatias.amethyst.common.packet.character.PacketSendCharacterSprites;
import io.github.lucariatias.amethyst.common.packet.login.PacketLoginDetails;
import io.github.lucariatias.amethyst.common.packet.login.PacketLoginStatus;
import io.github.lucariatias.amethyst.common.packet.login.PacketPublicKey;
import io.github.lucariatias.amethyst.common.packet.login.PacketVersion;
import io.github.lucariatias.amethyst.common.packet.object.PacketRequestObjectTypes;
import io.github.lucariatias.amethyst.common.packet.tile.PacketRequestTileSheets;
import io.github.lucariatias.amethyst.common.packet.tile.PacketSendTileSheet;
import io.github.lucariatias.amethyst.common.packet.world.*;
import io.github.lucariatias.amethyst.common.player.Player;
import io.github.lucariatias.amethyst.common.sprite.Sprite;
import io.github.lucariatias.amethyst.common.tile.TileSheet;
import io.github.lucariatias.amethyst.common.world.World;
import io.github.lucariatias.amethyst.server.AmethystServer;
import io.github.lucariatias.amethyst.server.character.CharacterManager;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

import static io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class AmethystServerHandler extends ChannelHandlerAdapter {

    private AmethystServer server;

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private AttributeKey<Player> playerAttributeKey = AttributeKey.valueOf("player");
    private AttributeKey<byte[]> publicKeyAttributeKey = AttributeKey.valueOf("publicKey");

    public AmethystServerHandler(AmethystServer server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
        ctx.writeAndFlush(new PacketVersion(getClass().getPackage().getImplementationVersion()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws GeneralSecurityException, SQLException, UnsupportedEncodingException {
        System.out.println(msg.toString());
        if (msg instanceof PacketPublicKey) {
            PacketPublicKey packet = (PacketPublicKey) msg;
            ctx.channel().attr(publicKeyAttributeKey).set(packet.getEncodedPublicKey());
            ctx.writeAndFlush(new PacketPublicKey(server.getEncryptionManager().getKeyPair().getPublic().getEncoded()));
        } else if (msg instanceof PacketLoginDetails) {
            PacketLoginDetails packet = (PacketLoginDetails) msg;
            if (packet.isSignUp()) {
                server.getPlayerManager().addPlayer(packet.getPlayerName(), server.getEncryptionManager().decrypt(packet.getEncryptedPasswordHash()));
            }
            Player player = server.getPlayerManager().getPlayer(packet.getPlayerName());
            if (player != null) {
                if (server.getPlayerManager().checkLogin(player, server.getEncryptionManager().decrypt(packet.getEncryptedPasswordHash()))) {
                    ctx.attr(playerAttributeKey).set(player);
                    ctx.writeAndFlush(new PacketLoginStatus(true));
                } else {
                    ctx.writeAndFlush(new PacketLoginStatus(false));
                }
            } else {
                ctx.writeAndFlush(new PacketLoginStatus(false));
            }
        } else if (msg instanceof PacketPing) {
            ctx.writeAndFlush(new PacketPing());
        } else if (msg instanceof PacketRequestCharacterSprites) {
            PacketRequestCharacterSprites packet = (PacketRequestCharacterSprites) msg;
            CharacterManager characterManager = server.getCharacterManager();
            Character character = characterManager.getCharacter(packet.getCharacterId());
            Sprite walkUpSprite = characterManager.getWalkUpSprite(character);
            Sprite walkDownSprite = characterManager.getWalkDownSprite(character);
            Sprite walkLeftSprite = characterManager.getWalkLeftSprite(character);
            Sprite walkRightSprite = characterManager.getWalkRightSprite(character);
            ctx.writeAndFlush(new PacketSendCharacterSprites(
                    character.getId(),
                    walkUpSprite,
                    walkDownSprite,
                    walkLeftSprite,
                    walkRightSprite
            ));
            walkUpSprite.flush();
            walkDownSprite.flush();
            walkLeftSprite.flush();
            walkRightSprite.flush();
        } else if (msg instanceof PacketRequestTileSheets) {
            for (TileSheet tileSheet : TileSheet.getTileSheets()) {
                ctx.writeAndFlush(new PacketSendTileSheet(tileSheet.getName(), tileSheet.getSheet(), tileSheet.getTileWidth(), tileSheet.getTileHeight()));
            }
        } else if (msg instanceof PacketRequestObjectTypes) {

        } else if (msg instanceof PacketRequestWorlds) {
            for (World world : World.getWorlds()) {
                ctx.writeAndFlush(new PacketSendWorld(world.getName()));
            }
        } else if (msg instanceof PacketRequestCurrentWorldArea) {
            ctx.writeAndFlush(new PacketSendArea(World.getWorld("default").getArea("default")));
            ctx.writeAndFlush(new PacketShowArea("default"));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}

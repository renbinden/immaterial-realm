package io.github.alyphen.amethyst.server.network;

import io.github.alyphen.amethyst.common.character.Character;
import io.github.alyphen.amethyst.common.entity.EntityCharacter;
import io.github.alyphen.amethyst.common.entity.EntityFactory;
import io.github.alyphen.amethyst.common.object.WorldObjectFactory;
import io.github.alyphen.amethyst.common.object.WorldObjectInitializer;
import io.github.alyphen.amethyst.common.packet.PacketPing;
import io.github.alyphen.amethyst.common.packet.character.PacketCharacterSpawn;
import io.github.alyphen.amethyst.common.packet.entity.PacketEntitySpawn;
import io.github.alyphen.amethyst.common.packet.login.PacketLoginDetails;
import io.github.alyphen.amethyst.common.packet.login.PacketLoginStatus;
import io.github.alyphen.amethyst.common.packet.login.PacketPublicKey;
import io.github.alyphen.amethyst.common.packet.login.PacketVersion;
import io.github.alyphen.amethyst.common.packet.object.PacketCreateObject;
import io.github.alyphen.amethyst.common.packet.object.PacketRequestObjectTypes;
import io.github.alyphen.amethyst.common.packet.object.PacketSendObjectType;
import io.github.alyphen.amethyst.common.packet.player.PacketRequestPlayers;
import io.github.alyphen.amethyst.common.packet.player.PacketSendPlayers;
import io.github.alyphen.amethyst.common.packet.tile.PacketRequestTileSheets;
import io.github.alyphen.amethyst.common.packet.tile.PacketSendTileSheet;
import io.github.alyphen.amethyst.common.packet.world.*;
import io.github.alyphen.amethyst.common.player.Player;
import io.github.alyphen.amethyst.common.sprite.Sprite;
import io.github.alyphen.amethyst.common.tile.TileSheet;
import io.github.alyphen.amethyst.common.world.World;
import io.github.alyphen.amethyst.common.world.WorldArea;
import io.github.alyphen.amethyst.server.AmethystServer;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.stream.Collectors;

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
                    ctx.channel().attr(playerAttributeKey).set(player);
                    ctx.writeAndFlush(new PacketLoginStatus(true));
                } else {
                    ctx.writeAndFlush(new PacketLoginStatus(false));
                }
            } else {
                ctx.writeAndFlush(new PacketLoginStatus(false));
            }
        } else if (msg instanceof PacketPing) {
            ctx.writeAndFlush(new PacketPing());
        } else if (msg instanceof PacketRequestPlayers) {
            ctx.writeAndFlush(new PacketSendPlayers(channels.stream().filter(channel -> channel.attr(playerAttributeKey).get() != null).map(channel -> channel.attr(playerAttributeKey).get()).collect(Collectors.toSet())));
        } else if (msg instanceof PacketRequestTileSheets) {
            for (TileSheet tileSheet : TileSheet.getTileSheets()) {
                ctx.writeAndFlush(new PacketSendTileSheet(tileSheet.getName(), tileSheet.getSheet(), tileSheet.getTileWidth(), tileSheet.getTileHeight()));
            }
        } else if (msg instanceof PacketRequestObjectTypes) {
            for (WorldObjectInitializer initializer : WorldObjectFactory.getObjectInitializers()) {
                ctx.writeAndFlush(new PacketSendObjectType(initializer.getObjectName(), initializer.getObjectSprite(), initializer.getObjectBounds()));
            }
        } else if (msg instanceof PacketRequestWorlds) {
            for (World world : World.getWorlds()) {
                ctx.writeAndFlush(new PacketSendWorld(world.getName()));
            }
        } else if (msg instanceof PacketRequestCurrentWorldArea) {
            WorldArea area = World.getWorld("default").getArea("default");
            ctx.writeAndFlush(new PacketSendArea(area));
            ctx.writeAndFlush(new PacketShowArea("default"));
            area.getObjects().stream().forEach(object -> ctx.writeAndFlush(new PacketCreateObject(object.getType(), area.getWorld().getName(), area.getName(), object.getX(), object.getY())));
            area.getEntities().stream().filter(entity -> !(entity instanceof EntityCharacter)).forEach(entity -> ctx.writeAndFlush(new PacketEntitySpawn(entity.getId(), entity.getClass(), area.getName(), entity.getX(), entity.getY())));
            area.getEntities().stream().filter(entity -> entity instanceof EntityCharacter).forEach(entity -> {
                EntityCharacter entityCharacter = (EntityCharacter) entity;
                Character character = entityCharacter.getCharacter();
                Sprite walkUpSprite = server.getCharacterManager().getWalkUpSprite(character);
                Sprite walkDownSprite = server.getCharacterManager().getWalkDownSprite(character);
                Sprite walkLeftSprite = server.getCharacterManager().getWalkLeftSprite(character);
                Sprite walkRightSprite = server.getCharacterManager().getWalkRightSprite(character);
                try {
                    ctx.writeAndFlush(new PacketCharacterSpawn(character, walkUpSprite, walkDownSprite, walkLeftSprite, walkRightSprite));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                walkUpSprite.flush();
                walkDownSprite.flush();
                walkLeftSprite.flush();
                walkRightSprite.flush();
            });
            Player player = ctx.channel().attr(playerAttributeKey).get();
            Character character = server.getCharacterManager().getCharacter(player);
            if (character == null) {
                character = new Character(player.getId(), -1);
                server.getCharacterManager().addCharacter(character);
                character = server.getCharacterManager().getCharacter(player);
            }
            Sprite walkUpSprite = server.getCharacterManager().getWalkUpSprite(character);
            Sprite walkDownSprite = server.getCharacterManager().getWalkDownSprite(character);
            Sprite walkLeftSprite = server.getCharacterManager().getWalkLeftSprite(character);
            Sprite walkRightSprite = server.getCharacterManager().getWalkRightSprite(character);
            EntityCharacter entity = EntityFactory.spawn(EntityCharacter.class, area, 0, 0);
            entity.setCharacter(character);
            try {
                channels.writeAndFlush(new PacketCharacterSpawn(character, walkUpSprite, walkDownSprite, walkLeftSprite, walkRightSprite));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            walkUpSprite.flush();
            walkDownSprite.flush();
            walkLeftSprite.flush();
            walkRightSprite.flush();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}

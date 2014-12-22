package io.github.alyphen.amethyst.server.network;

import io.github.alyphen.amethyst.common.character.Character;
import io.github.alyphen.amethyst.common.chat.ChatChannel;
import io.github.alyphen.amethyst.common.entity.Entity;
import io.github.alyphen.amethyst.common.entity.EntityCharacter;
import io.github.alyphen.amethyst.common.entity.EntityFactory;
import io.github.alyphen.amethyst.common.object.WorldObjectFactory;
import io.github.alyphen.amethyst.common.object.WorldObjectInitializer;
import io.github.alyphen.amethyst.common.packet.Packet;
import io.github.alyphen.amethyst.common.packet.clientbound.character.PacketCharacterSpawn;
import io.github.alyphen.amethyst.common.packet.clientbound.chat.PacketClientboundGlobalChatMessage;
import io.github.alyphen.amethyst.common.packet.clientbound.chat.PacketClientboundLocalChatMessage;
import io.github.alyphen.amethyst.common.packet.clientbound.chat.PacketSendChannel;
import io.github.alyphen.amethyst.common.packet.clientbound.chat.PacketSetChannel;
import io.github.alyphen.amethyst.common.packet.clientbound.login.PacketClientboundPublicKey;
import io.github.alyphen.amethyst.common.packet.clientbound.player.PacketPlayerJoin;
import io.github.alyphen.amethyst.common.packet.clientbound.player.PacketPlayerLeave;
import io.github.alyphen.amethyst.common.packet.serverbound.chat.PacketRequestChannels;
import io.github.alyphen.amethyst.common.packet.serverbound.chat.PacketServerboundGlobalChatMessage;
import io.github.alyphen.amethyst.common.packet.serverbound.chat.PacketServerboundLocalChatMessage;
import io.github.alyphen.amethyst.common.packet.serverbound.control.PacketControlPressed;
import io.github.alyphen.amethyst.common.packet.serverbound.control.PacketControlReleased;
import io.github.alyphen.amethyst.common.packet.clientbound.entity.PacketEntitySpawn;
import io.github.alyphen.amethyst.common.packet.serverbound.login.PacketLoginDetails;
import io.github.alyphen.amethyst.common.packet.clientbound.login.PacketLoginStatus;
import io.github.alyphen.amethyst.common.packet.serverbound.login.PacketServerboundPublicKey;
import io.github.alyphen.amethyst.common.packet.clientbound.login.PacketVersion;
import io.github.alyphen.amethyst.common.packet.clientbound.object.PacketCreateObject;
import io.github.alyphen.amethyst.common.packet.serverbound.object.PacketRequestObjectTypes;
import io.github.alyphen.amethyst.common.packet.clientbound.object.PacketSendObjectType;
import io.github.alyphen.amethyst.common.packet.serverbound.player.PacketRequestPlayers;
import io.github.alyphen.amethyst.common.packet.clientbound.player.PacketSendPlayers;
import io.github.alyphen.amethyst.common.packet.serverbound.tile.PacketRequestTileSheets;
import io.github.alyphen.amethyst.common.packet.clientbound.tile.PacketSendTileSheet;
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
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import static io.github.alyphen.amethyst.common.world.Direction.*;
import static io.netty.channel.ChannelHandler.Sharable;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Sharable
public class AmethystServerHandler extends ChannelHandlerAdapter {

    private AmethystServer server;

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final AttributeKey<Player> PLAYER = AttributeKey.valueOf("player");
    private static final AttributeKey<byte[]> PUBLIC_KEY = AttributeKey.valueOf("publicKey");

    public AmethystServerHandler(AmethystServer server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
        ctx.writeAndFlush(new PacketVersion(getClass().getPackage().getImplementationVersion()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channels.stream().filter(channel -> channel != ctx.channel()).forEach(channel -> {
            Player player = ctx.channel().attr(PLAYER).get();
            channel.writeAndFlush(new PacketPlayerLeave(player.getId()));
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws GeneralSecurityException, SQLException, UnsupportedEncodingException {
        System.out.println(msg.toString());
        if (msg instanceof PacketServerboundPublicKey) {
            PacketServerboundPublicKey packet = (PacketServerboundPublicKey) msg;
            ctx.channel().attr(PUBLIC_KEY).set(packet.getEncodedPublicKey());
            ctx.writeAndFlush(new PacketClientboundPublicKey(server.getEncryptionManager().getKeyPair().getPublic().getEncoded()));
        } else if (msg instanceof PacketLoginDetails) {
            PacketLoginDetails packet = (PacketLoginDetails) msg;
            if (packet.isSignUp()) {
                server.getPlayerManager().addPlayer(packet.getPlayerName(), server.getEncryptionManager().decrypt(packet.getEncryptedPassword()));
            }
            Player player = server.getPlayerManager().getPlayer(packet.getPlayerName());
            if (player != null) {
                if (server.getPlayerManager().checkLogin(player, sha256Hex(server.getEncryptionManager().decrypt(packet.getEncryptedPassword()) + server.getPlayerManager().getSalt(player)))) {
                    ctx.channel().attr(PLAYER).set(player);
                    ctx.writeAndFlush(new PacketLoginStatus(true));
                    channels.stream().filter(channel -> channel != ctx.channel()).forEach(channel -> channel.writeAndFlush(new PacketPlayerJoin(player.getId(), player.getName())));
                } else {
                    ctx.writeAndFlush(new PacketLoginStatus(false));
                }
            } else {
                ctx.writeAndFlush(new PacketLoginStatus(false));
            }
        } else if (msg instanceof PacketRequestPlayers) {
            ctx.writeAndFlush(new PacketSendPlayers(channels.stream().filter(channel -> channel.attr(PLAYER).get() != null).map(channel -> channel.attr(PLAYER).get()).collect(Collectors.toSet())));
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
            Player player = ctx.channel().attr(PLAYER).get();
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
        } else if (msg instanceof PacketControlPressed) {
            PacketControlPressed packet = (PacketControlPressed) msg;
            Character character = server.getCharacterManager().getCharacter(ctx.channel().attr(PLAYER).get());
            EntityCharacter characterEntity = null;
            worldLoop: for (World world : World.getWorlds()) {
                for (WorldArea area : world.getAreas()) {
                    for (Entity entity : area.getEntities()) {
                        if (entity instanceof EntityCharacter) {
                            if (((EntityCharacter) entity).getCharacter().getId() == character.getId()) {
                                characterEntity = (EntityCharacter) entity;
                                break worldLoop;
                            }
                        }
                    }
                }
            }
            if (characterEntity != null) {
                switch (packet.getControl()) {
                    case MOVE_UP:
                        characterEntity.setDirectionFacing(UP);
                        characterEntity.setVerticalSpeed(characterEntity.getVerticalSpeed() - 2);
                        break;
                    case MOVE_DOWN:
                        characterEntity.setDirectionFacing(DOWN);
                        characterEntity.setVerticalSpeed(characterEntity.getVerticalSpeed() + 2);
                        break;
                    case MOVE_LEFT:
                        characterEntity.setDirectionFacing(LEFT);
                        characterEntity.setHorizontalSpeed(characterEntity.getHorizontalSpeed() - 2);
                        break;
                    case MOVE_RIGHT:
                        characterEntity.setDirectionFacing(RIGHT);
                        characterEntity.setHorizontalSpeed(characterEntity.getHorizontalSpeed() + 2);
                        break;
                }
            }
        } else if (msg instanceof PacketControlReleased) {
            PacketControlReleased packet = (PacketControlReleased) msg;
            Character character = server.getCharacterManager().getCharacter(ctx.channel().attr(PLAYER).get());
            EntityCharacter characterEntity = null;
            worldLoop: for (World world : World.getWorlds()) {
                for (WorldArea area : world.getAreas()) {
                    for (Entity entity : area.getEntities()) {
                        if (entity instanceof EntityCharacter) {
                            if (((EntityCharacter) entity).getCharacter().getId() == character.getId()) {
                                characterEntity = (EntityCharacter) entity;
                                break worldLoop;
                            }
                        }
                    }
                }
            }
            if (characterEntity != null) {
                switch (packet.getControl()) {
                    case MOVE_UP:
                        characterEntity.setVerticalSpeed(characterEntity.getVerticalSpeed() + 2);
                        break;
                    case MOVE_DOWN:
                        characterEntity.setVerticalSpeed(characterEntity.getVerticalSpeed() - 2);
                        break;
                    case MOVE_LEFT:
                        characterEntity.setHorizontalSpeed(characterEntity.getHorizontalSpeed() + 2);
                        break;
                    case MOVE_RIGHT:
                        characterEntity.setHorizontalSpeed(characterEntity.getHorizontalSpeed() - 2);
                        break;
                }
            }
        } else if (msg instanceof PacketServerboundLocalChatMessage) {
            PacketServerboundLocalChatMessage packet = (PacketServerboundLocalChatMessage) msg;
            ChatChannel chatChannel = server.getChatManager().getChannel(packet.getChannel());
            Character character = server.getCharacterManager().getCharacter(ctx.channel().attr(PLAYER).get());
            channels.stream().filter(channel -> {
                Player player = channel.attr(PLAYER).get();
                EntityCharacter characterEntity = null;
                for (Entity entity : World.getWorld("default").getArea(character.getAreaName()).getEntities()) {
                    if (entity instanceof EntityCharacter) {
                        if (((EntityCharacter) entity).getCharacter().getId() == character.getId()) {
                            characterEntity = (EntityCharacter) entity;
                        }
                    }
                }
                if (characterEntity != null) {
                    for (Entity entity : World.getWorld("default").getArea(character.getAreaName()).getEntities()) {
                        if (entity instanceof EntityCharacter) {
                            EntityCharacter otherCharacterEntity = (EntityCharacter) entity;
                            if (otherCharacterEntity.getCharacter().getPlayerId() == player.getId() && (otherCharacterEntity.distanceSquared(characterEntity) <= chatChannel.getRadius() * chatChannel.getRadius())) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }).forEach(channel -> channel.writeAndFlush(new PacketClientboundLocalChatMessage(character, packet.getChannel(), packet.getMessage())));
        } else if (msg instanceof PacketRequestChannels) {
            for (ChatChannel channel : server.getChatManager().getChannels()) {
                ctx.writeAndFlush(new PacketSendChannel(channel));
            }
            ctx.writeAndFlush(new PacketSetChannel(server.getChatManager().getDefaultChannel().getName()));
        } else if (msg instanceof PacketServerboundGlobalChatMessage) {
            PacketServerboundGlobalChatMessage packet = (PacketServerboundGlobalChatMessage) msg;
            channels.writeAndFlush(new PacketClientboundGlobalChatMessage(ctx.channel().attr(PLAYER).get(), packet.getChannel(), packet.getMessage()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    public void broadcastPacket(Packet packet) {
        channels.writeAndFlush(packet);
    }
}

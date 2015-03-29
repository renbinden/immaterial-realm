package io.github.alyphen.immaterial_realm.server.network;

import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannel;
import io.github.alyphen.immaterial_realm.common.entity.Entity;
import io.github.alyphen.immaterial_realm.common.entity.EntityCharacter;
import io.github.alyphen.immaterial_realm.common.entity.EntityFactory;
import io.github.alyphen.immaterial_realm.common.object.WorldObjectFactory;
import io.github.alyphen.immaterial_realm.common.object.WorldObjectInitializer;
import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.character.*;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.chat.PacketClientboundGlobalChatMessage;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.chat.PacketClientboundLocalChatMessage;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.chat.PacketSendChannel;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.chat.PacketSetChannel;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.entity.PacketEntitySpawn;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.login.PacketClientboundPublicKey;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.login.PacketLoginStatus;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.login.PacketVersion;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.object.PacketCreateObject;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.object.PacketSendObjectType;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.player.PacketPlayerJoin;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.player.PacketPlayerLeave;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.player.PacketSendPlayers;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.sprite.*;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.tile.PacketSendTileSheet;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.world.*;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.character.PacketRequestCharacterSprites;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.character.PacketRequestGenders;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.character.PacketRequestRaces;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.character.PacketSaveCharacter;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.chat.PacketRequestChannels;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.chat.PacketServerboundGlobalChatMessage;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.chat.PacketServerboundLocalChatMessage;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.control.PacketControlPressed;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.control.PacketControlReleased;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.login.PacketLoginDetails;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.login.PacketServerboundPublicKey;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.object.PacketRequestObjectTypes;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.player.PacketRequestPlayers;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.tile.PacketRequestTileSheets;
import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;
import io.github.alyphen.immaterial_realm.common.tile.TileSheet;
import io.github.alyphen.immaterial_realm.common.world.World;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;
import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.alyphen.immaterial_realm.common.world.Direction.*;
import static io.netty.channel.ChannelHandler.Sharable;
import static java.util.logging.Level.SEVERE;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Sharable
public class ImmaterialRealmServerHandler extends ChannelHandlerAdapter {

    private ImmaterialRealmServer server;

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final AttributeKey<Player> PLAYER = AttributeKey.valueOf("player");
    private static final AttributeKey<byte[]> PUBLIC_KEY = AttributeKey.valueOf("publicKey");

    public ImmaterialRealmServerHandler(ImmaterialRealmServer server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
        ctx.writeAndFlush(new PacketVersion(getClass().getPackage().getImplementationVersion()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Player player = ctx.channel().attr(PLAYER).get();
        Character character = server.getCharacterManager().getCharacter(player);
        for (World world : World.getWorlds()) {
            for (WorldArea area : world.getAreas()) {
                Iterator<Entity> entityIterator = area.getEntities().iterator();
                while (entityIterator.hasNext()) {
                    Entity entity = entityIterator.next();
                    if (entity instanceof EntityCharacter) {
                        EntityCharacter characterEntity = (EntityCharacter) entity;
                        if (characterEntity.getCharacter().getPlayerId() == player.getId()) {
                            character.setArea(area);
                            character.setX(characterEntity.getX());
                            character.setY(characterEntity.getY());
                            try {
                                server.getCharacterManager().updateCharacter(character);
                            } catch (SQLException exception) {
                                server.getLogger().log(SEVERE, "Failed to update character", exception);
                            }
                            entityIterator.remove();

                        }
                    }
                }
            }
        }
        channels.stream().filter(channel -> channel != ctx.channel()).forEach(channel -> channel.writeAndFlush(new PacketPlayerLeave(player.getId())));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws GeneralSecurityException, SQLException, UnsupportedEncodingException {
        printPacketDetails(msg);
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
                ctx.writeAndFlush(new PacketSendTileSheet(tileSheet.getName(), tileSheet.getSheetImage(), tileSheet.getTileWidth(), tileSheet.getTileHeight()));
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
                    ctx.writeAndFlush(new PacketCharacterSpawn(character, entity.getId(), walkUpSprite, walkDownSprite, walkLeftSprite, walkRightSprite));
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
            EntityCharacter entity = EntityFactory.spawn(EntityCharacter.class, area, character.getX(), character.getY());
            if (entity != null) {
                entity.setCharacter(character);
                try {
                    channels.writeAndFlush(new PacketCharacterSpawn(character, entity.getId(), walkUpSprite, walkDownSprite, walkLeftSprite, walkRightSprite));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
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
        } else if (msg instanceof PacketSaveCharacter) {
            PacketSaveCharacter packet = (PacketSaveCharacter) msg;
            if (packet.isNewCharacter()) {
                Player player = ctx.channel().attr(PLAYER).get();
                for (Character character : server.getCharacterManager().getCharacters(player)) {
                    character.setActive(false);
                    server.getCharacterManager().updateCharacter(character);
                }
                server.getCharacterManager().addCharacter(
                        new Character(
                                player,
                                0,
                                packet.getName(),
                                packet.getGender(),
                                packet.getRace(),
                                packet.getDescription(),
                                false,
                                true,
                                "default",
                                0,
                                0
                        )
                );
                try {
                    server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), DOWN).save(new File("./characters/" + server.getCharacterManager().getCharacter(player).getId() + "/walk_down"));
                    server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), LEFT).save(new File("./characters/" + server.getCharacterManager().getCharacter(player).getId() + "/walk_left"));
                    server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), RIGHT).save(new File("./characters/" + server.getCharacterManager().getCharacter(player).getId() + "/walk_right"));
                    server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), UP).save(new File("./characters/" + server.getCharacterManager().getCharacter(player).getId() + "/walk_up"));
                } catch (IOException exception) {
                    server.getLogger().log(SEVERE, "Failed to save character sprites", exception);
                }
            } else {
                Player player = ctx.channel().attr(PLAYER).get();
                Character character = server.getCharacterManager().getCharacter(player);
                character.setName(packet.getName());
                character.setGender(packet.getGender());
                character.setRace(packet.getRace());
                character.setDescription(packet.getDescription());
                try {
                    Sprite downSprite = server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), DOWN);
                    downSprite.save(new File("./characters/" + server.getCharacterManager().getCharacter(player).getId() + "/walk_down"));
                    Sprite leftSprite = server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), LEFT);
                    leftSprite.save(new File("./characters/" + server.getCharacterManager().getCharacter(player).getId() + "/walk_left"));
                    Sprite rightSprite = server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), RIGHT);
                    rightSprite.save(new File("./characters/" + server.getCharacterManager().getCharacter(player).getId() + "/walk_right"));
                    Sprite upSprite = server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), UP);
                    upSprite.save(new File("./characters/" + server.getCharacterManager().getCharacter(player).getId() + "/walk_up"));
                    try {
                        channels.writeAndFlush(new PacketCharacterUpdate(character, upSprite, downSprite, leftSprite, rightSprite));
                    } catch (IOException exception) {
                        server.getLogger().log(SEVERE, "Failed to send character update", exception);
                    }
                    downSprite.flush();
                    leftSprite.flush();
                    rightSprite.flush();
                    upSprite.flush();
                } catch (IOException exception) {
                    server.getLogger().log(SEVERE, "Failed to save character sprites", exception);
                }
                server.getCharacterManager().updateCharacter(character);
            }
            ctx.writeAndFlush(new PacketCharacterSaveSuccessful());
        } else if (msg instanceof PacketRequestGenders) {
            List<String> genders = (List<String>) server.getConfiguration().get("genders");
            if (genders != null) {
                ctx.writeAndFlush(new PacketSendGenders(genders.toArray(new String[genders.size()])));
            }
        } else if (msg instanceof PacketRequestRaces) {
            List<String> races = (List<String>) server.getConfiguration().get("races");
            if (races != null) {
                ctx.writeAndFlush(new PacketSendRaces(races.toArray(new String[races.size()])));
            }
        } else if (msg instanceof PacketRequestCharacterSprites) {
            for (Sprite sprite : server.getCharacterComponentManager().getHairSprites(DOWN)) {
                ctx.writeAndFlush(new PacketAddHairSprite(sprite));
            }
            for (Sprite sprite : server.getCharacterComponentManager().getFaceSprites(DOWN)) {
                ctx.writeAndFlush(new PacketAddFaceSprite(sprite));
            }
            for (Sprite sprite : server.getCharacterComponentManager().getTorsoSprites(DOWN)) {
                ctx.writeAndFlush(new PacketAddTorsoSprite(sprite));
            }
            for (Sprite sprite : server.getCharacterComponentManager().getLegsSprites(DOWN)) {
                ctx.writeAndFlush(new PacketAddLegsSprite(sprite));
            }
            for (Sprite sprite : server.getCharacterComponentManager().getFeetSprites(DOWN)) {
                ctx.writeAndFlush(new PacketAddFeetSprite(sprite));
            }
        }
    }

    private void printPacketDetails(Object packet) {
        Class packetClass = packet.getClass();
        System.out.println(packetClass.getSimpleName());
        for (Field field : packetClass.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                System.out.println("  " + field.getName() + ": " + field.get(packet));
            } catch (IllegalAccessException ignored) {
            }
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

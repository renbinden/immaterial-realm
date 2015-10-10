package io.github.alyphen.immaterial_realm.server.network;

import io.github.alyphen.immaterial_realm.common.character.Character;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannel;
import io.github.alyphen.immaterial_realm.common.database.table.PlayerTable;
import io.github.alyphen.immaterial_realm.common.entity.Entity;
import io.github.alyphen.immaterial_realm.common.entity.EntityCharacter;
import io.github.alyphen.immaterial_realm.common.hud.HUDComponent;
import io.github.alyphen.immaterial_realm.common.object.WorldObjectType;
import io.github.alyphen.immaterial_realm.common.packet.Packet;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.character.*;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.chat.PacketClientboundGlobalChatMessage;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.chat.PacketClientboundLocalChatMessage;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.chat.PacketSendChannel;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.chat.PacketSetChannel;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.entity.PacketEntitySpawn;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.hud.PacketSendHUDComponent;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.login.PacketClientboundPublicKey;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.login.PacketLoginStatus;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.login.PacketVersion;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.object.PacketCreateObject;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.object.PacketSendObjectType;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.player.PacketPlayerJoin;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.player.PacketPlayerLeave;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.player.PacketSendPlayers;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.sprite.*;
import io.github.alyphen.immaterial_realm.common.packet.clientbound.tile.PacketSendTile;
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
import io.github.alyphen.immaterial_realm.common.packet.serverbound.hud.PacketRequestHUDComponents;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.login.PacketLoginDetails;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.login.PacketServerboundPublicKey;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.object.PacketRequestObjectTypes;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.player.PacketRequestPlayers;
import io.github.alyphen.immaterial_realm.common.packet.serverbound.tile.PacketRequestTiles;
import io.github.alyphen.immaterial_realm.common.player.Player;
import io.github.alyphen.immaterial_realm.common.sprite.Sprite;
import io.github.alyphen.immaterial_realm.common.tile.Tile;
import io.github.alyphen.immaterial_realm.common.world.World;
import io.github.alyphen.immaterial_realm.common.world.WorldArea;
import io.github.alyphen.immaterial_realm.server.ImmaterialRealmServer;
import io.github.alyphen.immaterial_realm.server.event.character.CharacterCreateEvent;
import io.github.alyphen.immaterial_realm.server.event.character.CharacterUpdateEvent;
import io.github.alyphen.immaterial_realm.server.event.chat.ChatEvent;
import io.github.alyphen.immaterial_realm.server.event.entity.CharacterDespawnEvent;
import io.github.alyphen.immaterial_realm.server.event.entity.CharacterSpawnEvent;
import io.github.alyphen.immaterial_realm.server.event.player.PlayerJoinEvent;
import io.github.alyphen.immaterial_realm.server.event.player.PlayerLoginEvent;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.alyphen.immaterial_realm.common.network.AttributeKeys.PLAYER;
import static io.github.alyphen.immaterial_realm.common.network.AttributeKeys.PUBLIC_KEY;
import static io.github.alyphen.immaterial_realm.common.world.Direction.*;
import static io.netty.channel.ChannelHandler.Sharable;
import static java.util.logging.Level.SEVERE;

@Sharable
public class ImmaterialRealmServerHandler extends ChannelHandlerAdapter {

    private ImmaterialRealmServer server;

    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


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
        if (player != null) {
            Character character = server.getCharacterManager().getCharacter(player);
            for (World world : server.getImmaterialRealm().getWorldManager().getWorlds()) {
                for (WorldArea area : world.getAreas()) {
                    Set<Entity> entitiesToRemove = new HashSet<>();
                    area.getEntities().stream().filter(entity -> entity instanceof EntityCharacter).forEach(entity -> {
                        EntityCharacter characterEntity = (EntityCharacter) entity;
                        if (characterEntity.getCharacter().getPlayerUUID().equals(player.getUUID())) {
                            character.setArea(area);
                            character.setX(characterEntity.getX());
                            character.setY(characterEntity.getY());
                            try {
                                server.getCharacterManager().updateCharacter(character);
                            } catch (SQLException exception) {
                                server.getLogger().log(SEVERE, "Failed to update character", exception);
                            }
                            server.getEventManager().onEvent(new CharacterDespawnEvent(characterEntity));
                            entitiesToRemove.add(entity);
                        }
                    });
                    entitiesToRemove.forEach(area::removeEntity);
                }
            }
            channels.stream().filter(channel -> channel != ctx.channel()).forEach(channel -> channel.writeAndFlush(new PacketPlayerLeave(player.getUUID())));
        }
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
                try {
                    ((PlayerTable) server.getDatabaseManager().getDatabase().getTable(Player.class)).insert(new Player(packet.getPlayerName()), server.getEncryptionManager().decrypt(packet.getEncryptedPassword()));
                } catch (SQLException exception) {
                    server.getLogger().log(SEVERE, "Failed to commit new user to database", exception);
                    PlayerLoginEvent event = new PlayerLoginEvent(null, packet.isSignUp(), false, "Login unsuccessful: Server failed to commit new user to database");
                    server.getEventManager().onEvent(event);
                    ctx.writeAndFlush(new PacketLoginStatus(event.isSuccessful(), event.getFailMessage()));
                    return;
                }
            }
            Player player = ((PlayerTable) server.getDatabaseManager().getDatabase().getTable(Player.class)).get(packet.getPlayerName());
            if (player != null) {
                if (((PlayerTable) server.getDatabaseManager().getDatabase().getTable(Player.class)).checkLogin(player, server.getEncryptionManager().decrypt(packet.getEncryptedPassword()))) {
                    PlayerLoginEvent event = new PlayerLoginEvent(player, packet.isSignUp(), true, "");
                    server.getEventManager().onEvent(event);
                    ctx.channel().attr(PLAYER).set(player);
                    ctx.writeAndFlush(new PacketLoginStatus(true));
                    channels.stream().filter(channel -> channel != ctx.channel()).forEach(channel -> channel.writeAndFlush(new PacketPlayerJoin(player.getUUID(), player.getName())));
                } else {
                    PlayerLoginEvent event = new PlayerLoginEvent(player, packet.isSignUp(), false, "Login unsuccessful: Incorrect password");
                    server.getEventManager().onEvent(event);
                    ctx.writeAndFlush(new PacketLoginStatus(event.isSuccessful(), event.getFailMessage()));
                }
            } else {
                PlayerLoginEvent event = new PlayerLoginEvent(null, packet.isSignUp(), false, "Login unsuccessful: Player does not exist");
                server.getEventManager().onEvent(event);
                ctx.writeAndFlush(new PacketLoginStatus(event.isSuccessful(), event.getFailMessage()));
            }
        } else if (msg instanceof PacketRequestPlayers) {
            ctx.writeAndFlush(new PacketSendPlayers(channels.stream().filter(channel -> channel.attr(PLAYER).get() != null).map(channel -> channel.attr(PLAYER).get()).collect(Collectors.toSet())));
        } else if (msg instanceof PacketRequestTiles) {
            for (Tile tile : server.getImmaterialRealm().getTileManager().getTiles()) {
                try {
                    ctx.writeAndFlush(new PacketSendTile(tile));
                } catch (IOException exception) {
                    server.getLogger().log(SEVERE, "Failed to load tile from packet", exception);
                }
            }
        } else if (msg instanceof PacketRequestObjectTypes) {
            for (WorldObjectType type : server.getImmaterialRealm().getWorldObjectTypeManager().getObjectTypes()) {
                ctx.writeAndFlush(new PacketSendObjectType(server.getImmaterialRealm(), type.getObjectName(), type.getObjectSprite(), type.getObjectBounds()));
            }
        } else if (msg instanceof PacketRequestWorlds) {
            for (World world : server.getImmaterialRealm().getWorldManager().getWorlds()) {
                ctx.writeAndFlush(new PacketSendWorld(world.getName()));
            }
        } else if (msg instanceof PacketRequestCurrentWorldArea) {
            PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(ctx.channel().attr(PLAYER).get());
            server.getEventManager().onEvent(playerJoinEvent);
            WorldArea area = server.getImmaterialRealm().getWorldManager().getWorld("default").getArea("default");
            ctx.writeAndFlush(new PacketSendArea(area));
            ctx.writeAndFlush(new PacketShowArea("default"));
            area.getObjects().stream().forEach(object -> ctx.writeAndFlush(new PacketCreateObject(object.getType(), area.getWorld().getName(), area.getName(), object.getX(), object.getY())));
            area.getEntities().stream().filter(entity -> !(entity instanceof EntityCharacter)).forEach(entity -> ctx.writeAndFlush(new PacketEntitySpawn(entity.getUUID(), entity.getClass(), area.getName(), entity.getX(), entity.getY())));
            area.getEntities().stream().filter(entity -> entity instanceof EntityCharacter).forEach(entity -> {
                EntityCharacter entityCharacter = (EntityCharacter) entity;
                Character character = entityCharacter.getCharacter();
                try {
                    ctx.writeAndFlush(new PacketCharacterSpawn(character, entity.getUUID()));
                } catch (IOException exception) {
                    server.getLogger().log(SEVERE, "Failed to send character spawn packet", exception);
                }
            });
            Player player = ctx.channel().attr(PLAYER).get();
            Character character = server.getCharacterManager().getCharacter(player);
            if (character == null) {
                character = new Character(UUID.randomUUID(), player.getUUID(), server.getCharacterManager().getDefaultWalkUpSprite(), server.getCharacterManager().getDefaultWalkDownSprite(), server.getCharacterManager().getDefaultWalkLeftSprite(), server.getCharacterManager().getDefaultWalkRightSprite());
                server.getDatabaseManager().getDatabase().getTable(Character.class).insert(character);
            }
            EntityCharacter entity = server.getImmaterialRealm().getEntityFactory().spawn(EntityCharacter.class, area, character.getX(), character.getY());
            if (entity != null) {
                CharacterSpawnEvent entitySpawnEvent = new CharacterSpawnEvent(entity, area.getWorld(), area, entity.getX(), entity.getY());
                server.getEventManager().onEvent(entitySpawnEvent);
                if (entitySpawnEvent.isCancelled()) {
                    area.removeEntity(entity);
                    return;
                }
                entitySpawnEvent.getEntity().setCharacter(character);
                try {
                    channels.writeAndFlush(new PacketCharacterSpawn(entitySpawnEvent.getEntity().getCharacter(), entitySpawnEvent.getEntity().getUUID()));
                } catch (IOException exception) {
                    server.getLogger().log(SEVERE, "Failed to send character spawn packet", exception);
                }
            }
        } else if (msg instanceof PacketRequestHUDComponents) {
            for (HUDComponent component : server.getHUDManager().getPlayerHUD(ctx.channel().attr(PLAYER).get()).getComponents()) {
                ctx.writeAndFlush(new PacketSendHUDComponent(component));
            }
        } else if (msg instanceof PacketControlPressed) {
            PacketControlPressed packet = (PacketControlPressed) msg;
            Character character = server.getCharacterManager().getCharacter(ctx.channel().attr(PLAYER).get());
            EntityCharacter characterEntity = null;
            worldLoop: for (World world : server.getImmaterialRealm().getWorldManager().getWorlds()) {
                for (WorldArea area : world.getAreas()) {
                    for (Entity entity : area.getEntities()) {
                        if (entity instanceof EntityCharacter) {
                            if (((EntityCharacter) entity).getCharacter().getUUID().equals(character.getUUID())) {
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
            worldLoop: for (World world : server.getImmaterialRealm().getWorldManager().getWorlds()) {
                for (WorldArea area : world.getAreas()) {
                    for (Entity entity : area.getEntities()) {
                        if (entity instanceof EntityCharacter) {
                            if (((EntityCharacter) entity).getCharacter().getUUID().equals(character.getUUID())) {
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
            ChatChannel chatChannel = server.getImmaterialRealm().getChatChannelManager().getChatChannel(packet.getChannel());
            Player sender = ctx.channel().attr(PLAYER).get();
            Character character = server.getCharacterManager().getCharacter(sender);
            ChatEvent event = new ChatEvent(sender, character, chatChannel, packet.getMessage());
            server.getEventManager().onEvent(event);
            if (!event.isCancelled()) {
                channels.stream().filter(channel -> {
                    Player player = event.getPlayer();
                    EntityCharacter characterEntity = null;
                    for (Entity entity : server.getImmaterialRealm().getWorldManager().getWorld("default").getArea(event.getCharacter().getAreaName()).getEntities()) {
                        if (entity instanceof EntityCharacter) {
                            if (((EntityCharacter) entity).getCharacter().getUUID().equals(event.getCharacter().getUUID())) {
                                characterEntity = (EntityCharacter) entity;
                            }
                        }
                    }
                    if (characterEntity != null) {
                        for (Entity entity : server.getImmaterialRealm().getWorldManager().getWorld("default").getArea(event.getCharacter().getAreaName()).getEntities()) {
                            if (entity instanceof EntityCharacter) {
                                EntityCharacter otherCharacterEntity = (EntityCharacter) entity;
                                if (otherCharacterEntity.getCharacter().getPlayerUUID().equals(player.getUUID()) && (otherCharacterEntity.distanceSquared(characterEntity) <= chatChannel.getRadius() * chatChannel.getRadius())) {
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }).forEach(channel -> channel.writeAndFlush(new PacketClientboundLocalChatMessage(event.getCharacter(), event.getChannel().getName(), event.getMessage())));
            }
        } else if (msg instanceof PacketRequestChannels) {
            for (ChatChannel channel : server.getImmaterialRealm().getChatChannelManager().getChatChannels()) {
                ctx.writeAndFlush(new PacketSendChannel(channel));
            }
            ctx.writeAndFlush(new PacketSetChannel(server.getImmaterialRealm().getChatChannelManager().getDefaultChatChannel().getName()));
        } else if (msg instanceof PacketServerboundGlobalChatMessage) {
            PacketServerboundGlobalChatMessage packet = (PacketServerboundGlobalChatMessage) msg;
            Player player = ctx.channel().attr(PLAYER).get();
            ChatEvent event = new ChatEvent(player, server.getCharacterManager().getCharacter(player), server.getImmaterialRealm().getChatChannelManager().getChatChannel(packet.getChannel()), packet.getMessage());
            if (!event.isCancelled())
                channels.writeAndFlush(new PacketClientboundGlobalChatMessage(event.getPlayer(), event.getChannel().getName(), event.getMessage()));
        } else if (msg instanceof PacketSaveCharacter) {
            PacketSaveCharacter packet = (PacketSaveCharacter) msg;
            if (packet.isNewCharacter()) {
                Player player = ctx.channel().attr(PLAYER).get();
                CharacterCreateEvent characterCreateEvent = new CharacterCreateEvent(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), packet.getName(), packet.getGender(), packet.getRace(), packet.getDescription());
                server.getEventManager().onEvent(characterCreateEvent);
                for (Character character : server.getCharacterManager().getCharacters(player)) {
                    character.setActive(false);
                    server.getCharacterManager().updateCharacter(character);
                }
                server.getDatabaseManager().getDatabase().getTable(Character.class).insert(
                        new Character(
                                UUID.randomUUID(),
                                player,
                                packet.getName(),
                                packet.getGender(),
                                packet.getRace(),
                                packet.getDescription(),
                                false,
                                true,
                                "default",
                                0,
                                0,
                                server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), UP),
                                server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), DOWN),
                                server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), LEFT),
                                server.getCharacterComponentManager().combine(packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), RIGHT)
                        )
                );
            } else {
                Player player = ctx.channel().attr(PLAYER).get();
                Character character = server.getCharacterManager().getCharacter(player);
                CharacterUpdateEvent characterUpdateEvent = new CharacterUpdateEvent(character, packet.getHairId(), packet.getFaceId(), packet.getTorsoId(), packet.getLegsId(), packet.getFeetId(), packet.getName(), packet.getGender(), packet.getRace(), packet.getDescription());
                if (!characterUpdateEvent.isCancelled()) {
                    character.setName(characterUpdateEvent.getNewName());
                    character.setGender(characterUpdateEvent.getNewGender());
                    character.setRace(characterUpdateEvent.getNewRace());
                    character.setDescription(characterUpdateEvent.getNewDescription());
                    character.setWalkUpSprite(server.getCharacterComponentManager().combine(characterUpdateEvent.getNewHairId(), characterUpdateEvent.getNewFaceId(), characterUpdateEvent.getNewTorsoId(), characterUpdateEvent.getNewLegsId(), characterUpdateEvent.getNewFeetId(), UP));
                    character.setWalkDownSprite(server.getCharacterComponentManager().combine(characterUpdateEvent.getNewHairId(), characterUpdateEvent.getNewFaceId(), characterUpdateEvent.getNewTorsoId(), characterUpdateEvent.getNewLegsId(), characterUpdateEvent.getNewFeetId(), DOWN));
                    character.setWalkLeftSprite(server.getCharacterComponentManager().combine(characterUpdateEvent.getNewHairId(), characterUpdateEvent.getNewFaceId(), characterUpdateEvent.getNewTorsoId(), characterUpdateEvent.getNewLegsId(), characterUpdateEvent.getNewFeetId(), LEFT));
                    character.setWalkRightSprite(server.getCharacterComponentManager().combine(characterUpdateEvent.getNewHairId(), characterUpdateEvent.getNewFaceId(), characterUpdateEvent.getNewTorsoId(), characterUpdateEvent.getNewLegsId(), characterUpdateEvent.getNewFeetId(), RIGHT));
                    server.getDatabaseManager().getDatabase().getTable(Character.class).update(character);
                    try {
                        channels.writeAndFlush(new PacketCharacterUpdate(character));
                    } catch (IOException exception) {
                        server.getLogger().log(SEVERE, "Failed to send character update", exception);
                    }
                    server.getCharacterManager().updateCharacter(character);
                }
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
                ctx.writeAndFlush(new PacketAddHairSprite(server.getImmaterialRealm(), sprite));
            }
            for (Sprite sprite : server.getCharacterComponentManager().getFaceSprites(DOWN)) {
                ctx.writeAndFlush(new PacketAddFaceSprite(server.getImmaterialRealm(), sprite));
            }
            for (Sprite sprite : server.getCharacterComponentManager().getTorsoSprites(DOWN)) {
                ctx.writeAndFlush(new PacketAddTorsoSprite(server.getImmaterialRealm(), sprite));
            }
            for (Sprite sprite : server.getCharacterComponentManager().getLegsSprites(DOWN)) {
                ctx.writeAndFlush(new PacketAddLegsSprite(server.getImmaterialRealm(), sprite));
            }
            for (Sprite sprite : server.getCharacterComponentManager().getFeetSprites(DOWN)) {
                ctx.writeAndFlush(new PacketAddFeetSprite(server.getImmaterialRealm(), sprite));
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

    public void sendPacket(Player player, Packet packet) {
        channels.stream().filter(channel -> channel.attr(PLAYER).get() == player).forEach(channel -> channel.writeAndFlush(packet));
    }

}

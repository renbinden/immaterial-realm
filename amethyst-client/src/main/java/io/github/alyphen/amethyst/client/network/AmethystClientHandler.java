package io.github.alyphen.amethyst.client.network;

import io.github.alyphen.amethyst.client.AmethystClient;
import io.github.alyphen.amethyst.common.character.Character;
import io.github.alyphen.amethyst.common.entity.Entity;
import io.github.alyphen.amethyst.common.entity.EntityCharacter;
import io.github.alyphen.amethyst.common.entity.EntityFactory;
import io.github.alyphen.amethyst.common.object.WorldObject;
import io.github.alyphen.amethyst.common.object.WorldObjectFactory;
import io.github.alyphen.amethyst.common.object.WorldObjectInitializer;
import io.github.alyphen.amethyst.common.packet.PacketPing;
import io.github.alyphen.amethyst.common.packet.character.PacketCharacterSpawn;
import io.github.alyphen.amethyst.common.packet.entity.PacketEntityMove;
import io.github.alyphen.amethyst.common.packet.entity.PacketEntitySpawn;
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
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

import static io.github.alyphen.amethyst.common.object.WorldObjectFactory.registerObjectInitializer;
import static java.util.concurrent.TimeUnit.SECONDS;

public class AmethystClientHandler extends ChannelHandlerAdapter {

    private AmethystClient client;
    private Timer timer;

    public AmethystClientHandler(AmethystClient client) {
        this.client = client;
        timer = new HashedWheelTimer();
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws GeneralSecurityException, IOException, SQLException {
        System.out.println(msg.toString());
        if (msg instanceof PacketVersion) {
            context.writeAndFlush(new PacketPublicKey(client.getEncryptionManager().getKeyPair().getPublic().getEncoded()));
        } else if (msg instanceof PacketPublicKey) {
            PacketPublicKey packet = (PacketPublicKey) msg;
            client.getNetworkManager().setServerPublicKey(packet.getEncodedPublicKey());
            client.showPanel("login");
        } else if (msg instanceof PacketLoginStatus) {
            PacketLoginStatus packet = (PacketLoginStatus) msg;
            if (packet.isSuccessful()) {
                client.showPanel("world");
                context.writeAndFlush(new PacketRequestPlayers());
            } else {
                client.getLoginPanel().setStatusMessage("Login unsuccessful.");
                client.getLoginPanel().reEnableLoginButtons();
            }
        } else if (msg instanceof PacketPing) {
            timer.newTimeout(timeout -> context.writeAndFlush(new PacketPing()), 20, SECONDS);
        } else if (msg instanceof PacketSendPlayers) {
            PacketSendPlayers packet = (PacketSendPlayers) msg;
            for (Player player : packet.getPlayers()) {
                if (client.getPlayerManager().getPlayer(player.getId()) == null) {
                    client.getPlayerManager().addPlayer(player);
                }
            }
            context.writeAndFlush(new PacketRequestTileSheets());
            context.writeAndFlush(new PacketRequestObjectTypes());
            context.writeAndFlush(new PacketRequestWorlds());
        } else if (msg instanceof PacketSendTileSheet) {
            TileSheet.load((PacketSendTileSheet) msg);
        } else if (msg instanceof PacketSendObjectType) {
            PacketSendObjectType packet = (PacketSendObjectType) msg;
            registerObjectInitializer(packet.getName(), new WorldObjectInitializer() {

                @Override
                public String getObjectName() {
                    return packet.getName();
                }

                @Override
                public Sprite getObjectSprite() {
                    return packet.getSprite();
                }

                @Override
                public Rectangle getObjectBounds() {
                    return packet.getBounds();
                }

                @Override
                public WorldObject initialize(long id) {
                    return new WorldObject(id, getObjectName(), getObjectSprite(), getObjectBounds());
                }

            });
        } else if (msg instanceof PacketSendWorld) {
            PacketSendWorld packet = (PacketSendWorld) msg;
            client.getWorldPanel().setWorld(World.create(packet.getName()));
            context.writeAndFlush(new PacketRequestCurrentWorldArea());
        } else if (msg instanceof PacketSendArea) {
            PacketSendArea packet = (PacketSendArea) msg;
            client.getWorldPanel().getWorld().addArea(WorldArea.load(packet));
            context.writeAndFlush(new PacketRequestObjects(packet.getWorld(), packet.getArea()));
        } else if (msg instanceof PacketShowArea) {
            PacketShowArea packet = (PacketShowArea) msg;
            client.getWorldPanel().setArea(client.getWorldPanel().getWorld().getArea(packet.getArea()));
        } else if (msg instanceof PacketCreateObject) {
            PacketCreateObject packet = (PacketCreateObject) msg;
            if (client.getWorldPanel().getWorld().getName().equals(packet.getWorld()) && client.getWorldPanel().getArea().getName().equals(packet.getArea())) {
                WorldObject object = WorldObjectFactory.createObject(packet.getType());
                object.setX(packet.getX());
                object.setY(packet.getY());
                client.getWorldPanel().getArea().addObject(object);
            }
        } else if (msg instanceof PacketEntitySpawn) {
            PacketEntitySpawn packet = (PacketEntitySpawn) msg;
            if (packet.getAreaName().equals(client.getWorldPanel().getArea().getName())) {
                Entity entity = EntityFactory.spawn(packet, client.getWorldPanel().getWorld());
                entity.setX(packet.getX());
                entity.setY(packet.getY());
            }
        } else if (msg instanceof PacketCharacterSpawn) {
            PacketCharacterSpawn packet = (PacketCharacterSpawn) msg;
            if (client.getWorldPanel().getArea() != null) {
                if (packet.getAreaName().equals(client.getWorldPanel().getArea().getName())) {
                    Character character = client.getCharacterManager().getCharacter(packet.getId());
                    if (character == null) {
                        character = new Character(packet.getPlayerId(), packet.getId(), packet.getName(), packet.getGender(), packet.getRace(), packet.getDescription(), packet.isDead(), packet.isActive(), packet.getAreaName(), packet.getX(), packet.getY());
                        client.getCharacterManager().addCharacter(character);
                    } else {
                        character.setPlayerId(packet.getPlayerId());
                        character.setName(packet.getName());
                        character.setGender(packet.getGender());
                        character.setRace(packet.getRace());
                        character.setDescription(packet.getDescription());
                        character.setDead(packet.isDead());
                        character.setActive(packet.isActive());
                        character.setAreaName(packet.getAreaName());
                        character.setX(packet.getX());
                        character.setY(packet.getY());
                        client.getCharacterManager().updateCharacter(character);
                    }
                    character.setWalkUpSprite(packet.getWalkUpSprite());
                    character.setWalkDownSprite(packet.getWalkDownSprite());
                    character.setWalkLeftSprite(packet.getWalkLeftSprite());
                    character.setWalkRightSprite(packet.getWalkRightSprite());
                    EntityCharacter entity = EntityFactory.spawn(EntityCharacter.class, client.getWorldPanel().getArea(), packet.getX(), packet.getY());
                    entity.setCharacter(character);
                    if (character.getPlayerId() == client.getPlayerManager().getPlayer(client.getPlayerName()).getId()) {
                        client.getWorldPanel().setPlayerCharacter(entity);
                    }
                }
            }
        } else if (msg instanceof PacketEntityMove) {
            PacketEntityMove packet = (PacketEntityMove) msg;
            if (packet.getAreaName().equals(client.getWorldPanel().getArea().getName())) {
                Entity entity = null;
                for (Entity entity1 : client.getWorldPanel().getArea().getEntities()) {
                    if (packet.getEntityId() == entity1.getId()) {
                        entity = entity1;
                        break;
                    }
                }
                if (entity != null) {
                    entity.setDirectionFacing(packet.getDirectionFacing());
                    entity.setX(packet.getX());
                    entity.setY(packet.getY());
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}

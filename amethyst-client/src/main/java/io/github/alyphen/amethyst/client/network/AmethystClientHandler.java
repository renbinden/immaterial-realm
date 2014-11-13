package io.github.alyphen.amethyst.client.network;

import io.github.alyphen.amethyst.client.AmethystClient;
import io.github.alyphen.amethyst.common.character.Character;
import io.github.alyphen.amethyst.common.entity.Entity;
import io.github.alyphen.amethyst.common.entity.EntityCharacter;
import io.github.alyphen.amethyst.common.object.WorldObject;
import io.github.alyphen.amethyst.common.object.WorldObjectFactory;
import io.github.alyphen.amethyst.common.object.WorldObjectInitializer;
import io.github.alyphen.amethyst.common.packet.PacketPing;
import io.github.alyphen.amethyst.common.packet.character.PacketSendCharacterSprites;
import io.github.alyphen.amethyst.common.packet.entity.PacketEntitySpawn;
import io.github.alyphen.amethyst.common.packet.login.PacketLoginStatus;
import io.github.alyphen.amethyst.common.packet.login.PacketPublicKey;
import io.github.alyphen.amethyst.common.packet.login.PacketVersion;
import io.github.alyphen.amethyst.common.packet.object.PacketCreateObject;
import io.github.alyphen.amethyst.common.packet.object.PacketRequestObjectTypes;
import io.github.alyphen.amethyst.common.packet.object.PacketSendObjectType;
import io.github.alyphen.amethyst.common.packet.tile.PacketRequestTileSheets;
import io.github.alyphen.amethyst.common.packet.tile.PacketSendTileSheet;
import io.github.alyphen.amethyst.common.packet.world.*;
import io.github.alyphen.amethyst.common.sprite.Sprite;
import io.github.alyphen.amethyst.common.tile.TileSheet;
import io.github.alyphen.amethyst.common.world.World;
import io.github.alyphen.amethyst.common.world.WorldArea;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
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
                JOptionPane.showMessageDialog(null, "Login successful.");
                client.showPanel("world");
                context.writeAndFlush(new PacketPing());
                context.writeAndFlush(new PacketRequestTileSheets());
                context.writeAndFlush(new PacketRequestObjectTypes());
                context.writeAndFlush(new PacketRequestWorlds());
            } else {
                client.getLoginPanel().setStatusMessage("Login unsuccessful.");
                client.getLoginPanel().reEnableLoginButtons();
            }
        } else if (msg instanceof PacketPing) {
            timer.newTimeout(timeout -> context.writeAndFlush(new PacketPing()), 20, SECONDS);
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
                Entity entity = client.getEntityFactory().spawn(packet, client.getWorldPanel().getWorld());
                entity.setX(packet.getX());
                entity.setY(packet.getY());
                if (entity instanceof EntityCharacter) {
                    EntityCharacter character = (EntityCharacter) entity;
                    if (character.getPlayer().getName().equals(client.getPlayerName())) {
                        client.getWorldPanel().setPlayerCharacter(character);
                    }
                }
            }
        } else if (msg instanceof PacketSendCharacterSprites) {
            PacketSendCharacterSprites packet = (PacketSendCharacterSprites) msg;
            Sprite walkUpSprite = packet.getWalkUpSprite();
            walkUpSprite.save(new File("./characters/" + client.getNetworkManager().getServerAddress() + "/" + packet.getCharacterId() + "/walk_up.png"));
            Sprite walkDownSprite = packet.getWalkDownSprite();
            walkDownSprite.save(new File("./characters/" + client.getNetworkManager().getServerAddress() + "/" + packet.getCharacterId() + "/walk_down.png"));
            Sprite walkRightSprite = packet.getWalkRightSprite();
            walkRightSprite.save(new File("./characters/" + client.getNetworkManager().getServerAddress() + "/" + packet.getCharacterId() + "/walk_right.png"));
            Sprite walkLeftSprite = packet.getWalkLeftSprite();
            walkLeftSprite.save(new File("./characters/" + client.getNetworkManager().getServerAddress() + "/" + packet.getCharacterId() + "/walk_left.png"));
            Character character = client.getCharacterManager().getCharacter(packet.getCharacterId());
            character.setWalkUpSprite(walkUpSprite);
            character.setWalkDownSprite(walkDownSprite);
            character.setWalkRightSprite(walkRightSprite);
            character.setWalkLeftSprite(walkLeftSprite);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

}

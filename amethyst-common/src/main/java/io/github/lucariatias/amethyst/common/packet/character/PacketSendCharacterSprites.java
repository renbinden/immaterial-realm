package io.github.lucariatias.amethyst.common.packet.character;

import io.github.lucariatias.amethyst.common.packet.Packet;
import io.github.lucariatias.amethyst.common.sprite.Sprite;

import java.io.IOException;

public class PacketSendCharacterSprites extends Packet {

    private long characterId;
    private byte[][] walkUpSprite;
    private int walkUpFrameDelay;
    private byte[][] walkLeftSprite;
    private int walkLeftFrameDelay;
    private byte[][] walkRightSprite;
    private int walkRightFrameDelay;
    private byte[][] walkDownSprite;
    private int walkDownFrameDelay;

    public PacketSendCharacterSprites(long characterId, Sprite walkUpSprite, Sprite walkDownSprite, Sprite walkRightSprite, Sprite walkLeftSprite) {
        try {
            this.characterId = characterId;
            this.walkUpSprite = walkUpSprite.toByteArray();
            this.walkUpFrameDelay = walkUpSprite.getFrameDelay();
            this.walkDownSprite = walkDownSprite.toByteArray();
            this.walkDownFrameDelay = walkDownSprite.getFrameDelay();
            this.walkLeftSprite = walkLeftSprite.toByteArray();
            this.walkLeftFrameDelay = walkLeftSprite.getFrameDelay();
            this.walkRightSprite = walkRightSprite.toByteArray();
            this.walkRightFrameDelay = walkRightSprite.getFrameDelay();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public long getCharacterId() {
        return characterId;
    }

    public Sprite getWalkUpSprite() {
        try {
            return Sprite.fromByteArray(walkUpSprite, walkUpFrameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Sprite getWalkLeftSprite() {
        try {
            return Sprite.fromByteArray(walkLeftSprite, walkLeftFrameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Sprite getWalkRightSprite() {
        try {
            return Sprite.fromByteArray(walkRightSprite, walkRightFrameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Sprite getWalkDownSprite() {
        try {
            return Sprite.fromByteArray(walkDownSprite, walkDownFrameDelay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

}

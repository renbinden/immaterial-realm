package io.github.alyphen.immaterial_realm.common.network;

import io.github.alyphen.immaterial_realm.common.player.Player;
import io.netty.util.AttributeKey;

public class AttributeKeys {

    public static final AttributeKey<Player> PLAYER = AttributeKey.valueOf("player");
    public static final AttributeKey<byte[]> PUBLIC_KEY = AttributeKey.valueOf("publicKey");

    private AttributeKeys() {}

}

package io.github.alyphen.immaterial_realm.server.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.alyphen.immaterial_realm.common.chat.ChatChannel;

import java.awt.*;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ChatManager {

    private Map<String, ChatChannel> channels;
    private ChatChannel defaultChannel;

    public ChatManager() {
        channels = new HashMap<>();
        loadChannels();
    }
    
    public ChatChannel getChannel(String name) {
        return channels.get(name);
    }
    
    public void addChannel(ChatChannel channel) {
        channels.put(channel.getName(), channel);
    }

    public Collection<ChatChannel> getChannels() {
        return channels.values();
    }

    public ChatChannel getDefaultChannel() {
        return defaultChannel;
    }

    private void saveDefaultChannels() {
        File configDir = new File("./config");
        if (!configDir.exists()) configDir.mkdirs();
        File defaultChatConfigFile = new File(configDir, "chat.json");
        if (!defaultChatConfigFile.exists()) {
            Map<String, Object> defaultChatConfig = new HashMap<>();
            Map<String, Object> defaultChannelsConfig = new HashMap<>();
            Map<String, Object> sayChannelConfig = new HashMap<>();
            sayChannelConfig.put("radius", 256);
            Map<String, Object> sayChannelColour = new HashMap<>();
            sayChannelColour.put("red", 255);
            sayChannelColour.put("green", 255);
            sayChannelColour.put("blue", 255);
            sayChannelColour.put("alpha", 255);
            sayChannelConfig.put("colour", sayChannelColour);
            defaultChannelsConfig.put("say", sayChannelConfig);
            Map<String, Object> whisperChannelConfig = new HashMap<>();
            whisperChannelConfig.put("radius", 64);
            Map<String, Object> whisperChannelColour = new HashMap<>();
            whisperChannelColour.put("red", 120);
            whisperChannelColour.put("green", 120);
            whisperChannelColour.put("blue", 255);
            whisperChannelColour.put("alpha", 255);
            whisperChannelConfig.put("colour", whisperChannelColour);
            defaultChannelsConfig.put("whisper", whisperChannelConfig);
            Map<String, Object> shoutChannelConfig = new HashMap<>();
            shoutChannelConfig.put("radius", 256);
            Map<String, Object> shoutChannelColour = new HashMap<>();
            shoutChannelColour.put("red", 255);
            shoutChannelColour.put("green", 120);
            shoutChannelColour.put("blue", 120);
            shoutChannelColour.put("alpha", 255);
            shoutChannelConfig.put("colour", shoutChannelColour);
            defaultChannelsConfig.put("shout", shoutChannelConfig);
            Map<String, Object> generalChannelConfig = new HashMap<>();
            generalChannelConfig.put("radius", -1);
            Map<String, Object> generalChannelColour = new HashMap<>();
            generalChannelColour.put("red", 120);
            generalChannelColour.put("green", 255);
            generalChannelColour.put("blue", 120);
            generalChannelColour.put("alpha", 255);
            generalChannelConfig.put("colour", generalChannelColour);
            defaultChannelsConfig.put("general", generalChannelConfig);
            Map<String, Object> supportChannelConfig = new HashMap<>();
            supportChannelConfig.put("radius", -1);
            Map<String, Object> supportChannelColour = new HashMap<>();
            supportChannelColour.put("red", 192);
            supportChannelColour.put("green", 120);
            supportChannelColour.put("blue", 255);
            supportChannelColour.put("alpha", 255);
            supportChannelConfig.put("colour", supportChannelColour);
            defaultChannelsConfig.put("support", supportChannelConfig);
            defaultChatConfig.put("channels", defaultChannelsConfig);
            defaultChatConfig.put("default-channel", "say");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(defaultChatConfig);
            try (FileWriter writer = new FileWriter(defaultChatConfigFile)) {
                writer.write(json);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
    
    public void loadChannels() {
        saveDefaultChannels();
        File configDir = new File("./config");
        if (configDir.exists()) {
            File chatConfigFile = new File(configDir, "chat.json");
            if (chatConfigFile.exists()) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                StringBuilder jsonBuilder = new StringBuilder();
                try {
                    Scanner scanner = new Scanner(new FileInputStream(chatConfigFile));
                    while (scanner.hasNextLine()) {
                        jsonBuilder.append(scanner.nextLine()).append('\n');
                    }
                } catch (FileNotFoundException exception) {
                    exception.printStackTrace();
                }
                Map<String, Object> chatConfig = gson.fromJson(jsonBuilder.toString(), new TypeToken<Map<String, Object>>(){}.getType());
                Map<String, Object> channels = (Map<String, Object>) chatConfig.get("channels");
                for (Map.Entry<String, Object> entry : channels.entrySet()) {
                    Map<String, Object> channelConfig = (Map<String, Object>) entry.getValue();
                    Map<String, Object> channelColour = (Map<String, Object>) channelConfig.get("colour");
                    ChatChannel channel = new ChatChannel(
                            entry.getKey(),
                            new Color(
                                    (int) ((double) channelColour.get("red")),
                                    (int) ((double) channelColour.get("green")),
                                    (int) ((double) channelColour.get("blue")),
                                    (int) ((double) channelColour.get("alpha"))
                            ),
                            (int) ((double) channelConfig.get("radius"))
                    );
                    addChannel(channel);
                }
                defaultChannel = this.channels.get(chatConfig.get("default-channel"));
            }
        }
    }

    private void saveChannels() {
        File configDir = new File("./config");
        if (!configDir.exists()) configDir.mkdirs();
        File chatConfigFile = new File(configDir, "chat.json");
        if (!chatConfigFile.exists()) {
            Map<String, Object> chatConfig = new HashMap<>();
            Map<String, Object> channelsConfig = new HashMap<>();
            for (ChatChannel channel : getChannels()) {
                Map<String, Object> channelConfig = new HashMap<>();
                channelConfig.put("radius", channel.getRadius());
                Map<String, Object> channelColour = new HashMap<>();
                channelColour.put("red", channel.getColour().getRed());
                channelColour.put("green", channel.getColour().getGreen());
                channelColour.put("blue", channel.getColour().getBlue());
                channelColour.put("alpha", channel.getColour().getAlpha());
                channelConfig.put("colour", channelColour);
                channelsConfig.put(channel.getName(), channelConfig);
            }
            chatConfig.put("channels", channelsConfig);
            chatConfig.put("default-channel", getDefaultChannel().getName());
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(chatConfig);
            try (FileWriter writer = new FileWriter(chatConfigFile)) {
                writer.write(json);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
    
}

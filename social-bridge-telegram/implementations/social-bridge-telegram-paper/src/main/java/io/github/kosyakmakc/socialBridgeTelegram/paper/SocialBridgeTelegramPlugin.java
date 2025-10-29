package io.github.kosyakmakc.socialBridgeTelegram.paper;
import java.lang.Runtime.Version;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.kosyakmakc.socialBridge.SocialBridge;
import io.github.kosyakmakc.socialBridgeTelegram.TelegramPlatform;

public class SocialBridgeTelegramPlugin extends JavaPlugin {
    public SocialBridgeTelegramPlugin() {
        SocialBridge.INSTANCE.registerSocialPlatform(new TelegramPlatform(Version.parse("0.1.0")));
    }
}

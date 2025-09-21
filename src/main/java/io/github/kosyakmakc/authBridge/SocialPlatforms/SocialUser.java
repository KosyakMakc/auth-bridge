package io.github.kosyakmakc.authBridge.SocialPlatforms;

import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public abstract class SocialUser {
    private final ISocialPlatform platform;
    private MinecraftUser minecraftUser = null;
    private boolean isLazyMinecraftUser = true;

    public SocialUser(ISocialPlatform platform) {
        this.platform = platform;
    }

    public ISocialPlatform getPlatform() {
        return platform;
    }

    public @Nullable MinecraftUser getMinecraftUser() {
        if (isLazyMinecraftUser) {
            minecraftUser = platform.tryGetMinecraftUser(this);
            isLazyMinecraftUser = false;
        }
        return minecraftUser;
    }

    public abstract String getName();

    public abstract void sendMessage(String message, HashMap<String, String> placeholders);

    public abstract String getLocale();
}

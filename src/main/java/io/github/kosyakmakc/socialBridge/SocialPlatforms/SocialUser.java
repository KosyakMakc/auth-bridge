package io.github.kosyakmakc.socialBridge.SocialPlatforms;

import io.github.kosyakmakc.socialBridge.MinecraftPlatform.MinecraftUser;

import java.util.HashMap;

public abstract class SocialUser {
    private final ISocialPlatform platform;
    private MinecraftUser minecraftUser = null;
    private boolean isLazyMinecraftUser = true;

    public SocialUser(ISocialPlatform platform) {
        this.platform = platform;
    }

    @SuppressWarnings("unused")
    public ISocialPlatform getPlatform() {
        return platform;
    }

    public MinecraftUser getMinecraftUser() {
        if (isLazyMinecraftUser) {
            minecraftUser = platform.tryGetMinecraftUser(this);
            isLazyMinecraftUser = false;
        }
        return minecraftUser;
    }

    public abstract String getName();

    @SuppressWarnings("unused")
    public abstract void sendMessage(String message, HashMap<String, String> placeholders);

    @SuppressWarnings("unused")
    public abstract String getLocale();
}

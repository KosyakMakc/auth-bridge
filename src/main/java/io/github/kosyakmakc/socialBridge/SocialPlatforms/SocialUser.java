package io.github.kosyakmakc.socialBridge.SocialPlatforms;

import java.util.HashMap;

public abstract class SocialUser {
    private final ISocialPlatform platform;

    public SocialUser(ISocialPlatform platform) {
        this.platform = platform;
    }

    @SuppressWarnings("unused")
    public ISocialPlatform getPlatform() {
        return platform;
    }

    public abstract String getName();

    @SuppressWarnings("unused")
    public abstract void sendMessage(String message, HashMap<String, String> placeholders);

    @SuppressWarnings("unused")
    public abstract String getLocale();

    @SuppressWarnings("unused")
    public abstract SocialUserIdType getIdType();
    @SuppressWarnings("unused")
    public abstract Object getId();
}

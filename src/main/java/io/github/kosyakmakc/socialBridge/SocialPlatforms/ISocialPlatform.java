package io.github.kosyakmakc.socialBridge.SocialPlatforms;

import io.github.kosyakmakc.socialBridge.ISocialBridge;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.MinecraftUser;

import java.lang.Runtime.Version;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public interface ISocialPlatform {
    void setAuthBridge(ISocialBridge authBridge);
    String getPlatformName();
    Version getCompabilityVersion();

    @SuppressWarnings("unused")
    void Authorize(SocialUser sender, UUID minecraftId) throws SQLException, AuthorizeDuplicationException;

    @SuppressWarnings("unused")
    void sendMessage(SocialUser telegramUser, String message, HashMap<String, String> placeholders);

    MinecraftUser tryGetMinecraftUser(SocialUser socialUser);

    @SuppressWarnings("unused")
    boolean logoutUser(SocialUser sender);
    void Start();
}

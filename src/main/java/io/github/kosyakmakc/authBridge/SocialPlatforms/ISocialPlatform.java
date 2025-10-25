package io.github.kosyakmakc.authBridge.SocialPlatforms;

import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import io.github.kosyakmakc.authBridge.SocialPlatforms.TelegramPlatform.TelegramUser;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public interface ISocialPlatform {
    void setAuthBridge(IAuthBridge authBridge);
    String getPlatformName();

    void Authorize(SocialUser sender, UUID minecraftId) throws SQLException, AuthorizeDuplicationException;

    void sendMessage(TelegramUser telegramUser, String message, HashMap<String, String> placeholders);

    @Nullable MinecraftUser tryGetMinecraftUser(SocialUser socialUser);

    boolean logoutUser(SocialUser sender);
}

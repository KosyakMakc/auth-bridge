package io.github.kosyakmakc.authBridge.SocialPlatforms.TelegramPlatform;

import dev.vanutp.tgbridge.common.TgUser;
import io.github.kosyakmakc.authBridge.DatabasePlatform.LocalizationService;
import io.github.kosyakmakc.authBridge.SocialPlatforms.ISocialPlatform;
import io.github.kosyakmakc.authBridge.SocialPlatforms.SocialUser;

import java.util.HashMap;

public class TelegramUser extends SocialUser {
    private final TgUser user;

    public TelegramUser(ISocialPlatform socialPlatform, TgUser user) {
        super(socialPlatform);
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getFullName();
    }

    @Override
    public void sendMessage(String message, HashMap<String, String> placeholders) {
        getPlatform().sendMessage(this, message, placeholders);
    }

    @Override
    public String getLocale() {
        // TODO api extend?
        return LocalizationService.defaultLocale;
    }

    public long getId() {
        return user.getId();
    }
}

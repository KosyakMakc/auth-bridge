package io.github.kosyakmakc.authBridge.SocialPlatforms.TelegramPlatform;

import dev.vanutp.tgbridge.common.TelegramBridge;
import dev.vanutp.tgbridge.common.TgbridgeEvents;
import dev.vanutp.tgbridge.common.compat.AbstractCompat;
import tgbridge.shaded.kotlin.Pair;
import tgbridge.shaded.kotlin.Unit;

public class AppendAuthorizedNameIntegration extends AbstractCompat {
    private final TelegramPlatform socialPlatform;

    public AppendAuthorizedNameIntegration(TelegramPlatform socialPlatform, TelegramBridge bridge) {
        super(bridge);
        this.socialPlatform = socialPlatform;
    }

    @Override
    public void enable() {
        TgbridgeEvents.INSTANCE.getTG_CHAT_MESSAGE().addListener((chatEvent, continuation) -> {
            var mcPlayer = new TelegramUser(socialPlatform, chatEvent.getMessage().getFrom()).getMinecraftUser();

            if (mcPlayer != null) {
                chatEvent.getPlaceholders().addPlain(new Pair<>("authBridge-minecraftName", mcPlayer.getName()));
            }

            return Unit.INSTANCE;
        });
    }
}

package io.github.kosyakmakc.authBridge.SocialPlatforms.TelegramPlatform;

import dev.vanutp.tgbridge.common.TelegramBridge;
import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.Association_telegram;
import io.github.kosyakmakc.authBridge.IAuthBridge;
import io.github.kosyakmakc.authBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import io.github.kosyakmakc.authBridge.SocialPlatforms.AuthorizeDuplicationException;
import io.github.kosyakmakc.authBridge.SocialPlatforms.ISocialPlatform;
import io.github.kosyakmakc.authBridge.SocialPlatforms.SocialUser;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class TelegramPlatform implements ISocialPlatform {
    private IAuthBridge bridge;
    private TelegramBridge tgBridge;

    @Override
    public void setAuthBridge(IAuthBridge bridge) {
        this.bridge = bridge;
        this.tgBridge = TelegramBridge.Companion.getINSTANCE();

        var self = this;

        tgBridge.addIntegration(new AppendAuthorizedNameIntegration(this, tgBridge));
        for (var socialCommand : bridge.getSocialCommands()) {
            tgBridge.getBot().registerCommandHandler(socialCommand.getLiteral(), (tgMessage, continuation) -> {
                var sender = new TelegramUser(self, tgMessage.getFrom());
                try {
                    socialCommand.handle(sender, tgMessage.getEffectiveText());
                } catch (ArgumentFormatException e) {
                    sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), e.getMessageKey()), new HashMap<String, String>());
                }
                return null;
            });
        }
    }

    public IAuthBridge getBridge() {
        return bridge;
    }

    public TelegramBridge getTgBridge() {
        return tgBridge;
    }

    @Override
    public String getPlatformName() {
        return "telegram";
    }

    @Override
    public void Authorize(SocialUser sender, UUID minecraftId) throws SQLException, AuthorizeDuplicationException {
        if (!(sender instanceof TelegramUser tgUser)) {
            throw new RuntimeException("incorrect usage, SocialUser(" + sender.getClass().getName() + ") MUST BE assigned to this ISocialPlatform(" + this.getClass().getName() + ")");
        }
        var bridge = getBridge();
        var isDuplicate = new AtomicBoolean(false);
        bridge.queryDatabase(databaseContext -> {
            var existedRows = databaseContext.association_telegram
                    .queryBuilder()
                    .where()
                    .eq(Association_telegram.MINECRAFT_ID_FIELD_NAME, minecraftId)
                    .and()
                    .eq(Association_telegram.IS_DELETED_FIELD_NAME, false)
                    .countOf();

            if (existedRows > 0) {
                isDuplicate.set(true);
            }
            else {
                var association = new Association_telegram(minecraftId, tgUser.getId());
                databaseContext.association_telegram.create(association);
            }

            return null;
        });

        if(isDuplicate.get()) {
            throw new AuthorizeDuplicationException();
        }
    }

    @Override
    public void sendMessage(TelegramUser telegramUser, String message, HashMap<String, String> placeholders) {
        // TODO api extend?
    }

    @Override
    public @Nullable MinecraftUser tryGetMinecraftUser(SocialUser socialUser) {
        if (!(socialUser instanceof TelegramUser tgUser)) {
            throw new RuntimeException("incorrect usage, SocialUser(" + socialUser.getClass().getName() + ") MUST BE assigned to this ISocialPlatform(" + this.getClass().getName() + ")");
        }
        var bridge = getBridge();
        var logger = bridge.getLogger();
        var result = new AtomicReference<MinecraftUser>(null);
        try {
            bridge.queryDatabase(databaseContext -> {
                var association = databaseContext.association_telegram
                        .queryBuilder()
                        .where()
                        .eq(Association_telegram.TELEGRAM_ID_FIELD_NAME, tgUser.getId())
                        .and()
                        .eq(Association_telegram.IS_DELETED_FIELD_NAME, false)
                        .queryForFirst();

                if (association != null) {
                    result.set(bridge.getMinecraftPlatform().getUser(association.getMinecraftId()));
                }

                return null;
            });
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "failed get minecraft user", e);
        }

        return result.get();
    }
}

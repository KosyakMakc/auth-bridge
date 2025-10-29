package io.github.kosyakmakc.socialBridgeTelegram;
import dev.vanutp.tgbridge.common.TelegramBridge;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.Association_telegram;
import io.github.kosyakmakc.socialBridge.ISocialBridge;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.MinecraftUser;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.AuthorizeDuplicationException;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.ISocialPlatform;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.SocialUser;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class TelegramPlatform implements ISocialPlatform {
    private ISocialBridge bridge;
    private TelegramBridge tgBridge;

    public TelegramPlatform() {

    }

    @Override
    public void Start() {
        this.tgBridge = TelegramBridge.Companion.getINSTANCE();

        var self = this;

        tgBridge.addIntegration(new MessageHandlerIntegration(this));
//        for (var socialCommand : bridge.getSocialCommands()) {
//            tgBridge.getBot().registerCommandHandler(socialCommand.getLiteral(), (tgMessage, continuation) -> {
//
//                return null;
//            });
//        }
    }

    @Override
    public void setAuthBridge(ISocialBridge bridge) {
        this.bridge = bridge;
    }

    public ISocialBridge getBridge() {
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
    public void sendMessage(SocialUser telegramUser, String message, HashMap<String, String> placeholders) {

//        var builder = MiniMessage.builder()
//                .tags(TagResolver.builder()
//                        .resolver(StandardTags.defaults())
//                        .build());
//
//        for (var placeholderKey : placeholders.keySet()) {
//            builder.editTags(x -> x.resolver(Placeholder.component(placeholderKey, Component.text(placeholders.get(placeholderKey)))));
//        }
//        var builtMessage = builder.build().deserialize(message);


        // TODO api extend?
//            var chatId = chatEvent.getMessage().getChat().getId();
//            var replyToId = chatEvent.getMessage().getMessageId();
//            socialPlatform.getTgBridge().getBot().sendMessage(
//                    chatId,
//                    builtMessage,
//                    null,
//                    replyToId,
//                    "HTML",
//                    true,
//                    null);
//        this.getBridge().getLogger().info("tgMessage to \"" + telegramUser.getName() + "\" - " + builtMessage);
        this.getBridge().getLogger().info("tgMessage to \"" + telegramUser.getName() + "\" - " + message);
    }

    @Override
    public MinecraftUser tryGetMinecraftUser(SocialUser socialUser) {
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

    @Override
    public boolean logoutUser(SocialUser socialUser) {
        if (!(socialUser instanceof TelegramUser tgUser)) {
            throw new RuntimeException("incorrect usage, SocialUser(" + socialUser.getClass().getName() + ") MUST BE assigned to this ISocialPlatform(" + this.getClass().getName() + ")");
        }
        var bridge = getBridge();
        var logger = bridge.getLogger();
        var result = new AtomicBoolean(false);
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
                    association.Delete();
                    databaseContext.association_telegram.update(association);
                    result.set(true);
                }
                else {
                    result.set(false);
                }

                return null;
            });
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "failed get minecraft user", e);
        }

        return result.get();
    }
}

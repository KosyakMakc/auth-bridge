package io.github.kosyakmakc.authBridge.SocialCommands;

import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.AuthSession;
import io.github.kosyakmakc.authBridge.MessageKey;
import io.github.kosyakmakc.authBridge.Permissions;
import io.github.kosyakmakc.authBridge.SocialPlatforms.AuthorizeDuplicationException;
import io.github.kosyakmakc.authBridge.SocialPlatforms.SocialUser;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class CommitLoginCommand extends SocialCommandBase {
    public CommitLoginCommand() {
        super("authBridge-login", Permissions.NO_PERMISSION);
    }

    @Override
    public boolean canExecute(SocialUser sender, String[] args) {
        return sender != null
                && args.length == 2
                && args[0].equalsIgnoreCase("login");
    }

    @Override
    public void execute(SocialUser sender, String[] args) {
        var bridge = getBridge();
        var logger = bridge.getLogger();

        var placeholders = new HashMap<String, String>();

        try {
            AtomicBoolean isSuccess = new AtomicBoolean(false);
            bridge.queryDatabase(database -> {
                var availableSessions = database.sessions.queryBuilder()
                        .orderBy(AuthSession.EXPIRED_AT_FIELD_NAME, true)
                        .where()
                        .eq(AuthSession.MINECRAFT_ID_FIELD_NAME, UUID.fromString(args[1]))
                        .and()
                        .eq(AuthSession.IS_SPENT_FIELD_NAME, false)
                        .and()
                        .lt(AuthSession.EXPIRED_AT_FIELD_NAME, Date.from(Instant.now()))
                        .query();

                if (availableSessions.isEmpty()) {
                    logger.info(sender.getName() + " failed to commit login");
                    sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.COMMIT_LOGIN_FAILED), new HashMap<>());
                    return null;
                }

                var session = availableSessions.getFirst();

                try {
                    sender.getPlatform().Authorize(sender, session.getMinecraftId());
                    isSuccess.set(true);
                } catch (AuthorizeDuplicationException e) {
                    isSuccess.set(false);
                }

                session.spend();
                database.sessions.update(session);

                return null;
            });

            if (isSuccess.get()) {
                logger.info(sender.getName() + " success commited login to " + sender.getPlatform().getPlatformName() + " platform");
                placeholders.put("social-platform-name", sender.getPlatform().getPlatformName());
                sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.COMMITED_LOGIN), placeholders);
            }
            else {
                logger.info(sender.getName() + " duplicating his logins to " + sender.getPlatform().getPlatformName() + ", ignoring it...");
                placeholders.put("social-platform-name", sender.getPlatform().getPlatformName());
                sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.YOU_ARE_ALREADY_AUTHORIZED), placeholders);
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "failed to commit login", e);
            sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.INTERNAL_SERVER_ERROR), placeholders);
        }
    }
}

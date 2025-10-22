package io.github.kosyakmakc.authBridge.Commands.SocialCommands;

import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.AuthSession;
import io.github.kosyakmakc.authBridge.MessageKey;
import io.github.kosyakmakc.authBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.authBridge.SocialPlatforms.AuthorizeDuplicationException;
import io.github.kosyakmakc.authBridge.SocialPlatforms.SocialUser;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

public class CommitLoginCommand extends SocialCommandBase {
    public CommitLoginCommand() {
        super(
            "auth-login",
            List.of(
                CommandArgument.ofInteger("auth-code")));
    }

    @Override
    public void execute(SocialUser sender, List<Object> args) {
        var bridge = getBridge();
        var logger = bridge.getLogger();

        var authCode = (int) args.get(0);
        var placeholders = new HashMap<String, String>();

        try {
            AtomicBoolean isSuccess = new AtomicBoolean(false);
            bridge.queryDatabase(database -> {
                var availableSessions = database.sessions.queryBuilder()
                        .orderBy(AuthSession.EXPIRED_AT_FIELD_NAME, true)
                        .where()
                        .eq(AuthSession.AUTH_CODE_FIELD_NAME, authCode)
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

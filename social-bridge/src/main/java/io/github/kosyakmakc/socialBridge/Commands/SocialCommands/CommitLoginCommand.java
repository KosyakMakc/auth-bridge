package io.github.kosyakmakc.socialBridge.Commands.SocialCommands;

import io.github.kosyakmakc.socialBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.Tables.AuthSession;
import io.github.kosyakmakc.socialBridge.MessageKey;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.AuthorizeDuplicationException;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.SocialUser;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

public class CommitLoginCommand extends SocialCommandBase {
    public CommitLoginCommand() {
        super(
            "login",
            List.of(
                CommandArgument.ofInteger("auth-code")));
    }

    @Override
    public void execute(SocialUser sender, List<Object> args) {
        var bridge = getBridge();
        var logger = bridge.getLogger();

        var authCode = (int) args.getFirst();
        var placeholders = new HashMap<String, String>();

        try {
            AtomicReference<CommitLoginState> commitState = new AtomicReference<>(CommitLoginState.NotCommited);
            bridge.queryDatabase(database -> {
                var availableSessions = database.sessions.queryBuilder()
                        .orderBy(AuthSession.EXPIRED_AT_FIELD_NAME, true)
                        .where()
                        .eq(AuthSession.AUTH_CODE_FIELD_NAME, authCode)
                        .and()
                        .eq(AuthSession.IS_SPENT_FIELD_NAME, false)
                        .and()
                        .gt(AuthSession.EXPIRED_AT_FIELD_NAME, Date.from(Instant.now()))
                        .query();

                if (availableSessions.isEmpty()) {
                    commitState.set(CommitLoginState.NotCommited);
                    return null;
                }

                var session = availableSessions.getFirst();

                try {
                    sender.getPlatform().Authorize(sender, session.getMinecraftId());
                    commitState.set(CommitLoginState.Commited);
                } catch (AuthorizeDuplicationException e) {
                    commitState.set(CommitLoginState.DuplicationError);
                }

                session.spend();
                database.sessions.update(session);

                return null;
            });

            placeholders.put("social-platform-name", sender.getPlatform().getPlatformName());
            var resultState = commitState.get();
            switch (resultState) {
                case Commited -> {
                    logger.info(sender.getName() + " success commited login to " + sender.getPlatform().getPlatformName() + " platform");
                    sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.COMMITED_LOGIN), placeholders);
                }
                case NotCommited -> {
                    logger.info(sender.getName() + " failed to commit login");
                    sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.COMMIT_LOGIN_FAILED), placeholders);
                }
                case DuplicationError -> {
                    logger.info(sender.getName() + " duplicating his logins to " + sender.getPlatform().getPlatformName() + ", ignoring it...");
                    sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.YOU_ARE_ALREADY_AUTHORIZED), placeholders);
                }
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "failed to commit login", e);
            sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.INTERNAL_SERVER_ERROR), placeholders);
        }
    }

    enum CommitLoginState {
        Commited,
        NotCommited,
        DuplicationError,
    }
}

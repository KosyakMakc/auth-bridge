package io.github.kosyakmakc.authBridge.MinecraftCommands;

import io.github.kosyakmakc.authBridge.DatabasePlatform.Tables.AuthSession;
import io.github.kosyakmakc.authBridge.MessageKey;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import io.github.kosyakmakc.authBridge.Permissions;

import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

public class LoginCommand extends MinecraftCommandBase {
    private final Random random;

    public LoginCommand() {
        super(Permissions.CAN_LOGIN);
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public boolean canExecute(MinecraftUser sender, String[] args) {
        return sender != null
            && args.length == 1
            && args[0].equalsIgnoreCase("login");
    }

    @Override
    public void execute(MinecraftUser sender, String[] args) {
        var bridge = getBridge();
        var logger = bridge.getLogger();

        var code = random.nextInt() % 1_000_000;
        var message = getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.LOGIN_FROM_MINECRAFT);
        var placeholders = new HashMap<String, String>();
        placeholders.put("placeholder-code", Integer.toString(code));

        var sessionDbRecord = new AuthSession(sender.getId(), code, Duration.ofMinutes(10));
        try {
            bridge.queryDatabase(databaseContext -> {
                databaseContext.sessions.create(sessionDbRecord);
                return null;
            });

            logger.info(sender.getName() + " start login session");
            sender.sendMessage(message, placeholders);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "failed save auth session to database", e);
            sender.sendMessage(getBridge().getLocalizationService().getMessage(sender.getLocale(), MessageKey.INTERNAL_SERVER_ERROR), new HashMap<>());
        }
    }
}

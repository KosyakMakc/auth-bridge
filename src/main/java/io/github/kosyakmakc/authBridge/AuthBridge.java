package io.github.kosyakmakc.authBridge;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import io.github.kosyakmakc.authBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.authBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.authBridge.DatabasePlatform.*;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.authBridge.SocialPlatforms.ISocialPlatform;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class AuthBridge implements IAuthBridge {
    public static final String databasePath = "auth-bridge.sqlite";

    public static AuthBridge INSTANCE;

    private final IMinecraftPlatform minecraftPlatform;
    private final List<ISocialPlatform> socialPlatforms;
    private final DatabaseContext databaseContext;

    private final ConfigurationService configurationService;
    private final LocalizationService localizationService;


    private final List<IMinecraftCommand> mcCommands;
    private final List<ISocialCommand> socialCommands;

    private AuthBridge(IMinecraftPlatform mcPlatform) throws SQLException, IOException {
        minecraftPlatform = mcPlatform;
        socialPlatforms = ServiceLoader.load(ISocialPlatform.class).stream().map(ServiceLoader.Provider::get).toList();

        databaseContext = new DatabaseContext(this, new JdbcConnectionSource("jdbc:sqlite:" + Path.of(mcPlatform.getDataDirectory().toAbsolutePath().toString(), databasePath)));

        configurationService = new ConfigurationService(this);
        localizationService = new LocalizationService(this);

        new ApplyDatabaseMigrations().accept(this);
        localizationService.restoreDatabase();

        minecraftPlatform.setAuthBridge(this);
        for (var socialPlatform : socialPlatforms) {
            socialPlatform.setAuthBridge(this);
        }

        mcCommands = ServiceLoader.load(IMinecraftCommand.class, IMinecraftCommand.class.getClassLoader()).stream().map(ServiceLoader.Provider::get).toList();
        socialCommands = ServiceLoader.load(ISocialCommand.class, ISocialCommand.class.getClassLoader()).stream().map(ServiceLoader.Provider::get).toList();

        getLogger().info("[DEBUG] mcCommand(" + mcCommands.size() + "):");
        for (var mcCommand : mcCommands) {
            getLogger().info("\t\t" + mcCommand.getClass().getName());
            mcCommand.init(this);
        }
        getLogger().info("[DEBUG] socialCommand(" + socialCommands.size() + "):");
        for (var socialCommand : socialCommands) {
            getLogger().info("\t\t" + socialCommand.getClass().getName());
            socialCommand.init(this);
        }
    }

    @Override
    public Logger getLogger() {
        return minecraftPlatform.getLogger();
    }

    @Override
    public LocalizationService getLocalizationService() {
        return localizationService;
    }

    @Override
    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    @Override
    public void queryDatabase(IDatabaseConsumer action) throws SQLException {
        synchronized (databaseContext) {
            databaseContext.withTransaction(() -> action.accept(databaseContext) );
        }
    }

    @Override
    public List<IMinecraftCommand> getMinecraftCommands() {
        return mcCommands;
    }

    @Override
    public List<ISocialCommand> getSocialCommands() {
        return socialCommands;
    }

    @Override
    public IMinecraftPlatform getMinecraftPlatform() {
        return minecraftPlatform;
    }

    public static void Init(IMinecraftPlatform minecraftPlatform) throws SQLException, IOException {
        INSTANCE = new AuthBridge(minecraftPlatform);
    }
}

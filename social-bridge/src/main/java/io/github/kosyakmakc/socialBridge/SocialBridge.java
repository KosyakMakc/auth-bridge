package io.github.kosyakmakc.socialBridge;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.*;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.ISocialPlatform;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class SocialBridge implements ISocialBridge {

    public static ISocialBridge INSTANCE;

    private final IMinecraftPlatform minecraftPlatform;
    private final List<ISocialPlatform> socialPlatforms;
    private final DatabaseContext databaseContext;

    private final ConfigurationService configurationService;
    private final LocalizationService localizationService;

    private final List<IMinecraftCommand> mcCommands;
    private final List<ISocialCommand> socialCommands;

    private boolean isStarted = false;

    private SocialBridge(IMinecraftPlatform mcPlatform) throws SQLException, IOException {
        minecraftPlatform = mcPlatform;
        socialPlatforms = new LinkedList<ISocialPlatform>();

        var connectionString = mcPlatform.get("connectionString", null);
        if (connectionString == null) {
            throw new RuntimeException("failed connect to database, check connectionString in config");
        }
        databaseContext = new DatabaseContext(this, new JdbcConnectionSource(connectionString));

        configurationService = new ConfigurationService(this);
        localizationService = new LocalizationService(this);

        new ApplyDatabaseMigrations().accept(this);
        localizationService.restoreDatabase();

        minecraftPlatform.setAuthBridge(this);

        mcCommands = ServiceLoader.load(IMinecraftCommand.class, IMinecraftCommand.class.getClassLoader()).stream().map(ServiceLoader.Provider::get).toList();
        socialCommands = ServiceLoader.load(ISocialCommand.class, ISocialCommand.class.getClassLoader()).stream().map(ServiceLoader.Provider::get).toList();

        getLogger().info("Minecraft commands(" + mcCommands.size() + "):");
        for (var mcCommand : mcCommands) {
            getLogger().info("\t\t" + mcCommand.getClass().getName());
            mcCommand.init(this);
        }
        getLogger().info("Social commands(" + socialCommands.size() + "):");
        for (var socialCommand : socialCommands) {
            getLogger().info("\t\t" + socialCommand.getClass().getName());
            socialCommand.init(this);
        }

        getLogger().info("Social platforms(" + socialPlatforms.size() + "):");
        for (var socialPlatform : socialPlatforms) {
            getLogger().info("\t\t" + socialPlatform.getClass().getName());
            socialPlatform.setAuthBridge(this);
        }
    }

    @Override
    public void Start() {
        if (isStarted) {
            throw new RuntimeException("Social bridge already running");
        }
        isStarted = true;
        for (ISocialPlatform socialPlatform : socialPlatforms) {
            socialPlatform.Start();
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
        if (INSTANCE != null) {
            throw new RuntimeException("Social bridge MUST BE single instance");
        }
        INSTANCE = new SocialBridge(minecraftPlatform);
    }

    @Override
    public void registerSocialPlatform(ISocialPlatform socialPlatform) {
        if (isStarted) {
            throw new RuntimeException("Social bridge already running, register your platform on startup please");
        }

        socialPlatforms.add(socialPlatform);
        socialPlatform.setAuthBridge(this);
    }

    @Override
    public void registerModule(IBridgeModule module) {
        if (isStarted) {
            throw new RuntimeException("Social bridge already running, register your module on startup please");
        }

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerModule'");
    }
}

package io.github.kosyakmakc.socialBridge;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.*;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.ISocialPlatform;

import java.io.IOException;
import java.lang.Runtime.Version;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class SocialBridge implements ISocialBridge {

    public static ISocialBridge INSTANCE;

    private final IMinecraftPlatform minecraftPlatform;
    private final LinkedList<ISocialPlatform> socialPlatforms;
    private final DatabaseContext databaseContext;

    private final ConfigurationService configurationService;
    private final LocalizationService localizationService;

    private final Map<IBridgeModule, List<IMinecraftCommand>> minecraftCommands = new Hashtable<>();
    private final Map<IBridgeModule, List<ISocialCommand>> socialCommands = new Hashtable<>();

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

        var totalMcCommands = minecraftCommands.values().stream().mapToInt(x -> x.size()).sum();
        getLogger().info("Minecraft commands(" + totalMcCommands + "):");
        for (var module : minecraftCommands.keySet()) {
            for (var mcCommand : minecraftCommands.get(module)) {
                getLogger().info("\t\t/" + module.getName() + ' ' + mcCommand.getLiteral());
                mcCommand.init(this);
            }
        }
        var totalSocialCommands = socialCommands.values().stream().mapToInt(x -> x.size()).sum();
        getLogger().info("Social commands(" + totalSocialCommands + "):");
        for (var module : socialCommands.keySet()) {
            for (var socialCommand : socialCommands.get(module)) {
                getLogger().info("\t\t/" + module.getName() + '-' + socialCommand.getLiteral());
                socialCommand.init(this);
            }
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
    public Map<IBridgeModule, List<IMinecraftCommand>> getMinecraftCommands() {
        return minecraftCommands;
    }

    @Override
    public Map<IBridgeModule, List<ISocialCommand>> getSocialCommands() {
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
    public boolean registerSocialPlatform(ISocialPlatform socialPlatform) {
        if (isStarted) {
            throw new RuntimeException("Social bridge already running, register your platform on startup please");
        }

        var logger = getLogger();
        logger.info("Registering social platform - " + socialPlatform.getPlatformName() + "(" +  socialPlatform.getCompabilityVersion().toString() + ")");

        var rootVersion = getVersion();
        var childVersion = socialPlatform.getCompabilityVersion();
        if (isCompatibleVersion(rootVersion, childVersion)) {
            logger.warning(socialPlatform.getPlatformName() + " have incompatible social-bridge API, ignoring it...");
            return false;
        }
        else {
            socialPlatforms.add(socialPlatform);
            socialPlatform.setAuthBridge(this);
            logger.warning(socialPlatform.getPlatformName() + " connected");
            return true;
        }
    }

    @Override
    public boolean registerModule(IBridgeModule module) {
        if (isStarted) {
            throw new RuntimeException("Social bridge already running, register your module on startup please");
        }

        var logger = getLogger();
        logger.info("Registering module - " + module.getName() + "(" +  module.getCompabilityVersion().toString() + ")");

        var rootVersion = getVersion();
        var childVersion = module.getCompabilityVersion();
        if (isCompatibleVersion(rootVersion, childVersion)) {
            logger.warning(module.getName() + " have incompatible social-bridge API, ignoring it...");
            return false;
        }
        else {
            socialCommands.put(module, List.copyOf(module.getSocialCommands()));
            minecraftCommands.put(module, List.copyOf(module.getMinecraftCommands()));
            logger.warning(module.getName() + " connected");
            return true;
        }
    }

    private boolean isCompatibleVersion(Version rootVersion, Version childVersion) {
        if (rootVersion.feature() != childVersion.feature()) {
            return false;
        }
        if (rootVersion.interim() < childVersion.interim()) {
            return false;
        }
        return true;
    }

    @Override
    public Version getVersion() {
        return minecraftPlatform.getSocialBridgeVersion();
    }
}

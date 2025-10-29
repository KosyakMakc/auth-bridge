package io.github.kosyakmakc.socialBridge;

import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.ConfigurationService;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.IDatabaseConsumer;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.LocalizationService;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.ISocialPlatform;

import java.lang.Runtime.Version;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public interface ISocialBridge {
    Logger getLogger();
    Version getVersion();

    LocalizationService getLocalizationService();
    ConfigurationService getConfigurationService();
    void queryDatabase(IDatabaseConsumer action) throws SQLException;

    Map<IBridgeModule, List<IMinecraftCommand>> getMinecraftCommands();
    Map<IBridgeModule, List<ISocialCommand>> getSocialCommands();

    IMinecraftPlatform getMinecraftPlatform();

    boolean registerSocialPlatform(ISocialPlatform socialPlatform);
    boolean registerModule(IBridgeModule module);

    void Start();
}

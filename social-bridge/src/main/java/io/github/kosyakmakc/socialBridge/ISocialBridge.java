package io.github.kosyakmakc.socialBridge;

import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.ConfigurationService;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.IDatabaseConsumer;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.LocalizationService;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.IMinecraftPlatform;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.ISocialPlatform;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public interface ISocialBridge {
    Logger getLogger();

    LocalizationService getLocalizationService();
    ConfigurationService getConfigurationService();
    void queryDatabase(IDatabaseConsumer action) throws SQLException;

    List<IMinecraftCommand> getMinecraftCommands();
    List<ISocialCommand> getSocialCommands();

    IMinecraftPlatform getMinecraftPlatform();

    void registerSocialPlatform(ISocialPlatform socialPlatform);
    void registerModule(IBridgeModule module);

    void Start();
}

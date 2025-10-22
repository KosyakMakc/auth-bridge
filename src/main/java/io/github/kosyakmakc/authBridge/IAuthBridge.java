package io.github.kosyakmakc.authBridge;

import io.github.kosyakmakc.authBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.authBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.authBridge.DatabasePlatform.ConfigurationService;
import io.github.kosyakmakc.authBridge.DatabasePlatform.IDatabaseConsumer;
import io.github.kosyakmakc.authBridge.DatabasePlatform.LocalizationService;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.IMinecraftPlatform;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public interface IAuthBridge {
    Logger getLogger();
    LocalizationService getLocalizationService();
    ConfigurationService getConfigurationService();
    void queryDatabase(IDatabaseConsumer action) throws SQLException;

    List<IMinecraftCommand> getMinecraftCommands();
    List<ISocialCommand> getSocialCommands();

    IMinecraftPlatform getMinecraftPlatform();
}
